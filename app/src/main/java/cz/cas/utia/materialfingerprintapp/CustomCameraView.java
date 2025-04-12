package cz.cas.utia.materialfingerprintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.util.List;

import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing.ImageCapturedListener;
import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing.ImageReadyToBeCapturedListener;

public class CustomCameraView extends JavaCameraView implements CameraBridgeViewBase.CvCameraViewListener2 {

    public static final String TAG = "cameratag";

    private boolean targetImageLocked = false;
    private final TargetImage targetImage = new TargetImage();

    private int yOffset;
    private int cameraHeightRealPixels;

    private ImageCapturedListener imageCapturedListener;
    private ImageReadyToBeCapturedListener imageReadyToBeCapturedListener;

    public void setOnImageCapturedListener(ImageCapturedListener listener) {
        this.imageCapturedListener = listener;
    }

    public void setImageReadyToBeCapturedListener(ImageReadyToBeCapturedListener listener) {
        this.imageReadyToBeCapturedListener = listener;
    }

    public CustomCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int cameraFrameWidth, int cameraFrameHeight) {
        // Initialize any resources needed for processing
        double scale = getWidth() / (double) cameraFrameWidth;

        cameraHeightRealPixels = (int) (cameraFrameHeight * scale);

        yOffset = (getHeight() - cameraHeightRealPixels) / 2;
    }

    @Override
    public void onCameraViewStopped() {
        // Release resources if needed
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("dimens", "frame width: " + mFrameWidth + ", frame height: " + mFrameHeight);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            Log.d(TAG, "ACTION_DOWN is being handled.");

            double touchX = (event.getX() / (double) getWidth()) * mFrameWidth;
            double touchY = ((event.getY() - yOffset) / cameraHeightRealPixels) * mFrameHeight;

            Point clickPoint = new Point(touchX, touchY);

            if (targetImageLocked) {

                Log.d(TAG, "Clicked when target image is locked");


                MatOfPoint2f contour2f = new MatOfPoint2f(targetImage.cont.toArray());

                for (Point array : contour2f.toArray()) {
                    Log.d(TAG, array.toString());
                }

                if (Imgproc.pointPolygonTest(contour2f, clickPoint, false) >= 0) {

                    selectLastTargetPhoto();

                }

            }

        }

        return super.onTouchEvent(event);
    }

    public void selectLastTargetPhoto() {
        Mat materialImage = getInnerImagePart();

        Bitmap materialBitmap = Bitmap.createBitmap(materialImage.cols(), materialImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(materialImage, materialBitmap);

        //todo add check if listener not null?
        imageCapturedListener.onImageCaptured(materialBitmap);
    }

    private Mat getInnerImagePart() {
        MatOfPoint2f contour2f = new MatOfPoint2f(targetImage.getCont().toArray());
        MatOfPoint2f imagePoints = new MatOfPoint2f();

        Imgproc.approxPolyDP(contour2f, imagePoints, 50, true);


        final int TARGET_INNER_SIZE = 500;
        final double WIDTH_PERCENTAGE = 0.10543979463156594;
        final double HEIGHT_PERCENTAGE = 0.10543978453272496;

        int widthPadding = (int) (WIDTH_PERCENTAGE / (1 - WIDTH_PERCENTAGE) * TARGET_INNER_SIZE);
        int heightPadding = (int) (HEIGHT_PERCENTAGE / (1 - HEIGHT_PERCENTAGE) * TARGET_INNER_SIZE);


        int destWidth = TARGET_INNER_SIZE + 2 * widthPadding;
        int destHeight = TARGET_INNER_SIZE + 2 * heightPadding;


        MatOfPoint2f dstPoints = new MatOfPoint2f(
                new Point(0, 0),
                new Point(destWidth, 0),
                new Point(destWidth, destHeight),
                new Point(0, destHeight)
        );

        Mat homography = Calib3d.findHomography(imagePoints, dstPoints);

        Mat dst = new Mat(destHeight, destWidth, targetImage.getPhoto().type());

        Imgproc.warpPerspective(targetImage.getPhoto(), dst, homography, new Size(destWidth, destHeight));

        // Create a submatrix(slice of the image)
        // Define the region of interest (ROI)
        Rect roi = new Rect(widthPadding, heightPadding, TARGET_INNER_SIZE, TARGET_INNER_SIZE);

        return new Mat(dst, roi);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        // Perform image processing here
        Mat imageMat = inputFrame.rgba();


        Mat gray = new Mat();
        Mat blackTh = new Mat();


        Imgproc.cvtColor(imageMat, gray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.threshold(gray, blackTh, 50, 255, Imgproc.THRESH_BINARY_INV);

        List<MatOfPoint> contours = ImageAnalyzer.findRects(blackTh, 500, 0.15);

        Log.d(TAG, "Found: " + contours.size() + " contour(s).");

        // TODO: handle situations, where the phone is laying on the table and the whole image is a black contour
        // TODO: in that case, the rect shouldn't be detected
        // TODO: the contour area shouldn't be too close to the whole image area

        if (contours.size() == 1) {
            Scalar color;
            MatOfPoint rectCont = contours.get(0);
            if (ImageAnalyzer.isStraight(rectCont, 5) && ImageAnalyzer.isRect(rectCont, 0.1)) {
                color = new Scalar(0, 255, 0);
                targetImageLocked = true;

                imageReadyToBeCapturedListener.onImageReadyToBeCaptured(true);

                targetImage.setCont(rectCont);
                targetImage.setPhoto(imageMat);
            } else {
                color = new Scalar(255, 0, 0);
                targetImageLocked = false;

                imageReadyToBeCapturedListener.onImageReadyToBeCaptured(false);

            }
            Imgproc.drawContours(imageMat, contours, -1, color, 5);


            //TODO
            // listen for click within the contour which has to be green
            // on click - display the straightened rect image with two buttons - retake and use
            // to the same thing for non-specular photo
        } else {
            targetImageLocked = false;

            imageReadyToBeCapturedListener.onImageReadyToBeCaptured(false);
        }


        return imageMat;
    }

    public static class TargetImage {
        private Mat photo;
        private MatOfPoint cont;

        public void setPhoto(Mat photo) {
            this.photo = photo;
        }

        public Mat getPhoto() {
            return photo;
        }

        public void setCont(MatOfPoint cont) {
            this.cont = cont;
        }

        public MatOfPoint getCont() {
            return cont;
        }
    }
}
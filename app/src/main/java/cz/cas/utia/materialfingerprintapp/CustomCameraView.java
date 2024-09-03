package cz.cas.utia.materialfingerprintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import cz.cas.utia.materialfingerprintapp.ImageAnalyzer;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
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
import java.util.zip.Inflater;

public class CustomCameraView extends JavaCameraView implements CameraBridgeViewBase.CvCameraViewListener2 {

    public static final String TAG = CustomCameraView.class.getSimpleName();

    private Context context;

    private boolean targetImageLocked = false;
    private final TargetImage targetImage = new TargetImage();


    public CustomCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCvCameraViewListener(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        // Initialize any resources needed for processing
    }

    @Override
    public void onCameraViewStopped() {
        // Release resources if needed
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            Log.d("nesmysly", "Clicked");

            float touchX = event.getX();
            float touchY = event.getY();

            Point clickPoint = new Point(touchX, touchY);

            if (targetImageLocked) {
                Log.d("nesmysly", "In target image locked");
                Log.d("nesmysly", "clickPoint: " + clickPoint);

                MatOfPoint2f contour2f = new MatOfPoint2f(targetImage.cont.toArray());
                for(Point point : contour2f.toList()){
                    Log.d("nesmysly", "contour: " + point.x +" " + point.y);
                }

                if (Imgproc.pointPolygonTest(contour2f, clickPoint, false) >= 0) {
                    Log.d("nesmysly", "Clicked inside the polygon");
                    selectLastTargetPhoto();
                }

            }

        }

        return super.onTouchEvent(event);
    }

    private void selectLastTargetPhoto() {
        Mat materialImage = getInnerImagePart();

        Bitmap materialBitmap = Bitmap.createBitmap(materialImage.cols(), materialImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(materialImage, materialBitmap);

        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Find the ImageView and Buttons in the popup layout
        ImageView imageView = popupView.findViewById(R.id.popup_image_view);
        Button btn1 = popupView.findViewById(R.id.btn1);
        Button btn2 = popupView.findViewById(R.id.btn2);

        // Set the Bitmap to the ImageView
        imageView.setImageBitmap(materialBitmap);

        // Set up buttons
        btn1.setOnClickListener(v -> {
            // Handle button 1 click
        });

        btn2.setOnClickListener(v -> {
            // Handle button 2 click
        });

        // Create and show the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        // Show the PopupWindow (adjust the anchor view as needed)
        popupWindow.showAtLocation(this,
                android.view.Gravity.CENTER,
                0,
                0);

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

        // Create a submatrix (slice of the image)
        Rect roi = new Rect(widthPadding, heightPadding, TARGET_INNER_SIZE, TARGET_INNER_SIZE); // Define the region of interest (ROI)
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

        Log.d(TAG, "Found: " + contours.size() + " contours.");

        if (contours.size() == 1) {
            Scalar color;
            MatOfPoint rectCont = contours.get(0);
            if (ImageAnalyzer.isStraight(rectCont, 5) && ImageAnalyzer.isRect(rectCont, 0.1)) {
                color = new Scalar(0, 255, 0);
                targetImageLocked = true;
                targetImage.setCont(rectCont);
                targetImage.setPhoto(imageMat);
            } else {
                color = new Scalar(255, 0, 0);
                targetImageLocked = false;
            }
            Imgproc.drawContours(imageMat, contours, -1, color, 5);


            //TODO
            // listen for click within the contour which has to be green
            // on click - display the straightened rect image with two buttons - retake and use
            // to the same thing for non-specular photo
        } else {
            targetImageLocked = false;
        }


        return imageMat;
    }

    public class TargetImage {
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

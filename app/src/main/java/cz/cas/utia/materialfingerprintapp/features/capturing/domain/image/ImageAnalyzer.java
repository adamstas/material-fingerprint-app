package cz.cas.utia.materialfingerprintapp.features.capturing.domain.image;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

// code by Daniel Pilar
public class ImageAnalyzer {
    public static List<MatOfPoint> findRects(Mat segmentedImg, double minContourArea, double maxRelativeAreaDiff) {
        List<MatOfPoint> contours = new ArrayList<>();
        List<MatOfPoint> filteredContours = new ArrayList<>();

        // Find contours
        Imgproc.findContours(segmentedImg, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double contourArea = Imgproc.contourArea(contour);

            RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));
            Size dimensions = rotatedRect.size;
            double width = dimensions.width;
            double height = dimensions.height;
            double enclosingRectArea = width * height;

            if (enclosingRectArea != 0 && contourArea > minContourArea) {
                if (Math.abs(1 - contourArea / enclosingRectArea) <= maxRelativeAreaDiff) {
                    filteredContours.add(contour);
                }
            }
        }

        return filteredContours;


    }

    public static boolean isStraight(MatOfPoint contour, double tolerance) {
        // Calculate the minimum area rectangle for the contour
        RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

        // Get the angle of the rectangle
        double angle = rotatedRect.angle;

        // Adjust the angle to be within [0, 45]
        angle = Math.min(angle, 90 - angle);

        // Return true if the angle is within the tolerance
        return Math.abs(angle) <= tolerance;
    }

    public static boolean isRect(MatOfPoint contour, double relativeTolerance) {
        // Calculate the minimum area rectangle for the contour
        RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

        // Get the dimensions of the rectangle
        Size dimensions = rotatedRect.size;
        double width = dimensions.width;
        double height = dimensions.height;

        // Print the width and height (similar to Python's print statement)
        System.out.println("Width: " + width + ", Height: " + height);

        // Check if the relative difference between width and height is within tolerance
        return Math.abs(1 - width / height) <= relativeTolerance;
    }
}

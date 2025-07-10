package s05t02.interactiveCV.service.pdfService;

import s05t02.interactiveCV.model.documents.entries.concreteEntries.ProfilePicture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageShapeProcessor {

    /**
     * Processes an image file to the desired shape, returning a new BufferedImage.
     * param inputFile the source image file
     *
     * @param shape the desired shape (ROUND, SQUARE, etc.)
     * @return a new BufferedImage with the shape applied
     */
    public static BufferedImage processImage(BufferedImage original, ProfilePicture.Shape shape) throws Exception {
        if (original == null) throw new IllegalArgumentException("Could not read image!");

        switch (shape) {
            case ROUND:
                return applyCircularMask(original);
            case SQUARE:
                return cropSquare(original);
            case RECTANGLE:
            case MATCH:
                return original;
            case STAR:
                return applyStarMask(original);
            default:
                throw new UnsupportedOperationException("Unknown shape: " + shape);
        }
    }

    /**
     * Crops the image to a square (center crop).
     */
    private static BufferedImage cropSquare(BufferedImage img) {
        int size = Math.min(img.getWidth(), img.getHeight());
        int x = (img.getWidth() - size) / 2;
        int y = (img.getHeight() - size) / 2;
        return img.getSubimage(x, y, size, size);
    }

    /**
     * Applies a circular mask to the image.
     */
    private static BufferedImage applyCircularMask(BufferedImage img) {
        // Ensure square base for circle
        BufferedImage square = cropSquare(img);
        int size = square.getWidth();

        BufferedImage masked = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = masked.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle mask
        g2.setClip(new Ellipse2D.Double(0, 0, size, size));
        g2.drawImage(square, 0, 0, null);
        g2.dispose();

        return masked;
    }

    /**
     * Placeholder: returns the original image with no star mask.
     * Real implementation would need a complex star alpha mask.
     */
    private static BufferedImage applyStarMask(BufferedImage img) {
        // Crop to square for consistent star shape
        BufferedImage square = cropSquare(img);
        int size = square.getWidth();

        // Create output with transparency
        BufferedImage masked = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = masked.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create the star shape
        Shape star = createStar(size / 2.0, size / 2.0, size / 2.0, size * 0.4, 5);

        // Apply the clip
        g2.setClip(star);
        g2.drawImage(square, 0, 0, null);
        g2.dispose();

        return masked;
    }


    /**
     * Utility to save the result to disk.
     */
    public static void saveImage(BufferedImage img, String format, File outputFile) throws Exception {
        ImageIO.write(img, format, outputFile);
    }

    /**
     * Creates a regular star shape.
     *
     * @param centerX     Center X
     * @param centerY     Center Y
     * @param outerRadius Outer radius of star points
     * @param innerRadius Inner radius (between points)
     * @param numRays     Number of star points
     * @return Shape of the star
     */
    private static Shape createStar(double centerX, double centerY, double outerRadius, double innerRadius, int numRays) {
        double angle = Math.PI / numRays;
        GeneralPath path = new GeneralPath();

        for (int i = 0; i < numRays * 2; i++) {
            double r = (i % 2 == 0) ? outerRadius : innerRadius;
            double theta = i * angle - Math.PI / 2;
            double x = centerX + Math.cos(theta) * r;
            double y = centerY + Math.sin(theta) * r;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }

}


package s05t02.interactiveCV.service.pdfService;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s05t02.interactiveCV.model.documents.WithProfilePicture;
import s05t02.interactiveCV.service.cloud.CloudMetaData;
import s05t02.interactiveCV.service.cloud.CloudinaryMetaData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class Utils {
    static private Logger log = LoggerFactory.getLogger(Utils.class);

    public static BufferedImage fetchImageFromUrl(String imageUrl) throws Exception {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL is null or blank!");
        }
        try {
            URL url = new URL(imageUrl);
            return ImageIO.read(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL format: " + imageUrl, e);
        }
    }


    public static String imageToBase64(BufferedImage image, String format) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static String toDataUrl(String base64, String format) {
        return "data:image/" + format.toLowerCase() + ";base64," + base64;
    }

    public static WithProfilePicture processImageInDoc(WithProfilePicture document) throws Exception {
        if (!hasPicture(document)) return document;
        log.atDebug().log("Url  is {}", document.getProfilePicture().getDocumentCloudMetadata().publicUrl());
        BufferedImage imageFile = fetchImageFromUrl(document.getProfilePicture().getDocumentCloudMetadata().publicUrl());
        BufferedImage processedImage = ImageShapeProcessor.processImage(imageFile, document.getProfilePicture().getShape());
        String encodedImage = imageToBase64(processedImage, "PNG");
        CloudMetaData newMetadata = new CloudinaryMetaData(document.getProfilePicture().getDocumentCloudMetadata().id(), toDataUrl(encodedImage, "PNG"));
        document.getProfilePicture().setDocumentCloudMetadata(newMetadata);
        return document;
    }

    private static boolean hasPicture(WithProfilePicture document) {
        return document.getProfilePicture() != null && document.getProfilePicture().getDocumentCloudMetadata() != null && !document.getProfilePicture().getDocumentCloudMetadata().publicUrl().isEmpty() && document.getProfilePicture().getDocumentCloudMetadata().publicUrl() != null;
    }

    public static void registerPoppinsFonts(PdfRendererBuilder builder) {
        try {
            // Light
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-Light.ttf"), "Poppins", 300, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-LightItalic.ttf"), "Poppins", 300, BaseRendererBuilder.FontStyle.ITALIC, true);

            // Regular
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-Regular.ttf"), "Poppins", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-Italic.ttf"), "Poppins", 400, BaseRendererBuilder.FontStyle.ITALIC, true);

            // Bold
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-Bold.ttf"), "Poppins", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-BoldItalic.ttf"), "Poppins", 700, BaseRendererBuilder.FontStyle.ITALIC, true);

            // Black
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-Black.ttf"), "Poppins", 900, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(() -> Utils.class.getResourceAsStream("/fonts/Poppins-BlackItalic.ttf"), "Poppins", 900, BaseRendererBuilder.FontStyle.ITALIC, true);

        } catch (Exception e) {
            throw new RuntimeException("Failed to register Poppins fonts", e);
        }
    }

}

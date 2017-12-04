package cmput301f17t09.goalsandhabits.Main_Habits;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Simone on 2017/12/2.
 */

public class ImageController {
    /**
     * Convert an image to base64
     * @param image the image to convert
     * @return the image as a base64 string
     */
    public static String imageToBase64(Bitmap image){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // put image into the output stream
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Convert a base64 string to an image
     * @param base64 the string to decode
     * @return the string as an image
     */
    public static Bitmap base64ToImage(String base64){
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        return BitmapFactory.decodeStream(is);
    }

    /**
     * Compress an image as much as possible until the size in bytes is no more than the specified amount
     * @param image the image to compress
     * @param maxBytes the maximum size of the image in bytes
     * @return the compressed image if it was possible to get it to that size, or null otherwise
     */
    public static Bitmap compressImageToMax(Bitmap image, int maxBytes){
        int oldSize = image.getByteCount();

        // attempt to resize the image as much as possible while valid
        while (image != null && image.getByteCount() > maxBytes){

            // Prevent image from becoming too small
            if (image.getWidth() <= 20 || image.getHeight() <= 20)
                return null;

            // scale down the image by a factor of 2
            image = Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false);

            // the byte count did not change for some reason, can not be made any smaller
            if (image.getByteCount() == oldSize)
                return null;

            oldSize = image.getByteCount();
        }

        return image;
    }

}

package com.example.dopetheimmortal.turntapp.Useful;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * Created by jackson on 2016/05/29.
 */
public class ImageProcessing {
    public Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedByte, 0,
                decodedByte.length);
        return bmp;
    }

    public String encode(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] b = baos.toByteArray();
            String str = Base64.encodeToString(b, Base64.DEFAULT);
            return str;
        } catch (Exception e) {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            return encode(bitmap);
        }
    }

    public Bitmap resize(Bitmap received) {
        if (received.getHeight() > received.getWidth()) {
            if (received.getHeight() > 1000) {
                received = Bitmap.createScaledBitmap(received, received.getWidth() / 2, received.getHeight() / 2, true);
                if (received.getHeight() > 1000) {
                    received = resize(received);
                }
            }
        } else if (received.getHeight() < received.getWidth()) {
            if (received.getWidth() > 1000) {
                received = Bitmap.createScaledBitmap(received, received.getWidth() / 2, received.getHeight() / 2, true);
                if (received.getWidth() > 1000) {
                    received = resize(received);
                }
            }
        } else {
            received = Bitmap.createScaledBitmap(received, received.getWidth() / 2, received.getHeight() / 2, true);
            if (received.getWidth() > 1000) {
                received = resize(received);
            }
        }
        return received;
    }
    public String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/reports");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return root+"/"+fname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public Bitmap decodeSampledBitmapFromResource(String link, int reqWidth, int reqHeight, BitmapFactory.Options options) throws IOException {

        InputStream in = new URL(link).openStream();
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in, null, options);
    }
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
//        System.out.println(height+" "+width);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

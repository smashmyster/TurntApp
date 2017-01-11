package com.example.dopetheimmortal.turntapp.connector;

/**
 * Created by jackson on 2016/07/18.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.dopetheimmortal.turntapp.Useful.ImageProcessing;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by jackson on 2016/06/25.
 */
public class GetImage extends AsyncTask<Bitmap, Bitmap, Bitmap> {
    String link;
    Context context;
    ImageView image_view;

    public GetImage(String link, Context context, ImageView image) {
        this.link = link;
        this.context = context;
//        this.menu_interface=menu_interface;
        System.gc();
        this.image_view = image;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        Bitmap image = null;

        try {
            try {
//                image = new_name ImageProcessing().get_saved_image(food.image,"restaurants/menus");
                if (image == null) {
                    InputStream in = new URL(link).openStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(in, null, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    float ratio = imageWidth / imageHeight;
                    int new_height = (int) (200 * ratio);
                    image = new ImageProcessing().decodeSampledBitmapFromResource(link, 200, new_height, options);
                    //new_name ImageProcessing().createImageFile(food.image, image,"restaurants/menus");
                } else {
                    return image;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
//            BitmapFactory.Options options=new_name BitmapFactory.Options();
//            options.inSampleSize=3;
//            InputStream in = new_name URL(food.image).openStream();
//                image = BitmapFactory.decodeStream(in,null,options);
//            if (image.getHeight()>1000 && image.getWidth()>1000){
//                image=new_name ImageProcessing().resize_to_smaller(image);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        System.gc();
        if (bitmap != null)
            image_view.setImageBitmap(bitmap);
        System.gc();
    }
}

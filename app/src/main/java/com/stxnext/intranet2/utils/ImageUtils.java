package com.stxnext.intranet2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.File;

/**
 * Created by Tomasz Konieczny on 2014-08-07.
 */
public class ImageUtils {

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius, int border, int color) {
        Bitmap scaledBitmap;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            scaledBitmap = ThumbnailUtils.extractThumbnail(bmp, radius - 2, radius - 2);
        } else {
            scaledBitmap = bmp;
        }

        Bitmap output = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(scaledBitmap.getWidth() / 2, scaledBitmap.getHeight() / 2, scaledBitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        if (border > 0) {
            paint.setStrokeWidth(border);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(scaledBitmap.getWidth() / 2, scaledBitmap.getHeight() / 2, scaledBitmap.getWidth() / 2, paint);
        }

        return output;
    }

}

package com.stxnext.intranet2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.utils.ImageUtils;

/**
 * Created by Tomasz Konieczny on 2014-08-01.
 */
public class RoundedImageView extends ImageView {

    private Bitmap roundedBitmap;
    private int borderColor;
    private int borderSize;

    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(attrs);
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.RoundedImageView_borderColor) {
                int borderColorRes = typedArray.getResourceId(attr, NO_ID);
                this.borderColor = borderColorRes != NO_ID ? getContext().getResources().getColor(borderColorRes) : Color.WHITE;
            } else if (attr == R.styleable.RoundedImageView_borderSize) {
                int borderSizeRes = typedArray.getResourceId(attr, NO_ID);
                this.borderSize = borderSizeRes != NO_ID ? getContext().getResources().getDimensionPixelSize(borderSizeRes) : 10;
            }
        }

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        if (roundedBitmap == null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            int radius = getHeight();
            roundedBitmap = ImageUtils.getCroppedBitmap(bitmap, radius, borderSize, borderColor);
        }

        int centerWidth = canvas.getWidth() / 2 - roundedBitmap.getWidth() / 2;
        int centerHeight = canvas.getHeight() / 2 - roundedBitmap.getHeight() / 2;
        canvas.drawBitmap(roundedBitmap, centerWidth, centerHeight, null);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (roundedBitmap != null) {
            roundedBitmap.recycle();
            this.roundedBitmap = null;
        }
    }
}

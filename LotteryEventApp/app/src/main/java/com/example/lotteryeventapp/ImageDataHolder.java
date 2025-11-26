package com.example.lotteryeventapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class ImageDataHolder {
    byte[] imageBlob;
    String uid;
    final int MAX_SIZE_BYTES = 1048576;

    public ImageDataHolder(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        }else if (drawable instanceof VectorDrawable) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }else {
            throw new UnsupportedOperationException("Unsupported drawable type.");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.imageBlob = stream.toByteArray();
        bitmap.recycle();

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public ImageDataHolder(byte[] imageBlob) {
        this.imageBlob = imageBlob;

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(this.imageBlob, 0, this.imageBlob.length);
    }

    private boolean exceedsMaxDocumentSize() {
        int byteSize = 0;

        String key1 = "uid";
        byteSize += key1.getBytes(StandardCharsets.UTF_8).length + 1;
        byteSize += 20; // Auto uid generation have max length of 20 ascii characters

        String key2 = "imageBlob";
        byteSize += key2.getBytes(StandardCharsets.UTF_8).length + 1;
        byteSize += this.imageBlob.length;

        return byteSize > this.MAX_SIZE_BYTES;
    }

    // Getters and Setters
    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] blob) {
        this.imageBlob = blob;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
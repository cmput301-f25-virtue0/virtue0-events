package com.example.lotteryeventapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.google.firebase.firestore.Blob;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ImageDataHolder {
    Blob imageBlob;
    String uid = "";
    final int MAX_SIZE_BYTES = 1048576;

    public ImageDataHolder(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Check if version contains hardware bitmaps
                if (bitmap.getConfig() == Bitmap.Config.HARDWARE || bitmap.getConfig() == null) {
                    // Convert hardware bitmap to software bitmap
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
            }
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
        this.imageBlob = Blob.fromBytes(stream.toByteArray());
        bitmap.recycle();

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public ImageDataHolder(Blob blob) {
        this.imageBlob = blob;

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public ImageDataHolder(byte[] data) {
        this.imageBlob = Blob.fromBytes(data);

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public ImageDataHolder(Map<String, Object> data) {
        this.uid = (String) data.get("uid");

        Object objectBlob = data.get("imageBlob");
        this.imageBlob = (Blob) objectBlob;

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    public Bitmap getBitmap() {
        byte[] data = this.imageBlob.toBytes();
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private boolean exceedsMaxDocumentSize() {
        int byteSize = 0;

        String key1 = "uid";
        byteSize += key1.getBytes(StandardCharsets.UTF_8).length + 1;
        byteSize += 20; // Auto uid generation have max length of 20 ascii characters

        String key2 = "imageBlob";
        byteSize += key2.getBytes(StandardCharsets.UTF_8).length + 1;
        byte[] data = this.imageBlob.toBytes();
        byteSize += data.length;

        return byteSize > this.MAX_SIZE_BYTES;
    }

    // Getters and Setters
    public Blob getImageBlob() {
        return this.imageBlob;
    }

    public void setImageBlob(Blob blob) {
        this.imageBlob = blob;
    }

    public void setImageBlob(byte[] data) {
        this.imageBlob = Blob.fromBytes(data);
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
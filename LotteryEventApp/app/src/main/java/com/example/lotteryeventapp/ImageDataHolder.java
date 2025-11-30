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

    /**
     * construct ImageDataHolder from an ImageView
     * @param imageView ImageView ImageDataHolder is constructed by
     */
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

    /**
     * Construct ImageDataHolder from Blob
     * @param blob Blob ImageDataHolder is based on
     */
    public ImageDataHolder(Blob blob) {
        this.imageBlob = blob;

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    /**
     * construct ImageDataHolder from byte array
     * @param data byte array ImageDataHolder is constructed from
     */
    public ImageDataHolder(byte[] data) {
        this.imageBlob = Blob.fromBytes(data);

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    /**
     * construct ImageDataHolder from map from database
     * @param data data from database
     */
    public ImageDataHolder(Map<String, Object> data) {
        this.uid = (String) data.get("uid");

        Object objectBlob = data.get("imageBlob");
        this.imageBlob = (Blob) objectBlob;

        if (this.exceedsMaxDocumentSize()) {
            throw new UnsupportedOperationException("Image is too large.");
        }
    }

    /**
     * converts ImageDataHolder into a bitmap
     * @return Bitmap based on ImageDataHolder
     */
    public Bitmap convertToBitmap() {
        byte[] data = this.imageBlob.toBytes();
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * Image is larger than allowed image size
     * @return whether ImageDataHolder is too large
     */
    public boolean exceedsMaxDocumentSize() {
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

    /**
     * get blob of ImageDataHolder
     * @return blob of ImageDataHolder
     */
    public Blob getImageBlob() {
        return this.imageBlob;
    }
    /**
     * set blob of ImageDataHolder
     * @param blob blob of ImageDataHolder
     */
    public void setImageBlob(Blob blob) {
        this.imageBlob = blob;
    }

    /**
     * get uid of ImageDataHolder
     * @return uid of ImageDataHolder
     */
    public String getUid() {
        return this.uid;
    }
    /**
     * set uid of ImageDataHolder
     * @param uid uid of ImageDataHolder
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
package com.vivo.hybrid.game.sharememdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * [description]
 * author: yifei
 * created at 2019/8/10 下午10:15
 */
public class BitmapUtil {


    public static byte[] Bitmap2ByteArray(Bitmap bmp) {
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        return buf.array();
    }


    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}

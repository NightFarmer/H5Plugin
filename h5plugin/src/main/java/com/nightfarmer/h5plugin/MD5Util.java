package com.nightfarmer.h5plugin;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by zhangfan on 2016/5/10 0010.
 */
public class MD5Util {
    public static String encode32(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : digest.digest()) {
                builder.append(Integer.toHexString((b >> 4) & 0xf));
                builder.append(Integer.toHexString(b & 0xf));
            }
            return builder.toString().toUpperCase(
                    Locale.getDefault());
        } catch (Exception ignored) {
        }
        return null;
    }
}

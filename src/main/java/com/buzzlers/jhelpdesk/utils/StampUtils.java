package com.buzzlers.jhelpdesk.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class StampUtils {

    public static String craeteStampFromObjects(Object first, Object... rest) {
        assert first != null;
        StringBuilder sb = new StringBuilder(Thread.currentThread().getName());
        sb.append(Thread.currentThread().getId());
        sb.append(first);
        for (Object o : rest) {
            sb.append(o);
        }
        sb.append(System.nanoTime());
        return DigestUtils.shaHex(sb.toString());
    }
}

        
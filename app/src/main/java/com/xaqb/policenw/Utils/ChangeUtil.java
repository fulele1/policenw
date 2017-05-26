package com.xaqb.policenw.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lenovo on 2017/3/3.
 */

public class ChangeUtil {
    public static String procRet(String s) {
        byte[] aData = s.getBytes();
//        Log.i("-------adata", new String(aData));
        if (aData.length == 0) return "1返回数据格式错误";
        int iEnd = 0, i;
        for (i = 2; i < aData.length; i++)
            if (aData[i] == 1) {
                iEnd = i;
                break;
            }
        if (iEnd == 0) return "1返回数据格式错误";
        {
            byte[] aTmp = new byte[iEnd - 1];
//            aTmp[0] = aData[0];
            for (i = 1; i < iEnd; i++)
                aTmp[i - 1] = aData[i];
            return new String(aTmp);
        }
    }






    //md5
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("不支持md5", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持utf-8", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }



}

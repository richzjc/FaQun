package com.wallstreetcn.data.Encrypt;


import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yuweichen on 15/3/19.
 */
public class HmacCoder {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "HmacSHA256";


    public static byte[] encode(byte[] key, byte[] data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return sha256_HMAC.doFinal(data);
    }

    /**
     * 使用HmacSHA256消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static byte[] encodeHmacSHA256(byte[] data, byte[] key) {
        Key k = toKey(key);
        return encodeHmacSHA256(data, k);
    }


    /**
     * 使用HmacSHA256消息摘要算法计算消息摘要
     *
     * @param data 做消息摘要的数据
     * @param key  密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static byte[] encodeHmacSHA256(byte[] data, Key key) {
        Mac mac = null;
        try {
            mac = Mac.getInstance(KEY_ALGORITHM);
            mac.init(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return mac.doFinal(data);
    }


    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key) {
        //生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    public static String showByteArray(byte[] data) {
        if (null == data) {
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for (byte b : data) {
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
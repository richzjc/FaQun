package com.wallstreetcn.data.Encrypt;


import android.text.TextUtils;
import android.util.Base64;

import com.wallstreetcn.data.R;
import com.wallstreetcn.helper.utils.ResourceUtils;

import java.util.Arrays;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by yuweichen on 15/12/21.
 */
public class EncryptUtil {

    /**
     * 获取key，key用于APP内所有的加密中
     * 选股宝AESKEY : 6fJF7W0ryq+cCJxyY/ki5dPJGD48MOojZ5/Z9iCbNH8=
     *
     * @param secret
     * @return
     */
    public static byte[] getKey(String secret) {
        try {
            byte[] decoderSecret = Base64.decode(secret, Base64.NO_WRAP);
            int length = decoderSecret.length;
            byte[] keyByte = Arrays.copyOf(decoderSecret, length / 2);

            return keyByte;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Board内加密字符串：获取key，data
     *
     * @param secret
     * @param message
     * @return
     */
    public static byte[] encoderMessage(String secret, String message) {
        try {
            //步骤一：得到data和key
            byte[] data = message.getBytes();
            byte[] key = getKey(secret);
            //步骤二：获取随机向量，长度16位
            IvParameterSpec iv = AESCoder.getIv();
            //步骤三：获取向量的byte值
            byte[] ivByte = iv.getIV();

            //步骤四：拼接iv与加密后的data
            byte[] dataToEnc = ByteUtil.byteMerger(ivByte, data);

            //步骤五：对dataToEnc进行AES/CBC/PKCS7Padding加密
            byte[] encryptAesData = AESCoder.encrypt(dataToEnc, key, iv);

            //步骤六：对encryptAesData进行Hmac-SHA256加密
            byte[] encryptHmacData = HmacCoder.encode(key, encryptAesData);

            //步骤七：拼接encryptAesData + encryptHmacData
            byte[] finalByte = ByteUtil.byteMerger(encryptAesData, encryptHmacData);

            return finalByte;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Board内解密字符串：获取key，分割byte数组，匹配hmac，解密aes
     *
     * @param secret
     * @param encryptByte
     * @return
     */
    public static String decoderMessage(String secret, byte[] encryptByte) {
        try {
            // TLog.d(TAG,"payload  is:"+ByteUtil.showByteArray(payload));
            int length = encryptByte.length;

            //hmac enc后长度为32
            int startOrEnd = length - 32;
            byte[] aes = Arrays.copyOf(encryptByte, startOrEnd);
            // TLog.d(TAG,"aes  is:"+ByteUtil.showByteArray(aes));

            byte[] hmac = Arrays.copyOfRange(encryptByte, startOrEnd, length);
            //  TLog.d(TAG,"hmac  is:"+ByteUtil.showByteArray(hmac));

            byte[] key = getKey(secret);
            if (validateHmac(key, aes, hmac)) {
                byte[] decordData = AESCoder.decrypt(aes, key, AESCoder.getIv());
                // TLog.d(TAG,ByteUtil.showByteArray(decordData));
                byte[] data = Arrays.copyOfRange(decordData, 16, decordData.length);
                String result = new String(data);
                //    TLog.d(TAG,result);

                return result;
            } else {
                return ResourceUtils.getResStringFromId(R.string.data_data_error_text);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResourceUtils.getResStringFromId(R.string.data_data_error_text);
        }

    }

    /**
     * 验证hmac是否相等
     *
     * @param key
     * @param aes
     * @param hmac
     * @return
     */
    public static boolean validateHmac(byte[] key, byte[] aes, byte[] hmac) {
        try {
            byte[] encryptToMac = HmacCoder.encode(key, aes);
            // TLog.d(TAG,"encryptToMac:"+ByteUtil.showByteArray(encryptToMac));
            // TLog.d(TAG,"mac         :"+ByteUtil.showByteArray(hmac));

            if (Arrays.equals(encryptToMac, hmac)) {
                //TLog.d(TAG,"is true");
                return true;
            } else {
                //TLog.d(TAG,"is false");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 加密一段字符串
     *
     * @param token
     * @param key
     * @return
     */
    public static String encode(String token, String key) {
        byte[] encryptToken = EncryptUtil.encoderMessage(key, token);
        token = Base64.encodeToString(encryptToken, Base64.NO_WRAP);
        return token;
    }

    /**
     * 解密一段字符串
     *
     * @param token
     * @param key
     * @return
     */
    public static String decode(String token, String key) {
        if (!TextUtils.isEmpty(token)) {
            byte[] encryptToken = Base64.decode(token.getBytes(), Base64.NO_WRAP);
            token = EncryptUtil.decoderMessage(key, encryptToken);
        }
        return token;
    }
}
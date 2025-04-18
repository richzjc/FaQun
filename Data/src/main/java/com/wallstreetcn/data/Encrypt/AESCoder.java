package com.wallstreetcn.data.Encrypt;


import com.wallstreetcn.helper.utils.TLog;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Coder<br/>
 * secret key length:	128bit, default:	128 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 * @author yuweichen
 *
 */
public class AESCoder {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 初始化密钥
     *
     * @return byte[] 密钥
     * @throws Exception
     */
    public static byte[] initSecretKey() {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
        //初始化此密钥生成器，使其具有确定的密钥大小
        //AES 要求密钥长度为 128
        kg.init(128);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key	二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key){
        //生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    public static IvParameterSpec getIv(){
        SecureRandom rnd = new SecureRandom();
        IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));
        return iv;
    }


    /**
     * 加密
     *
     * @param data	待加密数据
     * @param key	密钥
     * @return byte[]	加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,Key key,IvParameterSpec iv) throws Exception{
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM,iv);
    }

    /**
     * 加密
     *
     * @param data	待加密数据
     * @param key	二进制密钥
     * @return byte[]	加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,byte[] key,IvParameterSpec iv) throws Exception{
        return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM,iv);
    }


    /**
     * 加密
     *
     * @param data	待加密数据
     * @param key	二进制密钥
     * @param cipherAlgorithm	加密算法/工作模式/填充方式
     * @return byte[]	加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm,IvParameterSpec iv) throws Exception{
        //还原密钥
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm,iv);
    }

    /**
     * 加密
     *
     * @param data	待加密数据
     * @param key	密钥
     * @param cipherAlgorithm	加密算法/工作模式/填充方式
     * @return byte[]	加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm,IvParameterSpec iv) throws Exception{
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);
        //执行操作
        return cipher.doFinal(data);
    }



    /**
     * 解密
     *
     * @param data	待解密数据
     * @param key	二进制密钥
     * @return byte[]	解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,byte[] key,IvParameterSpec iv) throws Exception{
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM,iv);
    }

    /**
     * 解密
     *
     * @param data	待解密数据
     * @param key	密钥
     * @return byte[]	解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,Key key,IvParameterSpec iv) throws Exception{
        return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM,iv);
    }

    /**
     * 解密
     *
     * @param data	待解密数据
     * @param key	二进制密钥
     * @param cipherAlgorithm	加密算法/工作模式/填充方式
     * @return byte[]	解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm,IvParameterSpec iv) throws Exception{
        //还原密钥
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm,iv);
    }

    /**
     * 解密
     *
     * @param data	待解密数据
     * @param key	密钥
     * @param cipherAlgorithm	加密算法/工作模式/填充方式
     * @return byte[]	解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm,IvParameterSpec iv) throws Exception{
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key,iv);
        //执行操作
        TLog.d("",key.toString().length()+"");
        return cipher.doFinal(data);
    }

    public static String  showByteArray(byte[] data){
        if(null == data){
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for(byte b:data){
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }


}
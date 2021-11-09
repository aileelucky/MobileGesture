package com.shinemo.shinemogesture.util;

import android.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {
    private static byte[] encrypt(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    private static byte[] decrypt(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }


    private static byte[] encrypt1(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    private static byte[] decrypt1(byte[] text, byte[] key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    /**
     * @param text 明文
     * @param key  密钥
     * @date 2017年8月1日
     * @desc 加密
     */
    public static String encodeAES(String text, String key) throws Exception {
        byte[] keybBytes = DigestUtils.md5(key);
        byte[] passwdBytes = text.getBytes();
        byte[] aesBytyes = encrypt(passwdBytes, keybBytes);
        return Base64.encodeToString(aesBytyes,Base64.DEFAULT);
    }

    /**
     * @param password 密文
     * @param key      密钥
     * @date 2017年8月1日
     * @desc 解密
     */
    public static String deCodeAES(String password, String key) throws Exception {
        byte[] keybBytes = DigestUtils.md5(key);
        byte[] debase64Bytes = Base64.decode(password.getBytes(),Base64.DEFAULT);
        return new String(decrypt(debase64Bytes, keybBytes));
    }


    /**
     * @param text 明文
     * @param key  密钥
     * @date 2017年8月1日
     * @desc 加密
     */
    public static String encodeAES1(String text, String key) throws Exception {
        byte[] keybBytes = DigestUtils.md5(key);
        byte[] passwdBytes = text.getBytes();
        byte[] aesBytyes = encrypt1(passwdBytes, keybBytes);
        return Base64.encodeToString(aesBytyes,Base64.DEFAULT);
    }

    /**
     * @param password 密文
     * @param key      密钥
     * @date 2017年8月1日
     * @desc 解密
     */
    public static String deCodeAES1(String password, String key) throws Exception {
        byte[] keybBytes = DigestUtils.md5(key);
        byte[] debase64Bytes = Base64.decode(password.getBytes(),Base64.DEFAULT);
        return new String(decrypt1(debase64Bytes, keybBytes));
    }
}

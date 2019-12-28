package com.waylon.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @author wanglei
 */
public class ThreeDESUtil {
    /**
     * 定义 加密算法,可用 DES,DESede,Blowfish
     */
    private static final String Algorithm = "DESede";

    /**
     * 算法/模式/补码方式
     */
    public static final String ALGORITHM_DES = "DESede/CBC/PKCS5Padding";

    // 加解密统一使用的编码方式
    private final static String ENCODING = "utf-8";

    public static String encrypt(String data, String keystr, String iv) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes(ENCODING));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(data.getBytes());
        return Base64.encodeBase64String(encryptData);
    }

    public static String decrypt(String data, String keystr, String iv) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64.decodeBase64(data));
        return new String(decryptData, ENCODING);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ThreeDESUtil.encrypt("root", "DgXKJbkirHGpEnKdI8R78Vt1rgsU4=", "01234567"));
        System.out.println(ThreeDESUtil.decrypt("tp7tUM8sug4=", "DgXKJbkirHGpEnKdI8R78Vt1rgsU4", "01234567"));
    }
}
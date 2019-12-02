package com.rafa.dangan.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Administrator
 * @date 2019/05/13
 */
public class securityUtil {
  private static final String KEY_ALGORITHM = "AES";
  private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

  public static void main(String[] args) {
    System.out.println(encrypt("1658208161","rafaelhenry@1234"));
  }

  public static String decrypt(String content, String password) {
    try {
      //实例化
      Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

      //使用密钥初始化，设置为解密模式
      cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

      //执行操作
      byte[] result = cipher.doFinal(Base64.decodeBase64(content));

      return new String(result, "utf-8");
    } catch (Exception ex) {

    }

    return null;
  }

  public static String encrypt(String content, String password) {
    try {
      Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

      byte[] byteContent = content.getBytes("utf-8");

      cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

      byte[] result = cipher.doFinal(byteContent);// 加密

      return Base64.encodeBase64String(result);//通过Base64转码返回
    } catch (Exception ex) {

    }

    return null;
  }

  private static SecretKeySpec getSecretKey(final String password) {
    //返回生成指定算法密钥生成器的 KeyGenerator 对象
    KeyGenerator kg = null;

    try {
      kg = KeyGenerator.getInstance(KEY_ALGORITHM);

      //AES 要求密钥长度为 128
      kg.init(128, new SecureRandom(password.getBytes()));

      //生成一个密钥
      SecretKey secretKey = kg.generateKey();

      return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
    } catch (NoSuchAlgorithmException ex) {

    }

    return null;
  }

  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1)
    { return null;}
    byte[] result = new byte[hexStr.length()/2];
    for (int i = 0;i< hexStr.length()/2; i++) {
      int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
      int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }
}

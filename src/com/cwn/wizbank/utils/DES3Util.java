package com.cwn.wizbank.utils;


import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
public class DES3Util {
	
	private static final String keyStr = "ZOUZOUZOUZOUZOUZOUZOUZOUZOUZOUZO";
	
	/**
	* @Description:	DES3 解密   
	* @param key
	* @param src
	* @return String
	 */
    public static String decryptEde(String key, String src) // throws  
                                                            // DecryptException  
    {  
        String plainText = null;  
        try  
        {  
            byte[] keyBytes = new byte[24]; // DES3 为24Bytes密钥  
            asciiToBcdBytes(key, keyBytes, Math.min(32, key.length()) / 2);  
  
            for (int i = 0; i < 8; ++i)  
                keyBytes[16 + i] = keyBytes[i];  
  
            // 从原始密匙数据创建一个DESKeySpec对象  
            KeySpec dks = new DESedeKeySpec(keyBytes);  
  
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成  
            // 一个SecretKey对象  
            SecretKey secKey = SecretKeyFactory.getInstance("DESede").generateSecret(dks);  
  
            // Cipher对象实际完成解密操作  
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");  
  
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();  
  
            // 用密匙初始化Cipher对象  
            cipher.init(Cipher.DECRYPT_MODE, secKey, sr);  
  
            int count = (src.length() + 1) / 2;  
            byte[] inputBytes = new byte[count];  
            asciiToBcdBytes(src, inputBytes, count);  
  
            // 正式执行解密操作  
            byte[] decryptBytes = cipher.doFinal(inputBytes);  
  
            int olen = decryptBytes.length - 1;  
            for (; decryptBytes[olen] == 0 && olen >= 0; --olen)  
            {  
            }  
  
            plainText = new String(decryptBytes, 0, olen + 1);  
        }  
        catch (Exception e)  
        {  
            CommonLog.error(e.getMessage(),e);
        }  
  
        return plainText;  
    }  
    
    public static String decryptEde(String src) // throws  
    {
    	return decryptEde(keyStr, src);
    }
  
    private static void asciiToBcdBytes(String str, byte[] hex, int count)// throws  
                                                                            // Exception  
    {  
        byte[] inputBytes = str.getBytes();  
        for (int i = 0, j = 0; j < inputBytes.length && i < count; ++i)  
        {  
            byte v = inputBytes[j];  
            ++j;  
            if (v <= 0x39)  
                hex[i] = (byte) (v - 0x30);  
            else  
                hex[i] = (byte) (v - 0x41 + 10);  
  
            hex[i] <<= 4;  
  
            if (j >= inputBytes.length)  
                break;  
  
            v = inputBytes[j];  
            ++j;  
  
            if (v <= 0x39)  
                hex[i] += (byte) (v - 0x30);  
            else  
                hex[i] += (byte) (v - 0x41 + 10);  
        }  
    }  
  
    /**
    * @Description:	DES3 加密   
    * @param key
    * @param src
    * @return String
     */
    public static String encryptEde(String key, String src)// throws  
                                                            // DecryptException  
    {  
        String plainText = null;  
        try  
        {  
            byte[] keyBytes = new byte[24]; // DES3 为24Bytes密钥  
            asciiToBcdBytes(key, keyBytes, Math.min(32, key.length()) / 2);  
  
            for (int i = 0; i < 8; ++i)  
                keyBytes[16 + i] = keyBytes[i];  
  
            // 从原始密匙数据创建一个DESKeySpec对象  
            KeySpec dks = new DESedeKeySpec(keyBytes);  
  
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成  
            // 一个SecretKey对象  
            SecretKey secKey = SecretKeyFactory.getInstance("DESede").generateSecret(dks);  
  
            // Cipher对象实际完成解密操作  
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");  
  
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();  
  
            // 用密匙初始化Cipher对象  
            cipher.init(Cipher.ENCRYPT_MODE, secKey, sr);  
  
            byte[] srcBytes = src.getBytes("GBK");  
            int srcLen = srcBytes.length;  
            int encLen = ((srcLen % 8) == 0) ? srcLen : ((srcLen / 8 + 1) * 8);  
  
            byte[] encBytes = new byte[encLen];  
            System.arraycopy(srcBytes, 0, encBytes, 0, srcLen);  
  
            // 正式执行解密操作  
            byte[] encryptBytes = cipher.doFinal(encBytes);  
            plainText = bcdBytesToAscii(encryptBytes, encLen);  
        }  
        catch (Exception e)  
        {  
            CommonLog.error(e.getMessage(),e);
        }  
  
        return plainText;  
    }  
    
    public static String encryptEde(String src){
    	return encryptEde(keyStr, src);
    }
  
    private static String bcdBytesToAscii(byte[] hex, int count)// throws  
                                                                // Exception  
    {  
        byte[] ascii = new byte[2 * count];  
        int t;  
        for (int i = 0; i < count; i++)  
        {  
            t = hex[i] & 0xf0;  
            t = t >> 4;  
            if (t <= 9)  
                t += '0';  
            else if (t >= 10 && t <= 15)  
                t += 'A' - 10;  
            else  
                t = '0';  
            ascii[2 * i] = (byte) t;  
  
            t = hex[i] & 0x0f;  
            if (t <= 9)  
                t += '0';  
            else if (t >= 10 && t <= 15)  
                t += 'A' - 10;  
            else  
                t = '0';  
            ascii[2 * i + 1] = (byte) t;  
        }  
  
        return (new String(ascii));  
    }

}

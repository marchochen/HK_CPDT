package com.cw.wizbank.message;
 
import java.io.*;
import java.security.*;
import javax.crypto.*;
import sun.misc.*;
import com.cw.wizbank.util.*;

public class MessageCryptography  {

    //private static long encryptionKey = 1;
    
    private Key key = null;
    private Cipher cipher = null;
    
    public MessageCryptography(long encryptionKey)
        throws cwException {
            try{
                
                //Generate a DES KEY
                Security.addProvider(new com.sun.crypto.provider.SunJCE());
                KeyGenerator generator= KeyGenerator.getInstance("DES");
                SecureRandom srn = new SecureRandom();
                srn.setSeed(encryptionKey);
                generator.init(srn);
                key = generator.generateKey();
                
                //Generates a Cipher object that implements DES transformation
                cipher = Cipher.getInstance("DES");
                return;
                
            }catch(NoSuchPaddingException pe) {
                throw new cwException("Failed to init MessageCryptography : " + pe.getMessage());
            }catch(NoSuchAlgorithmException ae) {
                throw new cwException("Failed to init MessageCryptography : " + ae.getMessage());
            }
        }
    
    public String encrypt(String str) 
        throws cwException {
        
            try{
                cipher.init(Cipher.ENCRYPT_MODE,key);
                byte[] stringBytes = str.getBytes();
                byte[] raw = cipher.doFinal(stringBytes);
                //return new String(raw);
                BASE64Encoder encoder = new BASE64Encoder();                
                return new String(encoder.encode(raw));
                
            }catch(InvalidKeyException ke){
                throw new cwException("Failed to encrypt string : " + ke.getMessage());
            }catch(IllegalBlockSizeException se){
                throw new cwException("Failed to encrypt string : " + se.getMessage());
            }catch(BadPaddingException pe){
                throw new cwException("Failed to encrypt string : " + pe.getMessage());
            }
    }
    
    public String decrypt(String str) 
        throws cwException {
            try{    
                
                cipher.init(Cipher.DECRYPT_MODE,key);
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] raw = decoder.decodeBuffer(str);
                byte[] stringBytes = cipher.doFinal(raw);
                return new String(stringBytes);
                
            }catch(BadPaddingException pe){
                throw new cwException("Failed to encrypt string : " + pe.getMessage());
            }catch(IllegalBlockSizeException se){
                throw new cwException("Failed to encrypt string : " + se.getMessage());
            }catch(InvalidKeyException ke){
                throw new cwException("Failed to encrypt string : " + ke.getMessage());
            }catch(IOException ioe){
                throw new cwException("Failed to encrypt string : " + ioe.getMessage());
            }
    }

}
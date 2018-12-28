package com.cw.wizbank.batch.encrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.sql.Timestamp;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import com.cw.wizbank.util.cwEncode;
import com.cwn.wizbank.utils.CommonLog;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptUtil {
    public static final String key = "cyber";
    public static final String ProviderSunJCE = "com.sun.crypto.provider.SunJCE";
    public static final String ProviderSUN = "sun.security.provider.Sun";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    if(args.length > 0 && args[0].trim().equalsIgnoreCase("DB_SET")){
	        System.out.println("Please enter db user name:");
	        BufferedReader bfRead = new BufferedReader(new InputStreamReader(System.in));
	        try {
	            String strName = bfRead.readLine();
	            System.out.println("Please enter db user password:");
	            
	            String strPwd = bfRead.readLine();
	            StringBuffer key_name = new StringBuffer(key);
	            StringBuffer key_pwd = new StringBuffer(key);

	            System.out.println("The encrypted user name is:"+encrypt(strName,key_name.reverse().toString()));

	            System.out.println("The encrypted password is:"+encrypt(strPwd,key_pwd.reverse().toString()));
	           
	            System.out.println("press any key to stop.");
	            System.in.read();
	        } catch (IOException e) {
	            CommonLog.error(e.getMessage(),e);
	        } catch (Exception e) {
	            CommonLog.error(e.getMessage(),e);
	        }
	        
	    }else if(args.length > 0 && args[0].trim().equalsIgnoreCase("LOGIN_DATE_LIMIT")){
	        boolean tt= true;
	      
            try {
                Timestamp start_date =  null;
                String date = "";
                while(tt){
                    try{
                        System.out.println("Login_Date_Limit: format: YYYY-MM-DD HH:mm:ss");
                        System.out.println("Please enter Login Date Limit:");
                        BufferedReader bfRead = new BufferedReader(new InputStreamReader(System.in));
                        date = bfRead.readLine();
                        start_date = Timestamp.valueOf(date);
                        tt = false;
                    }catch (Exception e) {
                        System.out.println("The date format is error, please enter again!");
                    }
                }
                System.out.println("The encrypted Login_Date_limit is:");
                System.out.println(cwEncode.encodeKey(date));
                
                System.out.println("press any key to stop.");
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                CommonLog.error(e.getMessage(),e);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                CommonLog.error(e.getMessage(),e);
            }
	    
	    }
	    else if(args.length > 0 && args[0].trim().equalsIgnoreCase("MAX_ACCOUNT")){
	        boolean tt= true;
	          
            try {
                int k =  0;
                String count = "";
                while(tt){
                    try{
                        System.out.println("Please enter max user count(Please enter integer):");
                        BufferedReader bfRead = new BufferedReader(new InputStreamReader(System.in));
                        count = bfRead.readLine();
                        k = java.lang.Integer.valueOf(count);
                        tt = false;
                    }catch (Exception e) {
                        System.out.println("Format is error, Please enter integer again!");
                    }
                }
                System.out.println("The encrypted max user count is:");
                System.out.println(cwEncode.encodeKey(count));
                
                System.out.println("press any key to stop.");
                System.in.read();
        
            } catch (IOException e) {
                CommonLog.error(e.getMessage(),e);
            } catch (Exception e) {
                CommonLog.error(e.getMessage(),e);
            }
	    }
	    else{
	        System.out.println("Please enter user name:");
	        BufferedReader bfRead = new BufferedReader(new InputStreamReader(System.in));
	        try {
	            String strName = bfRead.readLine();
	            System.out.println("Please enter user password:");
	            String strPwd = bfRead.readLine();
	            StringBuffer str = new StringBuffer(strName);
	            System.out.println("The encrypted password is:"+encrypt(strPwd,str.reverse().toString()));
	            
	            System.out.println("press any key to stop.");
	            System.in.read();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            CommonLog.error(e.getMessage(),e);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            CommonLog.error(e.getMessage(),e);
	        }
	    }
		

	}
	
	
    
	public static String encrypt(String str, String encryptionKey) 
        throws Exception {
            try{
                Key key = null;
                Cipher cipher = null;
    
                //Generate a DES KEY
                Security.addProvider((Provider)Class.forName(ProviderSunJCE).newInstance());
                Security.addProvider((Provider)Class.forName(ProviderSUN).newInstance());
                KeyGenerator generator= KeyGenerator.getInstance("DES");
                SecureRandom srn = SecureRandom.getInstance("SHA1PRNG", "SUN");
                srn.setSeed(encryptionKey.getBytes());
                generator.init(srn);
                key = generator.generateKey();
                
			    byte[] iv = new byte[8];
			    srn.nextBytes(iv);
			    IvParameterSpec params = new IvParameterSpec(iv, 0, 8);
    			
			    //Generates a Cipher object that implements DES transformation
			    cipher = Cipher.getInstance("DES/OFB16/NoPadding");
			    cipher.init(Cipher.ENCRYPT_MODE,key, params);
			    byte[] stringBytes = str.getBytes();
			    byte[] raw = cipher.doFinal(stringBytes);

			    BASE64Encoder encoder = new BASE64Encoder();                
			    return new String(encoder.encode(raw));
			} catch (ClassNotFoundException e) {
			    throw new Exception("Failed to init encryption provider(ClassNotFoundException)" + e.getMessage());
			} catch (InstantiationException e) {
			    throw new Exception("Failed to init encryption provider(InstantiationException)" + e.getMessage());
			} catch (IllegalAccessException e) {
			    throw new Exception("Failed to init encryption provider(IllegalAccessException)" + e.getMessage());
			} catch (NoSuchProviderException e) {
			    throw new Exception("Failed to init encryption provider(NoSuchProviderException)" + e.getMessage());
            }catch(NoSuchPaddingException pe) {
                throw new Exception("Failed to init encrypt : " + pe.getMessage());
            }catch(NoSuchAlgorithmException ae) {
                throw new Exception("Failed to init encrypt : " + ae.getMessage());
            }catch(InvalidKeyException ke){
                throw new Exception("Failed to encrypt string : " + ke.getMessage());
            }catch(IllegalBlockSizeException se){
                throw new Exception("Failed to encrypt string : " + se.getMessage());
            }catch(BadPaddingException pe){
                throw new Exception("Failed to encrypt string : " + pe.getMessage());
            }catch(InvalidAlgorithmParameterException iape){
                throw new Exception("Failed to encrypt string : " + iape.getMessage());
            }
        }
        
	/**
	 * David Qiu Weiping   updated 31th,May
	 * @param str  the supplied encrypted password
	 * @param encryptionKey 
	 * @return the Decrypted password
	 * @throws Exception if there occured such Exception
	 * @throws IOException if there occured such Exception
	 */
	public static String decrypt(String str, String encryptionKey)
		throws Exception,IOException {
		try {
			Key key = null;
			Cipher cipher = null;

			//Generate a DES KEY
			Security.addProvider(
				(Provider) Class.forName(ProviderSunJCE).newInstance());
			Security.addProvider(
				(Provider) Class.forName(ProviderSUN).newInstance());
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom srn = SecureRandom.getInstance("SHA1PRNG", "SUN");
			srn.setSeed(encryptionKey.getBytes());
			generator.init(srn);
			key = generator.generateKey();

			byte[] iv = new byte[8];
			srn.nextBytes(iv);
			IvParameterSpec params = new IvParameterSpec(iv, 0, 8);
			cipher = Cipher.getInstance("DES/OFB16/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, params);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] raw = cipher.doFinal(decoder.decodeBuffer(str));
			String result = new String(raw, "ISO-8859-1");
			//return the decrypted password
			return result; 
		} catch (ClassNotFoundException e) {
			throw new Exception(
				"Failed to init encryption provider(ClassNotFoundException)"
					+ e.getMessage());
		} catch (InstantiationException e) {
			throw new Exception(
				"Failed to init encryption provider(InstantiationException)"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(
				"Failed to init encryption provider(IllegalAccessException)"
					+ e.getMessage());
		} catch (NoSuchProviderException e) {
			throw new Exception(
				"Failed to init encryption provider(NoSuchProviderException)"
					+ e.getMessage());
		} catch (NoSuchPaddingException pe) {
			throw new Exception(
				"Failed to init encrypt : " + pe.getMessage());
		} catch (NoSuchAlgorithmException ae) {
			throw new Exception(
				"Failed to init encrypt : " + ae.getMessage());
		} catch (InvalidKeyException ke) {
			throw new Exception(
				"Failed to encrypt string : " + ke.getMessage());
		} catch (IllegalBlockSizeException se) {
			throw new Exception(
				"Failed to encrypt string : " + se.getMessage());
		} catch (BadPaddingException pe) {
			throw new Exception(
				"Failed to encrypt string : " + pe.getMessage());
		} catch (InvalidAlgorithmParameterException iape) {
			throw new Exception(
				"Failed to encrypt string : " + iape.getMessage());
		}
	}  	

}

package com.cwn.wizbank.utils;

import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.exception.EncryptException;
import com.cwn.wizbank.exception.UploadException;

public class EncryptUtil {

	  public static final long CWN_ENC_KEY_1 = 157862245; 
	  public static final long CWN_ENC_KEY_2 = 32211; 
	  public static final String CWN_ENC_SEPARATOR = "_"; 
	  
	  public static String cwnEncrypt(long id){
		  String enc_id = (id + CWN_ENC_KEY_1) + CWN_ENC_SEPARATOR + (CWN_ENC_KEY_1 - id - CWN_ENC_KEY_2);
		  return enc_id;
	  }

	  public static long cwnDecrypt(String enc_id) throws  EncryptException {
		long decr_id = 0;
		try{
			long ids[] = cwUtils.splitToLong(enc_id, CWN_ENC_SEPARATOR);
			if (ids != null && ids.length == 2) {
				//如果传进过来数据符合加密码的规则，则对数据进行解密
				long key_1 = ids[0];
				long key_2 = ids[1];
			    
				key_1 = key_1 - CWN_ENC_KEY_1;
				long temp_key = key_2 + key_1 + CWN_ENC_KEY_2;
				if (temp_key == CWN_ENC_KEY_1) {
					//解决成功
					decr_id = key_1;
				}
			}else{
				//如果传进过来数据不符合加密码的规则，直接转成 long型数据。 如果开时所有都加密码的，这段代码可以去掉
				// 这里保留这样的逻辑是为了处理那些在URL中的ID是没有被加密过的情况，也是为了处理一些页面上的特殊情况，或是这次个修改时，可能中漏失一些没改到，
				decr_id =  Long.parseLong(enc_id.trim());
			}
		}catch (Exception e){
			throw new EncryptException("error_data_not_found");
			//如果传进过来数据不符合加密码的规则，且不能直接转为long型数据， 则表示数已补改过，解密码不成功
		}
		if(decr_id <  0){
			throw new EncryptException("error_data_not_found");
			//在这里抛出找不到数据的错误。
		}
       return decr_id;
	}
	
}

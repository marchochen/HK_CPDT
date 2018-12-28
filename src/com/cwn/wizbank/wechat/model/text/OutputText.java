package com.cwn.wizbank.wechat.model.text;

import com.cwn.wizbank.wechat.common.Static;
import com.cwn.wizbank.wechat.model.Base;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ΢���ı���Ϣ��
 * @author Administrator
 */
@XStreamAlias("xml")
public class OutputText extends Base{
	@XStreamAlias("Content")
	private String content = Static.CHAR_Blank;//�ظ�����Ϣ�ڳ��Ȳ���1048�ֽ�
	
	@XStreamAlias("FuncFlag")
	private String funcFlag = "0";//x0001����־ʱ���Ǳ���յ�����xi
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFuncFlag() {
		return funcFlag;
	}
	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}	
}

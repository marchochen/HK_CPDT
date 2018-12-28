package com.cw.wizbank.JsonMod.exam;

import java.util.Vector;

import com.cw.wizbank.util.cwUtils;

public class ExamPassport {
	private String usr_display_bil;
	private String usg_display_bil;
	private long usr_ent_id;
	private long test_itm_id;
	private Vector messages;
	public String terminate_msg;
	public boolean terminate_as_zero;
	public boolean isPause;
	public String pause_msg;
	public boolean isTerminate;
	public boolean isLogin;
	
	
	public ExamPassport() {
		messages = new Vector();
		isLogin = true;
	}
	
	class ExamMessage {
		String messageBody;
		private boolean isTerminate;
		private boolean isZero;
		private boolean isPause;
		
		public ExamMessage() {}
		
		public ExamMessage(String messageBody, boolean isPause, boolean isTerminate, boolean isZero) {
			this.messageBody = messageBody;
			this.isPause = isPause;
			this.isTerminate = isTerminate;
			this.isZero = isZero;
		}
		
		public boolean getIsPause() {
			return isPause;
		}
		public void setIsPause(boolean isPause) {
			this.isPause = isPause;
		}
		
		public boolean getIsZero() {
			return isZero;
		}
		public void setIsZero(boolean isZero) {
			this.isZero = isZero;
		}
		
		public String getMessageBody() {
			return messageBody;
		}
		public void setMessageBody(String messageBody) {
			this.messageBody = messageBody;
		}
		
		public boolean getIsTerminate() {
			return isTerminate;
		}
		public void setIsTerminate(boolean isTerminate) {
			this.isTerminate = isTerminate;
		}
	}
	
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	
	public String getUsg_display_bil() {
		return usg_display_bil;
	}
	
	public void setUsg_display_bil(String usg_display_bil) {
		this.usg_display_bil = usg_display_bil;
	}
	
	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}

	public long getTest_itm_id() {
		return test_itm_id;
	}

	public void setTest_itm_id(long test_itm_id) {
		this.test_itm_id = test_itm_id;
	}

	public String getExamStatus() {
		StringBuffer result = new StringBuffer();
		result.append("<exam_status>");
		if (messages.size() > 0) {
			ExamMessage msg = null;
			for (int i = 0; i < messages.size(); i++) {
				msg = (ExamMessage)messages.get(i);
				result.append(getExamStatusAsXml(msg.getIsPause(), msg.getIsTerminate(), msg.getIsZero(), msg.getMessageBody()));
				if (msg.getIsTerminate()) {
					//交卷了，以后的消息就不需要处理了
					messages.removeAllElements();
					break;
				}
				messages.remove(i--);
				if (msg.getIsPause()) {
                    //以后的消息等解除暂停再接收	
					break;
				}
			}
		}
		result.append("</exam_status>");
		return result.toString();
	}
	
	private String getExamStatusAsXml(boolean isPause, boolean isTerminate, boolean isZero, String content) {
		String result = "<exam_msg>"
					  + "<isPause>" + isPause + "</isPause>"
					  + "<terminate_ind>" + isTerminate + "</terminate_ind>"
					  + "<zero_ind>" + isZero + "</zero_ind>"
					  + "<content>" + cwUtils.esc4XML(cwUtils.escNull(content)) + "</content>"
					  + "</exam_msg>";
		return result;
	}

	public void addMessage(String message, boolean isPause, boolean terminate, boolean markAsZero) {
		this.messages.add(new ExamMessage(message, isPause, terminate, markAsZero));
	}
	
	public boolean checkIfTerminate() {
		boolean is_terminate = false;
		if(this.messages != null && this.messages.size() > 0) {
			for(int i = 0; i < messages.size(); i++) {
				ExamMessage msg = (ExamMessage)messages.get(i);
				if(msg.getIsTerminate()) {
					is_terminate = true;
					this.isTerminate = true;
					this.terminate_msg = msg.getMessageBody();
					this.terminate_as_zero = msg.getIsZero();
					break;
				}
			}
		}
		return is_terminate;
	}
}

package com.cw.wizbank.content;

import com.cw.wizbank.JsonMod.BaseParam;

public class ContentQueImpReqParam extends BaseParam{
		private long que_obj_id;
		private String que_type;
		private String mod_type;
		public long getQue_obj_id() {
			return que_obj_id;
		}
		public void setQue_obj_id(long que_obj_id) {
			this.que_obj_id = que_obj_id;
		}
		public String getQue_type() {
			return que_type;
		}
		public void setQue_type(String que_type) {
			this.que_type = que_type;
		}
		public String getMod_type() {
			return mod_type;
		}
		public void setMod_type(String mod_type) {
			this.mod_type = mod_type;
		}
	
}

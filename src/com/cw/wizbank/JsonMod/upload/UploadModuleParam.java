package com.cw.wizbank.JsonMod.upload;

import com.cw.wizbank.JsonMod.BaseParam;

public class UploadModuleParam extends BaseParam {
    String sysmsg_id;
    long cos_id;
    long new_res_id;
    public String getSysmsg_id() {
        return sysmsg_id;
    }

    public void setSysmsg_id(String sysmsgId) {
        sysmsg_id = sysmsgId;
    }

    public long getCos_id() {
        return cos_id;
    }

    public void setCos_id(long cosId) {
        cos_id = cosId;
    }

    public long getNew_res_id() {
        return new_res_id;
    }

    public void setNew_res_id(long newResId) {
        new_res_id = newResId;
    }
}

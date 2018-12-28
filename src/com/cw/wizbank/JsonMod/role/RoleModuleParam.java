package com.cw.wizbank.JsonMod.role;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class RoleModuleParam extends BaseParam {
    long rol_id;
    String rol_ext_id;
    String rol_title;
    boolean rol_tc_ind;
    int ent_id;
    long[] rol_id_lst;
    String ftn_id_lst;
    public long getRol_id() {
        return rol_id;
    }
    public void setRol_id(long rolId) {
        rol_id = rolId;
    }
    public String getRol_ext_id() {
        return rol_ext_id;
    }
    public void setRol_ext_id(String rolExtId) {
        rol_ext_id = rolExtId;
    }
    public String getRol_title() {
        return rol_title;
    }
    public void setRol_title(String rolTitle) throws cwException {
         rol_title = cwUtils.unicodeFrom(rolTitle, clientEnc, encoding,isBMultiPart());
    }
    public boolean getRol_tc_ind() {
        return rol_tc_ind;
    }
    public void setRol_tc_ind(boolean rolTcInd) {
        rol_tc_ind = rolTcInd;
    }
    public long[] getRol_id_lst() {
        return rol_id_lst;
    }
    public void setRol_id_lst(long[] rolIdLst) {
        rol_id_lst = rolIdLst;
    }
    public String getFtn_id_lst() {
        return ftn_id_lst;
    }
    public void setFtn_id_lst(String ftnIdLst) {
        ftn_id_lst = ftnIdLst;
    }
    public int getEnt_id() {
        return ent_id;
    }
    public void setEnt_id(int entId) {
        ent_id = entId;
    }

}

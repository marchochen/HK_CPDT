package com.cw.wizbank.cf;

import java.sql.SQLException;

public interface CFCertificateLink {

    /**
     * @param ent_id
     * @return boolean
     */
    public boolean isQualified(int ctf_id, int usr_ent_id) throws SQLException ;
}

package com.cw.wizbank.codetable;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
//import com.cw.wizbank.db.DbEntityDefinition;

public class CodeDataModule extends ServletModule {
    
	public final static String CODE_DATA_TYPE_LST = "code_data_type_lst";
	public final static String UPD_CODE_DATA_EXEC = "upd_code_data_exec";
	public final static String INS_CODE_DATA_EXEC = "ins_code_data_exec";
	public final static String INS_CODE_DATA_PRE = "ins_code_data_pre";
	public final static String GET_CODE_DATA_DETAIL ="get_code_data_detail";
	public final static String GET_CODE_DATA_LST = "get_code_data_lst";
	public final static String INS_CTB = "ins_ctb";
    public final static String DEL_CODE_DATA = "del_code_data";
    public final static String UPD_CTB = "upd_ctb";
    public final static String PREP_INS_CTB = "prep_ins_ctb";
    public final static String GET_CTB = "get_ctb";
    public final static String LOOKUP_CTB = "lookup_ctb";
    public final static String GET_CODETYPE = "get_codetype";
    public final static String XML = "_xml";       
    private final static String CODE_DATA_MODULE = "code_data_module";
	private final static String SMSG_DEL_MSG = "GEN002";
	private final static String SMSG_UPD_MSG = "GEN003";
	private final static String SMSG_INS_MSG = "GEN004";
	private final static String SMSG_UPDED_MSG = "GEN006";
    
    public CodeDataModule() { ; }
    
    public void process()
        throws SQLException, IOException, cwException {

//        String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

        if (prof == null) {
            response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//            response.sendRedirect(url_relogin);        
        }
        
        PrintWriter out = response.getWriter();
        CodeTable ctb = new CodeTable();
        CodeTableReqParam urlp = new CodeTableReqParam(request, clientEnc, static_env.ENCODING);
		urlp.myCommon();
        urlp.codeTable(clientEnc,static_env.ENCODING);      
        HttpSession sess = request.getSession(true);
        try {
            if(urlp.cmd == null) {
                 
                throw new cwException("Invalid Command");
            }
            else if(urlp.cmd.equalsIgnoreCase(INS_CTB)) {
                urlp.codeTable(clientEnc, static_env.ENCODING);
                //no access control
                ctb.ins(con, prof.usr_id,urlp.ctb_type,urlp.ctb_id,urlp.ctb_title);
                con.commit();
                response.sendRedirect(urlp.url_success);
            }
            else if(urlp.cmd.equalsIgnoreCase(DEL_CODE_DATA)) {
                /*urlp.codeTable(clientEnc, static_env.ENCODING);
                    
                //no access control
                
                if(urlp.ctb_id_lst != null) {
                    for(int i=0; i<urlp.ctb_id_lst.length; i++) {
                        urlp.ctb.ctb_id = urlp.ctb_id_lst[i];
                        urlp.ctb.del(con, urlp.ctb_upd_timestamp_lst[i]);
                    }
                    con.commit();
                }*/
				ctb.del(con,urlp.ctb_type,urlp.ctb_id);
				cwSysMessage sms = new cwSysMessage(SMSG_DEL_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
            }
            /*else if(urlp.cmd.equalsIgnoreCase(UPD_CTB)) {
                urlp.codeTable(clientEnc, static_env.ENCODING);
                    
                //no access control
                urlp.ctb.upd(con, prof.usr_id, urlp.ctb_in_upd_timestamp);
                con.commit();
                response.sendRedirect(urlp.url_success);
            }*/
            else if(urlp.cmd.equalsIgnoreCase(PREP_INS_CTB) 
                    || urlp.cmd.equalsIgnoreCase(PREP_INS_CTB + XML)) {
                urlp.codeTable(clientEnc, static_env.ENCODING);
                StringBuffer xmlBuf = new StringBuffer(2500);
                    
                //no access control
                    
                xmlBuf.append("<codes>").append(cwUtils.NEWL);
                xmlBuf.append("</codes>").append(cwUtils.NEWL);
                StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
                if(urlp.cmd.equalsIgnoreCase(PREP_INS_CTB + XML)) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }                                                                                                
            }
            else if(urlp.cmd.equalsIgnoreCase(GET_CTB) 
                    || urlp.cmd.equalsIgnoreCase(GET_CTB + XML)) {
                urlp.codeTable(clientEnc, static_env.ENCODING);
                    
                //get access control
                    
                ctb.get(con);
                StringBuffer xmlBuf = new StringBuffer(2500);
                xmlBuf.append("<codes>").append(cwUtils.NEWL);
                xmlBuf.append(ctb.asXML(con, true)).append(cwUtils.NEWL);
                xmlBuf.append("</codes>").append(cwUtils.NEWL);
                StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
                if(urlp.cmd.equalsIgnoreCase(GET_CTB + XML)) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }                                                                                                
            }
            else if(urlp.cmd.equalsIgnoreCase(LOOKUP_CTB) 
                    || urlp.cmd.equalsIgnoreCase(LOOKUP_CTB + XML)) {
                urlp.codeTable(clientEnc, static_env.ENCODING);
                    
                //no access control
                    
                StringBuffer xmlBuf = new StringBuffer(2500);
                xmlBuf.append(CodeTable.lookUp(con,sess,urlp.ctb_type,urlp.ctb_id,urlp.ctb_title,urlp.orderBy,urlp.sortOrder,urlp.exact, urlp.page, urlp.search_timestamp, true)).append(cwUtils.NEWL);
                StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
                if(urlp.cmd.equalsIgnoreCase(LOOKUP_CTB + XML)) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }                                                                                                
            }
            // get all code types available in the system (2001.07.27 wai)
            //Vincent's code
            else if(urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_LST) 
                    || urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_LST + XML)) {
                StringBuffer xmlBuf = new StringBuffer(500);
                String code_data_type;
                xmlBuf.append(CodeTable.getCodeDataListAsXML(con,urlp.ctb_type));
                StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
                if(urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_LST + XML)) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
            }
			else if(urlp.cmd.equalsIgnoreCase(CODE_DATA_TYPE_LST) 
					|| urlp.cmd.equalsIgnoreCase(CODE_DATA_TYPE_LST + XML)) {
				StringBuffer xmlBuf = new StringBuffer(500);

				//no access control
				xmlBuf.append(CodeTable.getTypeListAsXML(con));
				StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
				if(urlp.cmd.equalsIgnoreCase(CODE_DATA_TYPE_LST + XML)) {
					out.println(result.toString());
				} else {
					generalAsHtml(result.toString(), out, urlp.stylesheet);
				}
			}
			else if(urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_DETAIL) 
					|| urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_DETAIL + XML)) {
				StringBuffer xmlBuf = new StringBuffer(500);
				xmlBuf.append(CodeTable.getTypeListAsXML(con));
				xmlBuf.append(CodeTable.getCodeDataDetailAsXML(con,urlp.ctb_id,urlp.ctb_type));
				StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
				if(urlp.cmd.equalsIgnoreCase(GET_CODE_DATA_DETAIL+ XML)) {
					out.println(result.toString());
				} else {
					generalAsHtml(result.toString(), out, urlp.stylesheet);
				}
			}	
			else if(urlp.cmd.equalsIgnoreCase(INS_CODE_DATA_PRE) 
					|| urlp.cmd.equalsIgnoreCase(INS_CODE_DATA_PRE + XML)) {
				StringBuffer xmlBuf = new StringBuffer(500);
				xmlBuf.append(CodeTable.insCodeDataAsXML(con,urlp.ctb_type));
				StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),CODE_DATA_MODULE));
				if(urlp.cmd.equalsIgnoreCase(INS_CODE_DATA_PRE+ XML)) {
					out.println(result.toString());
				} else {
					generalAsHtml(result.toString(), out, urlp.stylesheet);
				}
			}
			else if (urlp.cmd.equalsIgnoreCase(INS_CODE_DATA_EXEC) ||
			urlp.cmd.equalsIgnoreCase(INS_CODE_DATA_EXEC+XML)) {
				ctb.ins(con,prof.usr_id,urlp.ctb_type,urlp.ctb_id,urlp.ctb_title);
				cwSysMessage sms = new cwSysMessage(SMSG_INS_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);													
			}
			else if (urlp.cmd.equalsIgnoreCase(UPD_CODE_DATA_EXEC) ||
			urlp.cmd.equalsIgnoreCase(UPD_CODE_DATA_EXEC+XML)) {
				boolean isTypeM = true,isIdM = true; //check ctb_id status
				if (urlp.ctb_type.equalsIgnoreCase(urlp.ctb_type_bef))
					isTypeM = false;
				if (urlp.ctb_id.equalsIgnoreCase(urlp.ctb_id_bef))
					isIdM = false;
				ctb.upd(con,prof.usr_id,urlp.ctb_id,urlp.ctb_id_bef,urlp.ctb_type,urlp.ctb_type_bef,urlp.ctb_title,isTypeM,isIdM,urlp.upd_timestamp);
				cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);													
			}				
            //Vincent's code		
        }
        catch (cwSysMessage se) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        }
    }
}
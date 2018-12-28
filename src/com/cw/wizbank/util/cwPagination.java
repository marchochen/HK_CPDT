package com.cw.wizbank.util;

import java.io.Serializable;
import java.sql.Timestamp;

public class cwPagination implements Serializable {
    public static final String ValueSortAsc  = "ASC";
    public static final String ValueSortDesc = "DESC";
    
    private static final String NameTag       = "pagination";
    private static final String NameTotalRec  = "total_rec";
    private static final String NameTotalPage = "total_page";
    private static final String NamePageSize  = "page_size";
    private static final String NameCurPage   = "cur_page";
    private static final String NameSortCol   = "sort_col";
	private static final String NameSortCol1   = "sort_col1";
    private static final String NameSortOrder = "sort_order";
    private static final String NameTimeStamp = "timestamp";
    
    private static final String TagOpen       = "<";
    private static final String TagClose      = ">";
    private static final String TagComplete   = "/";
    private static final String TagAttOpen    = "=\"";
    private static final String TagAttClose   = "\"";
    private static final String TagSpc        = " ";
    
    public static final int defaultPageSize = 10;

    public int totalRec;
    public int totalPage;
    public int pageSize;
    public int curPage;
    public int sortColNo;
    public int startRec;
    public int endRec;
    public String sortCol;
	public String sortCol1;
    public String sortOrder;
    public Timestamp ts;
    
    public cwPagination () {
        this.totalRec  = 0;
        this.totalPage = 0;
        this.pageSize  = 0;
        this.curPage   = 0;
       this.sortColNo = 0;
        this.sortCol   = null;
       this.sortCol1   = null;
        this.sortOrder = null;
        this.ts        = null;
    }
    
    public StringBuffer asXML() {
        StringBuffer result = new StringBuffer();
        result.append(TagOpen).append(NameTag)
              .append(TagSpc).append(NameTotalRec).append(TagAttOpen).append(this.totalRec).append(TagAttClose)
              .append(TagSpc).append(NameTotalPage).append(TagAttOpen).append(this.totalPage).append(TagAttClose)
              .append(TagSpc).append(NamePageSize).append(TagAttOpen).append(this.pageSize).append(TagAttClose)
              .append(TagSpc).append(NameCurPage).append(TagAttOpen).append(this.curPage).append(TagAttClose)
              .append(TagSpc).append(NameSortCol).append(TagAttOpen).append(this.sortCol).append(TagAttClose)
      .append(TagSpc).append(NameSortCol1).append(TagAttOpen).append(this.sortCol1).append(TagAttClose)
              .append(TagSpc).append(NameSortOrder).append(TagAttOpen).append(this.sortOrder).append(TagAttClose)
              .append(TagSpc).append(NameTimeStamp).append(TagAttOpen).append(this.ts).append(TagAttClose)
              .append(TagComplete).append(TagClose);
        return result;
    }
    public void setTotalRec(int total_record){
        this.totalRec = total_record;
        setTotalPage();
    }
    
    public void setTotalPage(){
        if(this.pageSize!=0){
            this.totalPage = (int)Math.ceil((float)this.totalRec/this.pageSize);
        }
        else this.totalPage=0;
    }
    
    public void setRec(){
        this.startRec = (this.curPage - 1) * this.pageSize;
        this.endRec = this.curPage * this.pageSize;
    }
    
    
    public static final char tab = '\t';
    // line delimiters used in the input file (only valid for MSDOS)
    public static final char lineDelimiter1_ = '\r';
    public static final char lineDelimiter2_ = '\n';
    public static String esc4SortSql(String var){
    	if(var == null || var.trim().length() < 1){
    		return var;
    	}
    	var = var.replaceAll(lineDelimiter1_+"", "");
    	var = var.replaceAll(lineDelimiter2_+"", "");
    	var = var.replaceAll(tab+"", "");
    	var = var.replaceAll(" ", "");
    	var = var.replaceAll(";", "");
    	var = var.replaceAll("'", "");
  
    	//头尾补上空格，预防拼SQL时没加上空格
        return "" + var + "";
    }

}
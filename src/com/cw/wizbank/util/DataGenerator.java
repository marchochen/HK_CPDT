package com.cw.wizbank.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cw.wizbank.config.dataloader.ColumnType;
import com.cw.wizbank.config.dataloader.DataLoader;
import com.cw.wizbank.config.dataloader.ParamType;
import com.cw.wizbank.config.dataloader.PartType;
import com.cw.wizbank.config.dataloader.TableGroupType;
import com.cw.wizbank.config.dataloader.TableType;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cwn.wizbank.utils.CommonLog;

public class DataGenerator {
    private String configRoot;
    private String resultFileRoot;
    private boolean isUnicodeFile;
    private DataLoader cfgDataLoader;
    private File fileRoot;
    
    private final String cfgFileData = "data_loader.xml";
    private final String allSchemaContext = "com.cw.wizbank.config.dataloader";
    private final String spliter = "\t";
    private final String varible_ind = "$";
    
    private Hashtable params = null;
    private Hashtable columns = null;
    
	public static void main(String[] args){
		try {
		    Date s_date = new Date(System.currentTimeMillis());
			System.out.println("start...\t\t" + s_date.toString());
			DataGenerator dLoader = new DataGenerator();
			dLoader.configRoot = args[0];
			dLoader.resultFileRoot = args[1];
			dLoader.isUnicodeFile = (Boolean.valueOf(args[2])).booleanValue();
			dLoader.loadCongfigXml();
			dLoader.genDataFile();
			Date e_date = new Date(System.currentTimeMillis());
			System.out.println("success!\t\t" + e_date.toString());
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
			System.out.println("IOException!\t\t" + e.getMessage());
		} catch (JAXBException e) {
			CommonLog.error(e.getMessage(),e);
			System.out.println("JAXBException!\t\t" + e.getMessage());
		} catch (cwException e) {
			CommonLog.error(e.getMessage(),e);
			System.out.println("cwException!\t\t" + e.getMessage());
		} catch (qdbException e) {
			CommonLog.error(e.getMessage(),e);
			System.out.println("qdbException!\t\t" + e.getMessage());
		}
	}

    private void loadCongfigXml() throws JAXBException{
        if (System.getProperty("javax.xml.parsers.SAXParserFactory") != null) {
            System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        }
        if (System.getProperty("javax.xml.parsers.DocumentBuilderFactory") != null) {
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        }
        File docRootCfg = new File(this.configRoot);
        JAXBContext jc = JAXBContext.newInstance(this.allSchemaContext, this.getClass().getClassLoader());
        Unmarshaller unmar = jc.createUnmarshaller();
        unmar.setValidating(true);
        this.cfgDataLoader = (DataLoader)unmar.unmarshal(new File(docRootCfg, this.cfgFileData));
    }
    
    private void getParamHash() {
    	List param = this.cfgDataLoader.getParam();
		if (param != null) {
			this.params = new Hashtable();
			for (int i = 0; i < param.size(); i++) {
				ParamType paramType = (ParamType)param.get(i);
				this.params.put(paramType.getName(), paramType.getValue());
			}
		}
    }
    
    private void genDataFile() throws IOException, JAXBException, cwException, qdbException{
    	getParamHash();
		fileRoot = new File(resultFileRoot);
        if(!fileRoot.exists()) {
        	fileRoot.mkdirs();
        }

    	List table_group_lst = this.cfgDataLoader.getTableGroup();
    	if(table_group_lst != null) {

    		for(int i=0; i<table_group_lst.size(); i++) {
    	    	Vector outputObj = new Vector();
    			TableGroupType tableGroup = (TableGroupType)table_group_lst.get(i);
    			String prefix_fileName = tableGroup.getName();
    			boolean enabled = tableGroup.isEnabled();
    			int count = tableGroup.getCount();
    			if(enabled) {
    				CommonLog.debug("  " + prefix_fileName + " start...");
    				long s1 = System.currentTimeMillis();
    				List table_lst = tableGroup.getTable();
    				//generate the BufferedOutputStream object for each file.
					for(int table_cnt=0; table_cnt<table_lst.size(); table_cnt++) {
						TableType table = (TableType)table_lst.get(table_cnt);
						String fileName = prefix_fileName + (table_cnt + 1) + "_" + table.getName() + ".txt";
						File resultFile = new File(fileRoot, fileName); 
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(resultFile));
						outputObj.add(out);
					}
					
					try {
						//generate the content and write to files.
	    				for(int total_cnt=0; total_cnt<count; total_cnt++) {
	    					columns = new Hashtable();
	    					for(int table_cnt=0; table_cnt<table_lst.size(); table_cnt++) {
	    						Vector col_name = new Vector();
	    						Vector col_value = new Vector();
	
	    						TableType table = (TableType)table_lst.get(table_cnt);
	    						List column_lst = table.getColumn();
	    						for(int col_cnt=0; col_cnt<column_lst.size(); col_cnt++) {
	    							ColumnType column = (ColumnType)column_lst.get(col_cnt);
	    							String name = column.getName();
	    							String value = "";
	    							List part_lst = column.getPart();
	    							if(part_lst != null && part_lst.size() > 0) {
	    								for(int m=0; m<part_lst.size(); m++) {
	    									PartType part = (PartType)part_lst.get(m);
	    									value += getValue(part.getValue());
	    								}
	    							} else {
	   									long start_value = column.getStartValue();
	    								if(start_value > 0) {
	    									long next = column.getNext();
	    									value = String.valueOf(start_value + total_cnt/next);
	    								} else {
	    									value = getValue(column.getValue());
	    								}
	    							}

	    							columns.put(name, value);
	    							
	    							col_name.addElement(name);
	    							col_value.addElement(value);
	    						}
	    						
	    						BufferedOutputStream writer = (BufferedOutputStream)outputObj.elementAt(table_cnt);
	    						writeFile(col_name, col_value, writer);
	    					}
	    				}
					} finally {
						for(int w_cnt=0; w_cnt<outputObj.size(); w_cnt++) {
							BufferedOutputStream writer = (BufferedOutputStream)outputObj.elementAt(w_cnt);
							writer.close();
						}
						CommonLog.info("  " + prefix_fileName + " end. Used time : " + (System.currentTimeMillis() - s1));
					}
    			}
    		}
    	}
    }
    
    private String getValue(String inValue) throws cwException {
    	String value = "";
    	if(inValue != null) {
    		if(inValue.startsWith(varible_ind)) {
    			String key = inValue.substring(1);
    			value = (String)this.columns.get(key) != null ? (String)this.columns.get(key) : (String)this.params.get(key);
    			if(value == null) {
    				throw new cwException("undefined variable : " + inValue);
    			}
    		} else {
    			value = inValue;
    		}
    	}
    	return value;
    }
    
   	private void writeFile(Vector col_name, Vector col_value, BufferedOutputStream out) throws IOException, qdbException {
    	StringBuffer content = new StringBuffer();
    	String name = null;
    	String value = null;
    	String usr_ste_usr_id = "";
    	for(int i=0; i<col_name.size(); i++) {
    		name = (String)col_name.elementAt(i);
    		value = (String)col_value.elementAt(i);
    		if(name.equals("usr_ste_usr_id")) {
    			usr_ste_usr_id = value;
    		}
    	}

    	for(int i=0; i<col_name.size(); i++) {
    		name = (String)col_name.elementAt(i);
    		value = (String)col_value.elementAt(i);
			if(name != null && name.equals("usr_pwd")) {
				value = dbRegUser.encrypt(value, new StringBuffer(usr_ste_usr_id).reverse().toString());
			}
			content.append(value);
			if(i != col_name.size() - 1) {
				content.append(spliter);
			}
    	}
    	content.append(cwUtils.NEWL);

    	byte[] data = null;
    	if(isUnicodeFile) {
    		data = content.toString().getBytes(cwUtils.ENC_UNICODE_LITTLE);
    		out.write(data, 2, data.length - 2);//skip the unicode bom
    	} else {
    		data = content.toString().getBytes();
    		out.write(data, 0, data.length);
    	}
    }
    
}

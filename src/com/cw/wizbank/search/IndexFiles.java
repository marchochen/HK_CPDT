package com.cw.wizbank.search;

import java.io.*;
import java.util.Calendar;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.cn.*;

import com.cw.wizbank.search.document.*;
import com.cwn.wizbank.utils.CommonLog;

public class IndexFiles {
	
	static int editCount = 0;
    static String ADD = "ADD";
    static String REMOVE = "REMOVE";
    
    static String KEY = "KEY";
    static String PATH = "PATH";
    static String BODY = "BODY";
    static String OBJ_ID = "OBJ_ID";
    static String VERSION_NUMBER = "VERSION_NUMBER";
    static String FILENAME = "FILENAME";
    static String WORK_FOLDER_PATH = "WORK_FOLDER_PATH";
    static String WORK_FOLDER_ID = "WORK_FOLDER_ID";
    static String DOMAIN_PATH = "DOMAIN_PATH";
    static String DOMAIN_ID = "DOMAIN_ID";
    static String DOMAIN = "DOMAIN";
    static String TITLE = "TITLE";
    static String DESCRIPTION = "DESCRIPTION";
    static String KEYWORD = "KEYWORD";
    static String TEMPLATE_DATA = "TEMPLATE_DATA";
    static String TYPE_ID = "TYPE_ID";
    static String AUTHOR = "AUTHOR";
    static String MODIFIED_DATE = "MODIFIED_DATE";
    static String CALL_NUMBER = "CALL_NUMBER";
    
    String key = null;
    String indexPath = null;
    String keyword = null;
    String templateData = null;
    String docPath = null;
    String objID = null;
    String versionNum = null;
    String filename = null;
    String workFolderPath = null;
    String workFolderID = null;
    String callNum = null;
    String[] domainPath = null;
    String[] domainID = null;
    String title = null;
    String description = null;
    String typeID = null;
    String author = null;
    String modifiedDate = null;
    String encoding = null;
    boolean boolDocument = false;
    
    String action = null;
    
    public IndexFiles(String indexPath) {
        this.indexPath = indexPath;
    }

    /*
    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }
    */
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public void setPath(String docPath) {
        this.docPath = docPath;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public void setVersion(String versionNum) {
        this.versionNum = versionNum;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCallNumber(String callNum) {
        this.callNum = callNum;
    }

    public void setTemplateData(String templateData) throws SAXException, ParserConfigurationException, IOException {
        StringReader in = new StringReader(templateData);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser  = saxFactory.newSAXParser();
        TemplateDataParser myXMLparser = new TemplateDataParser();
        saxParser.parse(new InputSource(in), myXMLparser);
        this.templateData = myXMLparser.templateData;
        in.close();
    }

    /*
    public void setWorkFolder(String workFolderPath, String workFolderID) {
        this.workFolderPath = workFolderPath;
        this.workFolderID = workFolderID;
    }
    

    public void setDomain(String[] domainPath, String[] domainID) {
        this.domainPath = domainPath;
        this.domainID = domainID;
    }
    */

    public void setDomain(String[] domainID) {
        this.domainID = domainID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // set the document type
    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // set the modified date in String format
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    // set to true is index the keyword only
    public void indexDocument(boolean flag) {
    	CommonLog.debug("setting document flag :" + flag);        
        boolDocument = flag;
    }

    public void addIndex() throws IOException, FileNotFoundException, UnsupportedEncodingException{
        IndexWriter writer;
        InputStream is = null;

        if (!IndexReader.indexExists(indexPath)) {
            writer = new IndexWriter(indexPath, new ChineseAnalyzer(), true);
        }
        else {
            writer = new IndexWriter(indexPath, new ChineseAnalyzer(), false);
        }

		writer.infoStream = System.out;
        Document doc = new Document();

        if (boolDocument == true) {
            try {
                doc.add(Field.UnIndexed(PATH, docPath));
                
                CommonLog.debug("Indexing file " + docPath);
                is = new FileInputStream(docPath);

                // special case for WORD document
                if (docPath.toLowerCase().endsWith(".doc")) {
    	            String file_content = "";
                    ReadWord myReadWord = new ReadWord();
                    file_content = myReadWord.getContent(docPath);
                    doc.add(Field.Text(BODY, file_content));
                }
                else if (docPath.toLowerCase().endsWith(".html") || docPath.toLowerCase().endsWith(".htm")) {
    	            String file_content = "";
                    HTMLStringFilter myHTMLStringFilter = new HTMLStringFilter(docPath);
                    file_content = myHTMLStringFilter.getText();
                    doc.add(Field.Text(BODY, file_content));
                }
                else if (docPath.toLowerCase().endsWith(".pdf")) {
    	            String file_content = "";
                    ReadPDF myReadPDF = new ReadPDF();
                    file_content = myReadPDF.getText(docPath);
                    doc.add(Field.Text(BODY, file_content));
                }
                else if (docPath.toLowerCase().endsWith(".txt") || docPath.toLowerCase().endsWith(".ini")) {
                    if (encoding == null) {
                        doc.add(Field.Text(BODY, (Reader) new InputStreamReader(is)));
                    }
                    else {
                        doc.add(Field.Text(BODY, (Reader) new InputStreamReader(is, encoding)));
                    }
                }
                else {
                    // un-support file format
                }
                    
            } catch (Exception e) {
            	CommonLog.error("Error in indexing the file");
            	CommonLog.error(e.getMessage(),e);
            }
        }

        if (key != null && key.trim().length() > 0) {
            doc.add(Field.UnIndexed(KEY, key));
        }

        if (objID != null && objID.trim().length() > 0) {
            doc.add(Field.UnIndexed(OBJ_ID, objID));
        }

        if (versionNum != null && versionNum.trim().length() > 0) {
            doc.add(Field.UnIndexed(VERSION_NUMBER, versionNum));
        }

        if (filename != null && filename.trim().length() > 0) {
            doc.add(Field.UnIndexed(FILENAME, filename));
        }

        if (workFolderPath != null && workFolderPath.trim().length() > 0) {
            doc.add(Field.UnIndexed(WORK_FOLDER_PATH, workFolderPath));
        }
            
        if (workFolderID != null && workFolderID.trim().length() > 0) {
            doc.add(Field.UnIndexed(WORK_FOLDER_ID, workFolderID));
        }
            
            
        if (domainID != null) {
            String domainList = "";
            for (int j=0; j<domainID.length; j++) {
                /*
                if (domainPath[j] != null && domainPath[j].trim().length() > 0) {
                    doc.add(Field.UnIndexed(DOMAIN_PATH + Integer.toString(j+1), domainPath[j]));
                }
                */
                    
                // index the domain ID
                if (domainID[j] != null && domainID[j].trim().length() > 0) {
                    domainList += DOMAIN + domainID[j] + " ";
                }
            }
            doc.add(Field.Text(DOMAIN_ID, domainList));
        }
            
        if (title != null && title.trim().length() > 0) {
            //doc.add(Field.UnIndexed(TITLE, title));
            doc.add(Field.Text(TITLE, title));
        }
        
        if (description != null && description.trim().length() > 0) {
            doc.add(Field.UnIndexed(DESCRIPTION, description));
        }
            
        if (typeID != null && typeID.trim().length() > 0) {
            doc.add(Field.Text(TYPE_ID, typeID));
        }
            
        if (author != null && author.trim().length() > 0) {
            //doc.add(Field.UnIndexed(AUTHOR, author));
            doc.add(Field.Text(AUTHOR, author));
        }

        if( callNum != null && callNum.trim().length() > 0 ) {
            doc.add(Field.Text(CALL_NUMBER, this.callNum));
        }

        if (modifiedDate != null && modifiedDate.trim().length() > 0) {
            doc.add(Field.UnIndexed(MODIFIED_DATE, modifiedDate));
        }            
            
        // index the keyword
        if (keyword != null && keyword.trim().length() > 0) {
//System.out.println("keyword:" + keyword);            
            doc.add(Field.Text(KEYWORD, keyword));
        }

        // index the keyword
        if (templateData != null && templateData.trim().length() > 0) {
            doc.add(Field.Text(TEMPLATE_DATA, templateData));
//System.out.println("templateData:" + templateData);            
        }

        writer.addDocument(doc);
        //writer.optimize();
        writer.close();
        
        if (is != null) {
            is.close();
        }
    }
    
    /*
    public void addDocument(String index_path, String doc_path) {
        IndexFiles myIndexFiles = new IndexFiles();
        String[] docPath = new String[1];
        docPath[0] = doc_path;
        try {
            myIndexFiles.setIndexPath(index_path);
            myIndexFiles.setPath(docPath);
            myIndexFiles.addIndex();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
    */
    
    public void removeIndex() {

        try {
            IndexReader myIndexReader = IndexReader.open(indexPath);
            for (int i=0; i<myIndexReader.maxDoc(); i++) {
            	if( !myIndexReader.isDeleted(i) ) {
					Document myDocument = myIndexReader.document(i);
					if (myDocument.get(IndexFiles.KEY).equalsIgnoreCase(key)) {
						myIndexReader.delete(i);
						myIndexReader.close();
						//return;
					}            		
            	}
            }

            myIndexReader.close();
        } catch(Exception e) {
        	CommonLog.error("Error in deleting the document from index");
        	CommonLog.error(e.getMessage(),e);
        }
    }
    
    
    // usage: IndexFiles index-path file . . . 
    public static void main(String[] args) throws Exception {
        IndexFiles myIndexFiles = new IndexFiles(args[0]);
        String[] docPath = new String[args.length-1];
        
        // testing purpose only
        String[] objID = new String[args.length-1];
        String[] verNum = new String[args.length-1];
        String[] filename = new String[args.length-1];
        String[] workFolderPath = new String[args.length-1];
        String[] workFolderID = new String[args.length-1];
        String[][] domainPath = new String[args.length-1][2];
        String[][] domainID = new String[args.length-1][2];
        String[] title = new String[args.length-1];
        String[] description = new String[args.length-1];
        String[] keyword = new String[args.length-1];
        String[] typeID = new String[args.length-1];
        String[] author = new String[args.length-1];
        String[] modifiedDate = new String[args.length-1];
        
        for (int i=1; i<args.length; i++) {
            docPath[i-1] = args[i];
            
            // testing purpose only
            objID[i-1] = Integer.toString(i);
            verNum[i-1] = "Ver. " + Integer.toString(i);
            filename[i-1] = docPath[i-1];
            //workFolderPath[i-1] = "Workfolder Path " + Integer.toString(i);
            //workFolderID[i-1] = "Workfolder ID " + Integer.toString(i);
            title[i-1] = "Title " + Integer.toString(i);
            description[i-1] = "Desc " + Integer.toString(i);
            keyword[i-1] = "Keyword " + Integer.toString(i);
            typeID[i-1] = "DOC";
            author[i-1] = "Author " + Integer.toString(i);
            modifiedDate[i-1] = Long.toString(Calendar.getInstance().getTime().getTime());
            /*
            for (int j=0; j<domainPath[i-1].length; j++) {
                domainPath[i-1][j] = "Domain Path " + Integer.toString(i) + ":" + Integer.toString(j+1);
            }
            */
            /*
            for (int j=0; j<domainID[i-1].length; j++) {
                domainID[i-1][j] = Integer.toString(j+1);
            }
            */
            domainID[i-1][0] = "1";
            domainID[i-1][1] = Integer.toString(i);            
        }
        
        for (int i=0; i<args.length-1; i++) {
            // set the key
            myIndexFiles.setKey(docPath[i]);
            
            myIndexFiles.setPath(docPath[i]);
            
            // testing purpose only
            myIndexFiles.setObjID(objID[i]);
            myIndexFiles.setVersion(verNum[i]);
            myIndexFiles.setFilename(filename[i]);
            myIndexFiles.setKeyword(keyword[i]);
            //myIndexFiles.setWorkFolder(workFolderPath[i], workFolderID[i]);
            myIndexFiles.setDomain(domainID[i]);
            myIndexFiles.setTitle(title[i]);
            myIndexFiles.setDescription(description[i]);
            myIndexFiles.setTypeID(typeID[i]);
            myIndexFiles.setAuthor(author[i]);
            myIndexFiles.setModifiedDate(modifiedDate[i]);
                
            myIndexFiles.setEncoding("Big5");
            
            myIndexFiles.addIndex();
        }
    }
}
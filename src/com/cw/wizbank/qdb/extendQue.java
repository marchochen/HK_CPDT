package com.cw.wizbank.qdb;
import java.util.Vector;
import java.util.Hashtable;
// ----------------------------------------------------------------------------
// QDB question XML formation (2000.07.25 wai)
// with fields for various attribute of a question
// and methods to make them into XML
// ----------------------------------------------------------------------------
// -- currently only support interaction of type MC and FB --
// -- be careful when reusing this construct for more than one questions --
// ----------------------------------------------------------------------------
// steps on generating XML:
// 1. create an instance of "extendQue"
//    specify maximum no. of objective ID, maximum no. of interaction and maximum no. of options per interaction
// 2. assign attributes for the question, and for the interactions of this question and for the options of each interaction
// 3. call extendQue.getBody() once to get the <body>...</body>
// 4. for each interaction, call extendQue.queInter[n].getOutcome() to get its <outcome>...</outcome>
// 5. for each interaction, call extendQue.queInter[n].getExplanation() to get its <explanation>...</explanation>

public class extendQue extends Object {
    
    public static final String MC_LOGIC_AND = "AND";
    public static final String MC_LOGIC_OR = "OR";
    public static final String MC_LOGIC_SINGLE = "SINGLE";

    public static final String TF_ANSWER_TRUE = "TRUE";
    public static final String TF_ANSWER_FALSE = "FASLE";
    // constants
    private final String trueFalse_  = "TF";
    private final String multiChoice_  = "MC";
    private final String fillBlank_    = "FB";
    private final String matching_    = "MT";
    private final String typing_       = "TP";
	private final String essay_        = "ES";
	private final String essay2_        = "ES2";
	
    private final String blankTag_     = "[blank]";
    private final String htmlOpenTag_  = "<html>";
    private final String htmlCloseTag_ = "</html>";
	public int int_count = 0;
    //for checking if all the fields in this question are valid
    public boolean parseOK;
	public boolean submit_file_ind;
	public boolean shuffle;
    public String sc_sub_shuffle;
    public String sc_criteria;
    //attributes about a question
    public String  cont;       // content / body
    public boolean cont_html;  // content with html tag? (Y/N)
    public boolean cont_hint; // content with hints? (Y/N)
    public String  cont_pic;   // content media file
    public Vector  vFiles;     // content upload files
    public long    id;         // 
    public String  title;      // title / keyword
    public String  desc;       // description
    public int     diff;       // difficulty (1-easy/2-normal/3-hard)
    public float   dur;        // duration
    public String  onoff;      // online/offline status indicator (ON-online/OFF-offline)
    public String  folder;     // folder / privilege (AUTHOR-personal/CW-cyberwisdom)
    public String  owner_id;   // user id of the owner
    public int     media_width;     // width of the media
    public int     media_height;    // height of the media 
    public long    obj_id[];   // objective id(s)
	public String  criteria;	//Criteria of Dynamic Scenario Question
	public long	   parent_id;	//Scenario sub-question parent id
    public String  lan;        // encoding
                               // Big5         (Traditional Chinese)
                               // ISO-8859-1   (English)
                               // GB2312       (Simplified Chinese)
    public queInter inter[];   // array of interaction
	public String queType;
    public long resource_id;     // 

    public matObj   src_obj[];     // array of source
    public matObj   tgt_obj[];     // array of target
    
    // formatting stuff
    private StringBuffer body;
    private StringBuffer bodyWork;
    
	public Hashtable h_criteria = null;
    
    // constructor
    public extendQue(int MaxObjID, int MaxInter, int MaxOpt, int MaxSrc, int MaxTgt) {
        // allocate all resources
        obj_id = new long[MaxObjID];
        inter  = new queInter[MaxInter];
        for (int i = 0; i < MaxInter; i++)
            inter[i] = new queInter((i + 1), MaxOpt);
        
        src_obj = new matObj[MaxSrc];
        for (int i = 0; i < MaxSrc; i++)
            src_obj[i] = new matObj(i+1);

        tgt_obj = new matObj[MaxTgt];
        for (int i = 0; i < MaxTgt; i++)
            tgt_obj[i] = new matObj(i+1);

        body     = new StringBuffer(300);
        bodyWork = new StringBuffer(300);
        
        media_width = 0;
        media_height = 0;
        
        cont_hint = false;
        parseOK = true;
		h_criteria = new Hashtable();
        
    }
    
    public String getBlank() {
        return blankTag_;
    }
    
    public int getScore() {
		int score = 0;
		if( inter != null ) { 
			for (int i=0; i < inter.length; i++) {
	        	if (inter[i].type == null)
	            	break;
	            else {
	            	score += inter[i].calScore(); 
	            }
			}
		}
        return score; 
    }
    
    // format and return the content within body tag
    public String getBody() {
        body.setLength(0);
        
        // question main content
        body.append("<body>").append(setBodyInter(cont));
        // question media file
        if (cont_pic != null && cont_pic.length() > 0)
            body.append("<object type=\"").append(getObjType(cont_pic)).append("\" data=\"").append(dbUtils.esc4XML(cont_pic)).append("\" />");

        // question upload files
        if (vFiles != null) {
            for(int i=0; i<vFiles.size(); i++) {
                body.append("<object type=\"").append(getObjType((String)vFiles.elementAt(i))).append("\" data=\"").append(dbUtils.esc4XML((String)vFiles.elementAt(i))).append("\" />");
            }
        }
        
		if (inter!=null && inter.length>0 && inter[0].type.equalsIgnoreCase(matching_)) {
            if (media_width > 0)
                body.append("<media width=\"").append(media_width).append("\" height=\"").append(media_height).append("\" />");
            
            body.append("<hints type=\"");
                if (cont_hint) 
                    body.append("Y"); 
                else 
                    body.append("N");
                
                body.append("\" />"); 
     
            body.append("<source>"); 
            for(int i=0;i<src_obj.length;i++) {
				if (src_obj[i].text != null && src_obj[i].text.length() > 0) {
					body.append("<item id=\"").append(src_obj[i].id).append("\">"); 
						body.append("<text>"); 
						body.append(src_obj[i].text);
						body.append("</text>");
					if (src_obj[i].media != null && src_obj[i].media.length() > 0) {
						body.append("<object type=\"").append(getObjType(src_obj[i].media)).append("\" data=\"").append(dbUtils.esc4XML(src_obj[i].media)).append("\" />");
					}
					body.append("</item>");
				}
            }
            body.append("</source>");
                
            
        }
        
        body.append("</body>");
        
        return dbUtils.delTab(body.toString());
        
    }
    // Get the content before the first interaction.
    public String getDesc() {
        
        StringBuffer queDesc = new StringBuffer();
    
        queDesc.setLength(0);
        
        // question main content
        int j= cont.indexOf(blankTag_, 0); 
        int i = 0; 
        if (j >= 0) 
            i = j;
        else 
            i = cont.length(); 

        if (cont_html)
            queDesc.append(htmlOpenTag_).append(cont.substring(i)).append(htmlCloseTag_);
        else
            queDesc.append(cont.substring(i));
            
        return queDesc.toString();

    }
    
    
    // substitute all blanks within the question content with any corresponding interaction tags
    private String setBodyInter(String inCont) {
        if (inCont == null) return "";
        
        // initialize working storage
        bodyWork.setLength(0);
        int i = 0, j = 0, interCnt = 0;
        // for each blank found, substitute it with a corresponding formatted interaction tag
        while ((j = inCont.indexOf(blankTag_, i)) > -1) {
            // the part before this blank and after the last blank
            if (cont_html)
                bodyWork.append(htmlOpenTag_).append(inCont.substring(i, j)).append(htmlCloseTag_);
            else
                bodyWork.append(inCont.substring(i, j));
            // if the interaction exists for this blank, use it
            if (interCnt < inter.length && inter[interCnt].type != null)
                bodyWork.append(inter[interCnt].getIaction());
            // next blank
            interCnt++;
            i = j + blankTag_.length();
        }
        // substitute [blank x] to the corresponding interaction
        String blank_prefix = "[blank";
        String blank_prefix2 = "[blank";
        if (inCont.indexOf(blank_prefix2) != -1) {
            String que_cont = inCont;
            inCont = "";
            while (que_cont.indexOf(blank_prefix) != -1 && que_cont.charAt(que_cont.indexOf(blank_prefix) + blank_prefix.length()) != ']') {
                if (cont_html) {
                    que_cont = htmlOpenTag_ + que_cont.substring(0, que_cont.indexOf(blank_prefix)) + htmlCloseTag_ + que_cont.substring(que_cont.indexOf(blank_prefix));
                }
                
                String temp_que_cont = que_cont.substring(que_cont.indexOf(blank_prefix));
                int blank_close_index = que_cont.indexOf(blank_prefix) + temp_que_cont.indexOf("]");
                String src_pattern = que_cont.substring(que_cont.indexOf(blank_prefix), blank_close_index+1);
                
                // find the interaction order
                int int_order = 0;
                for (int k=src_pattern.length()-2; k>=0; k--) {
                    if (src_pattern.charAt(k) == 'k') {
                        int_order = Integer.parseInt(src_pattern.substring(k+1,src_pattern.length()-1));
                        // avoid the addition of remaining interaction ate the end
                        interCnt = inter.length;
                        break;
                    }
                }
                
                bodyWork.append(replaceStr(que_cont.substring(0, blank_close_index+1), src_pattern, inter[int_order-1].getIaction()));
                que_cont = que_cont.substring(blank_close_index+1);
            }
            
            if(que_cont != null && que_cont.length() > 0){
            	if(que_cont.lastIndexOf(blank_prefix) > -1){
            		que_cont = que_cont.substring(que_cont.lastIndexOf(blank_prefix) + blank_prefix.length() + 1);
            	}
            }
            if (cont_html) {
                bodyWork.append(htmlOpenTag_ + que_cont + htmlCloseTag_);
            }
            else {
                bodyWork.append(que_cont);
            }
        }
        // continue the original [blank] case
        else {        
            // the remaining part after the last blank
            if (cont_html)
                bodyWork.append(htmlOpenTag_).append(inCont.substring(i)).append(htmlCloseTag_);
            else
                bodyWork.append(inCont.substring(i));
            // the remaining interactions, if any
            if( inter != null ) {
	            for (; interCnt < inter.length; interCnt++) {
	                if (inter[interCnt].type == null)
	                    break;
	                bodyWork.append(inter[interCnt].getIaction());
	            }
            }
        }
        
        return bodyWork.toString();
    }
    
    // get the type of the media file, by looking at the extension of the filename
    static String getObjType(String inName) {
        String ext, objType;
        
        // get the extension
        int period_idx = inName.lastIndexOf(".");
        if (period_idx >= 0)
            ext = inName.substring(period_idx);
        else
            ext = "";
        // determine the type
        if (ext.equalsIgnoreCase(".gif"))
            objType = "image/gif";
        else if (ext.equalsIgnoreCase(".jpg"))
            objType = "image/jpg";
        else if (ext.equalsIgnoreCase(".png"))
            objType = "image/png";
        else if (ext.equalsIgnoreCase(".swf"))
            objType = "application/x-shockwave-flash";
        else {
            objType = "unknown";
        }
        return objType;
    }
    
    // inner class for an matching object
    public class matObj {
        public int id ;        // id of the object
        public String text;    // text of the object
        public String media;   // filename of the object
        
        matObj (int index) {
            id      = index;
        }
    }
    
    // ------------------------------------------------------------------------
    // inner class for an interaction within a question
    public class queInter {
        // attribute about an interaction
        public String  type;    // interaction type (MC-multiple choice/FB-fill in the blank)
        public String  shuffle; // allow shuffle indicator      (for MC only)
        public String  logic;   // for mulitple correct MC  ( Value : OR , AND )
                                // OR : correct if any correct answer is selected
                                //AND : correct only if all correct answer(s) are selected.
        public int     att_len; // allowed length of an attempt (for FB only)
        public int     score;   // 
        public queOpt  opt[];   // possible options(MC) / possible answers(FB)
        
        // formatting stuff
        private int order;      // order of this interaction within the question
        private StringBuffer iaction;
        private StringBuffer outcome;
        private StringBuffer explain;
        
        // for other option of MC
        public boolean has_other_option;
        
        // constructor
        public queInter(int inOrder, int MaxOpt) {
            // allocate all resources
            order = inOrder;
            opt   = new queOpt[MaxOpt];
            for (int i = 0; i < MaxOpt; i++)
                opt[i] = new queOpt();
            iaction = new StringBuffer(300);
            outcome = new StringBuffer(400);
            explain = new StringBuffer(200);
        }
        
        // format and return the content within interaction tag
        public String getIaction() {
            iaction.setLength(0);

            calScore(); 
            
            // interaction header
			if (type.equals(matching_)) {
				if (tgt_obj[order-1].text != null && tgt_obj[order-1].text.length() > 0) {
					iaction.append("<interaction order=\"").append(order).append("\" type=\"").append(type).append("\" score=\"").append(score);
				}
			} else {
				iaction.append("<interaction order=\"").append(order).append("\" type=\"").append(type).append("\" score=\"").append(score);
			}

            if (type.equals(multiChoice_)) {
                iaction.append("\" shuffle=\"").append(shuffle);
                if (logic !=null) {
                    iaction.append("\" logic=\"").append(logic);
                }
            }else if (type.equals(fillBlank_) || type.equalsIgnoreCase(essay_) || type.equalsIgnoreCase(essay2_))
                iaction.append("\" length=\"").append(att_len);
            
			if (type.equals(matching_)) {
				if (tgt_obj[order-1].text != null && tgt_obj[order-1].text.length() > 0) {
					iaction.append("\">");
				}
			} else {
				iaction.append("\">");
			}
            
            // possible options for MC only
            if (type.equals(multiChoice_)) {
                for (int i = 0; i < opt.length; i++) {
                    if (opt[i].cont == null) break;
                    if(has_other_option && (i + 1) == opt.length) {
                    	iaction.append("<option id=\"").append((i+1)).append("\" other_option_ind=\"true\">");
                    } else {
                    	iaction.append("<option id=\"").append((i+1)).append("\">");
                    }
                    if (opt[i].cont_html)
                        iaction.append(htmlOpenTag_).append(opt[i].cont).append(htmlCloseTag_);
                    else
                        iaction.append(opt[i].cont);
                    if (opt[i].cont_pic != null && opt[i].cont_pic.length() > 0)
                        iaction.append("<object type=\"").append(getObjType(opt[i].cont_pic)).append("\" data=\"").append(dbUtils.esc4XML(opt[i].cont_pic)).append("\" />");
                    iaction.append("</option>");
                }
            }else if( type.equalsIgnoreCase(typing_)) {                
                iaction.append("<option id=\"1\">")
                       .append(opt[0].cont)
                       .append("</option>");
            } else if (type.equals(trueFalse_)) {
                for (int i = 0; i < opt.length; i++) {
                    if (opt[i].cont == null) break;
                    
                    iaction.append("<option id=\"").append((i+1)).append("\">");
                    iaction.append(opt[i].cont);
                    iaction.append("</option>");
                }
            }else if (type.equals(matching_)) {
                if (tgt_obj[order-1].text != null && tgt_obj[order-1].text.length() > 0) {
                    iaction.append("<text>"); 
                    iaction.append(tgt_obj[order-1].text);
                    iaction.append("</text>");
                }
                if (tgt_obj[order-1].media != null && tgt_obj[order-1].media.length() > 0) {
                    iaction.append("<object type=\"").append(getObjType(tgt_obj[order-1].media)).append("\" data=\"").append(dbUtils.esc4XML(tgt_obj[order-1].media)).append("\" />");
                }
            }
			if (type.equals(matching_)) {
				if (tgt_obj[order-1].text != null && tgt_obj[order-1].text.length() > 0) {
					iaction.append("</interaction>");
				}
			} else {
				iaction.append("</interaction>");
			}
            
            
            return iaction.toString();
        }
        
        // Calculate the max. score of an iteraction 
        public int calScore() {
            score =0;
            if (type.equalsIgnoreCase(matching_) ||
                (type.equalsIgnoreCase(multiChoice_) && logic !=null 
                 && logic.equalsIgnoreCase(MC_LOGIC_OR))){

                for(int i=0;i<opt.length;i++) {
                    score += opt[i].score; 
                }
            }else {
                for(int i=0;i<opt.length;i++) {
                    if (opt[i].score > score)
                        score = opt[i].score; 
                }
            }
            
            return score; 
        }
        
        // format and return the content within outcome tag
        public String getOutcome() {
            outcome.setLength(0);
            
            calScore(); 
            
            // outcome header
			if (type.equals(matching_)) {
				if (opt.length > 0) {
					outcome.append("<outcome order=\"").append(order).append("\" type=\"").append(type).append("\" score=\"").append(score);
				}
			} else {
				outcome.append("<outcome order=\"").append(order).append("\" type=\"").append(type).append("\" score=\"").append(score);
			}
            
            if (type.equals(multiChoice_)) {
                if (logic !=null) {
                    outcome.append("\" logic=\"").append(logic);
                }                    
            }
            if (type.equals(matching_)) {
                if (opt.length > 0) {
                    outcome.append("\">");
                }
            } else {
                outcome.append("\">");
            }
            
            // possible answers
            
            for (int i = 0; i < opt.length; i++) {
                if (opt[i].cont == null)
                    break;
                
                outcome.append("<feedback condition=\"");
                if (type.equals(multiChoice_) || type.equals(trueFalse_) || type.equals(typing_))
                    outcome.append((i+1));
                else if (type.equals(fillBlank_))
                    outcome.append(opt[i].cont).append("\" case_sensitive=\"").append(opt[i].case_sense).append("\" space_sensitive=\"").append(opt[i].spc_sense).append("\" type=\"").append(opt[i].type);
                else if (type.equals(matching_))
                    outcome.append(opt[i].cont);
                
                if (opt[i].score > 0) {
                    outcome.append("\" score=\"").append(opt[i].score).append("\"/>");
                }else
                    outcome.append("\"/>");
            }
			if (type.equals(matching_)) {
				if (opt.length > 0) {
                    outcome.append("</outcome>");
				}
			} else {
				outcome.append("</outcome>");
			}
            
            return outcome.toString();
        }
        
        // format and return the content within explanation tag
        public String getExplanation() {
            explain.setLength(0);
            
            // explanation header
			if (type.equals(matching_)) {
				if (opt.length > 0) {
					explain.append("<explanation order=\"").append(order).append("\" type=\"").append(type).append("\">");
				}
			} else {
				explain.append("<explanation order=\"").append(order).append("\" type=\"").append(type).append("\">");
			}
            // explanations for each possible option/answer
            for (int i = 0; i < opt.length; i++) {

				// perform checking only if type is not QUE_TYPE_ESSAY and not QUE_TYPE_ESSAY_2
				if ( queType!= null && !queType.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) 
					&& !queType.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2) ) {
				
	                if ( opt[i].cont == null && queType!= null 
	                && ( !queType.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) 
	                  || !queType.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2) ) ) {
						break; 
	                  }
				}
					
                explain.append("<rationale");
                if (type.equals(multiChoice_) || type.equals(trueFalse_) || type.equals(typing_))
                    explain.append(" id=\"").append((i+1)).append("\"");
                else if(opt[i].cont != null) 
                    explain.append(" condition=\"").append(opt[i].cont).append("\"");
                
                explain.append(">");
                
                if (opt[i].exp != null && opt[i].exp.length() > 0)
                    explain.append(opt[i].exp);

                explain.append("</rationale>");
            }
			if (type.equals(matching_)) {
				if (opt.length > 0) {
					explain.append("</explanation>");
				}
			} else {
				explain.append("</explanation>");
			}
            
            return explain.toString();
        }
        
        // ------------------------------------------------------------------------
        // inner class for an option within an interaction
        public class queOpt {
            // attribute about an option
            public int     id;         // answer (MT)
            public String  cont;       // choice(MC) / answer(FB)
            public boolean cont_html;  // choices with html tag? (Y/N) (for MC only)
            public String  cont_pic;   // media file of choices (for MC only)
            public int     score;      // score gain for each option
            public String  case_sense; // case sensitive? (Y/N) (for FB only)
            public String  spc_sense;  // space sensitive? (Y/N) (for FB only)
            public String  type;       // data type (Text/Number) (for FB only)
            public String  exp;        // explanation for each option
            public String  cond;       // condition of this choice towards the complete answer (AND/OR)
        }
        
        
    }
    
   	private String replaceStr(String inStr, String src_pattern, String target_pattern) {
        String result = "";
   	    while (inStr.indexOf(src_pattern) != -1) {
   	        int index = inStr.indexOf(src_pattern);
   	        result += inStr.substring(0, index) + target_pattern;
   	        inStr = inStr.substring(index + src_pattern.length());
   	    }
   	    
   	    return result+inStr;
   	}

}
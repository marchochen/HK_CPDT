package com.cw.wizbank.svnexport;

import java.io.*;
import java.util.List;

import com.cw.wizbank.util.HtmTemplateModule;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.HtmTemplateModule.HtmTemplate2Htm;

public class SvnExportBuilder
{

    private static final String SLASH = System.getProperty("file.separator");
    private String projectPath;
    private String exportPath;
    private String compressDir;
    private String toVersion;
	private boolean copyedWizbankJar = false;
	private boolean processAllHtmTpl = false;

    public static void main(String args[]) throws Exception {
    	File difftxtFile = new File(args[0]);
    	File exportxmlFile = new File(args[1]);
    	if(difftxtFile.isDirectory() || exportxmlFile.isDirectory()){
    		throw new Exception();
    	}

        SvnExportBuilder svnexportbuilder = new SvnExportBuilder();
        svnexportbuilder.setProjectPath(args[2]);
        svnexportbuilder.setExportPath(args[3]);
        svnexportbuilder.setCompressDir(args[4]);
        svnexportbuilder.setToVersion(args[5]);
        svnexportbuilder.generateBuildFile(difftxtFile, exportxmlFile);
    }

    public void generateBuildFile(File difftxtFile, File exportxmlFile)
        throws Exception
    {
        SvnDiffReader svndiffreader = new SvnDiffReader();
        generateBuildFile(svndiffreader.parse(difftxtFile), exportxmlFile);
    }

    public void generateBuildFile(List list, File exportxmlFile)
        throws Exception
    {
        File file1 = exportxmlFile.getParentFile();
        if(!file1.exists()){
            file1.mkdirs();
        }
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportxmlFile), "UTF8"));
        generateBuildFile(list, ((Writer) (bufferedwriter)));
        bufferedwriter.flush();
        bufferedwriter.close();
    }

    public void generateBuildFile(List list, Writer writer)
        throws Exception
    {
        writer.write("<project name=\"export\" default=\"export\" basedir=\".\">");
        writer.write("<description>execute files from localhost</description>");

        writer.write("<target name=\"export\" description=\"export files\">");
        if(list != null){
            for(int j = 0; j < list.size(); j++){
                writer.write(generateExportTask((String)list.get(j)));
            }
        }
        writer.write("</target>");

        writer.write("</project>");
        writer.flush();
    }

    private String generateExportTask(String s) throws Exception{
        StringBuffer stringbuffer = new StringBuffer();
        String s1 = null;
        if(s != null && s.length() > 0){
            int j = s.lastIndexOf('/');
            int k = s.lastIndexOf('\\');
            if(j > 0 || k > 0){
                s1 = j <= k ? s.substring(0, k) : s.substring(0, j);
            }
            
            String projectPrev = getProjectPath();
            String exportPrev = getExportPath() + SLASH;
            
            boolean isHtmTplFile = ((s.indexOf("src/htm_template") != -1) && (s.indexOf(".htm") != -1)) ? true : false;
            boolean isJavaFile = (s.indexOf(".java") != -1) ? true : false;
            boolean isJsFile = (s.indexOf(".js") != -1) ? true : false;
            boolean isCssFile = (s.indexOf(".css") != -1) ? true : false;

            if(s1 != null){
            	String fr = projectPrev + SLASH + s;
            	String to = exportPrev + s;
            	boolean frFileExists = (new File(fr)).exists();
            	if(!isHtmTplFile && !isJavaFile && frFileExists) {
           			stringbuffer.append("<copy file=\"").append(fr).append("\" tofile=\"").append(to).append("\"/>");
            	}
            	
            	if((isJsFile || isCssFile) && frFileExists) {
            		String type = "";
            		if(isJsFile) {
            			type = "js";
            		} else {
            			type = "css";
            		}
                	String argLine = " --type " + type + " --charset utf-8 -o " + to + " " + fr;
                	stringbuffer.append("<echo message=\"compressing file ").append(to).append("\"/>");
                	stringbuffer.append("<java jar=\"").append(getCompressDir()).append("\" fork=\"true").append("\" failonerror=\"true").append("\">")
                		.append("<arg line=\"").append(argLine).append("\"/>")
                		.append("</java>");
            	}

            	if (!copyedWizbankJar && isJavaFile) {
            		copyedWizbankJar = true;
            		String tmp = "www"+cwUtils.SLASH+"WEB-INF"+cwUtils.SLASH+"lib"+cwUtils.SLASH+"wizbank.jar";
            		String jarFr = projectPrev + SLASH + tmp;
            		String jarTo = exportPrev + tmp;
            		stringbuffer.append("<copy file=\"").append(jarFr).append("\" tofile=\"").append(jarTo).append("\"/>");
            	} else if(isHtmTplFile && frFileExists) {
        			HtmTemplateModule m = new HtmTemplateModule();
        			HtmTemplate2Htm h = m.new HtmTemplate2Htm();
        			m.setAppnRoot(getProjectPath());

        			if(!processAllHtmTpl) {
        				if(s.indexOf("src/htm_template/share") != -1) {
        					processAllHtmTpl = true;
        					h.processTemplate(null);
        					File templateFolder = new File(m.getAppnRoot() + HtmTemplateModule.getHtmTemplateFolder());
        					File[] templateLst = templateFolder.listFiles();
        					for (int i = 0; i < templateLst.length; i++) {
        						if (templateLst[i].isFile()) {
        							String filePath = templateLst[i].toString();
        							String fileName = filePath.substring(filePath.lastIndexOf(cwUtils.SLASH) + 1);
        							String htmFr = m.getAppnRoot() + h.getHtmFolder() + SLASH + fileName;
        							String htmTo = getExportPath() + h.getHtmFolder() + SLASH + fileName;
        							stringbuffer.append("<copy file=\"").append(htmFr).append("\" tofile=\"").append(htmTo).append("\"/>");
        						}
        					}
        				} else {
        					String htmName = s.substring((s1.length() + 1));
        					h.processTemplate(htmName);
        					String htmDir = h.getHtmFolder() + SLASH + htmName;
        					String htmFr = projectPrev + htmDir;
        					String htmTo = exportPrev + htmDir;
        					stringbuffer.append("<copy file=\"").append(htmFr).append("\" tofile=\"").append(htmTo).append("\"/>");
        				}
        			}
            	}
            }
        }
        return stringbuffer.toString();
    }

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	public String getCompressDir() {
		return compressDir;
	}

	public void setCompressDir(String compressDir) {
		this.compressDir = compressDir;
	}

	public String getToVersion() {
		return toVersion;
	}

	public void setToVersion(String toVersion) {
		this.toVersion = toVersion;
	}
}

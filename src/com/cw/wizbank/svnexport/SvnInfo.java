package com.cw.wizbank.svnexport;

import java.io.*;

public class SvnInfo {
	String curVersion;
	String exportDir;
	
	public static void main(String[] args) throws Exception {
    	File svninfotxt = new File(args[0]);
    	File svnpropertytxt = new File(args[1]);
    	if(svninfotxt.isDirectory() || svnpropertytxt.isDirectory()){
    		throw new Exception();
    	}
    	SvnInfo si = new SvnInfo();
    	si.setExportDir(args[2]);
    	si.setCurVersion(args[3]);
    	si.genPropertyFile(svninfotxt, svnpropertytxt);
    	si.genVersionTxt();
	}

	private void genPropertyFile(File svninfotxt, File svnpropertytxt) throws IOException {
		File file1 = svnpropertytxt.getParentFile();
		if (!file1.exists()) {
			file1.mkdirs();
		}
		
		BufferedReader infoReader = new BufferedReader(new InputStreamReader(new FileInputStream(svninfotxt)));
		String urlInd = "URL: ";
		String inputLine = "";
		String svnUrl = "";
		while((inputLine = infoReader.readLine()) != null) {
			if(inputLine.startsWith(urlInd) && inputLine.length() > (urlInd.length())) {
				svnUrl = inputLine.substring(urlInd.length());
				break;
			}
		}
		infoReader.close();
		
		OutputStreamWriter proWriter = new OutputStreamWriter(new FileOutputStream(svnpropertytxt));
		proWriter.write("svn.url=" + svnUrl);
		proWriter.flush();
		proWriter.close();

	}
	
	private void genVersionTxt() throws IOException {
		File versionTxt = new File(getExportDir(), "version.txt");
		OutputStreamWriter versionWriter = new OutputStreamWriter(new FileOutputStream(versionTxt));
		versionWriter.write("current version: " + getCurVersion());
		versionWriter.flush();
		versionWriter.close();
	}

	public String getCurVersion() {
		return curVersion;
	}

	public void setCurVersion(String curVersion) {
		this.curVersion = curVersion;
	}

	public String getExportDir() {
		return exportDir;
	}

	public void setExportDir(String exportDir) {
		this.exportDir = exportDir;
	}
}

package com.cw.wizbank.svnexport;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SvnDiffReader
{

    private ArrayList fileList;

    public SvnDiffReader()
    {
    }

    public List getFileList()
    {
        return fileList;
    }

    public List parse(File file)
        throws IOException
    {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String s = null;
        ArrayList arraylist = new ArrayList();
        while((s = bufferedreader.readLine()) != null) 
        {
            if(s.startsWith("Index:"))
            {
                arraylist.add(s.substring("Index:".length()).trim());
            }
        }
        fileList = arraylist;
        return fileList;
    }
}

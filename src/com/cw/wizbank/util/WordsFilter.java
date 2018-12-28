package com.cw.wizbank.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author DeanChen
 * 
 */
public class WordsFilter {

    /**
     * to get filter words by file path.
     * 
     * @param filepath
     * @param encode
     * @return
     * @throws IOException
     * @throws cwSysMessage
     * @throws cwException
     */
    public static Vector getFilterWordsVec(String filePathAbs)
            throws cwException {
        Vector wordsVec = new Vector();
        File file = new File(filePathAbs);
        if (!file.exists()) {
            return null;
        }
        int i = (int) file.length();
        if (i <= 0) {
            return null;
        }
        // if(!cwUtils.isValidEncodedFile(file, cwUtils.ENC_UNICODE_LITTLE)) {
        // throw new cwSysMessage("GEN008");
        // }
        try {
            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(cwUtils.openUTF8FileStream(file),
                            cwUtils.ENC_UTF), i);
            for (String wordsStr = null; (wordsStr = bufferedreader.readLine()) != null;) {
                String[] wordsArr = cwUtils.splitToString(wordsStr, ",", true);
                if (wordsArr != null) {
                    Vector wordsLineVec = cwUtils
                            .String2vector(wordsArr, false);
                    wordsVec = cwUtils.unionVectors(wordsLineVec, wordsVec,
                            false);
                }
            }
            bufferedreader.close();
        } catch (IOException ioe) {
            throw new cwException(" Error in read file: " + filePathAbs);
        }

        return wordsVec;
    }

    /**
     * validate whether content string has filter words.
     * 
     * @param contentStr
     * @param filterWordsVec
     * @return
     */
    public static boolean isContainFilterWords(String contentStr,
            Vector filterWordsVec) {
        boolean isContain = false;
        String filterWord = "";
        int wordIndex = -1;

        if (contentStr != null) {
            for (Iterator iter = filterWordsVec.iterator(); iter.hasNext();) {
                filterWord = (String) iter.next();
                wordIndex = contentStr.indexOf(filterWord);
                if (wordIndex >= 0) {
                    isContain = true;
                    break;
                }
            }
        }

        return isContain;
    }

}

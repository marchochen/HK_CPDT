/**
 * A parser used to extract data out of a PDF document.
 *
 * Copyright (C) 2002 Ben Litchfield
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * http://www.pdfbox.org
 *
 */
 
package com.cw.wizbank.search.document;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.cos.PDFDocument;
import org.pdfbox.util.PDFTextStripper;

import com.cwn.wizbank.utils.CommonLog;

/**
 * This is the main program that simply parses the pdf document and transforms it
 * into text.
 *
 * <BR/><BR/>
 * Usage: java pdfparser.Main &lt;input-pdf-document&gt; &lt;output-text-file&gt;
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.6 $
 */
public class ReadPDF
{
    /**
     * Infamous main method.
     *
     * @param args Command line arguments, should be one and a reference to a file.
     *
     * @throws IOException If there is an error parsing the document.
     */
     public ReadPDF() {
     }
     
     public String getText(String path) {
        InputStream input = null;
        ByteArrayOutputStream output = null;
        output = new ByteArrayOutputStream();
        try
        {
            input = new FileInputStream( path );
            PDFDocument document = parseDocument( input );

            //document.print();
            if( !document.isEncrypted() )
            {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.writeText( document, output );
                //System.out.println("output:\n" + output.toString());
            }
            else
            {
                System.err.println( "Error: Encrypted documents are not supported" );
            }
        }
        catch (Exception e){
        	CommonLog.error(e.getMessage(),e);
        }
        finally
        {
            try {
                if( input != null )
                {                    
                    input.close();
                }
                if( output != null )
                {
                    output.close();
                }
            } catch (Exception e) {
            	CommonLog.error(e.getMessage(),e);
            }
        }
        
        return output.toString();
     }
     
    private static PDFDocument parseDocument( InputStream input )throws IOException
    {
        PDFParser parser = new PDFParser( input );
        parser.parse();
        return parser.getDocument();
    }

    /**
     * This will print the usage requirements and exit.
     */
    private static void usage()
    {
        System.err.println( "Usage: java pdfparser.Main <PDF file> <Text File>" );
    }
    
    public static void main( String[] args ) throws IOException
    {
        if( args.length != 1 )
        {
            usage();
        }
        else
        {
            ReadPDF myReadPDF = new ReadPDF();
            CommonLog.debug(myReadPDF.getText(args[0]));
        }
    }

}
 
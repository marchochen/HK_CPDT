package com.cw.wizbank.scorm.util;

import java.net.*;
import java.io.*;

import com.cw.wizbank.scorm.util.DebugIndicator;
import com.cwn.wizbank.utils.CommonLog;


/**
 * This class provides a method of posting multiple serialized objects to a
 * Java servlet and getting objects in return.
 */
public class ServletWriter
{

   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;


   /**
    * Provides a means to 'POST' multiple serialized objects to a servlet.
    *
    * @param iServlet   The URL of the target servlet.
    *
    * @param iObjs      A list of objects to be serialized during the POST.
    *
    * @return A stream of serialized objects.
    * @exception Exception
    */
   static public ObjectInputStream postObjects(URL iServlet,
                                               Serializable iObjs[])
                                               throws Exception
 {

		if (_Debug) {
			CommonLog.debug("In ServletWriter::postObjects()");
		}

		URLConnection con = null;

		try {
			if (_Debug) {
				CommonLog.debug("Opening HTTP URL connection to " + "servlet.");
			}

			con = iServlet.openConnection();
		} catch (Exception e) {
			CommonLog.error("e = 1");

			if (_Debug) {
				CommonLog.debug("Exception caught in " + "ServletWriter::postObjects()");
			}

			CommonLog.error(e.getMessage(),e);
			throw e;
		}

		if (_Debug) {
			CommonLog.debug("HTTP connection to servlet is open");
			CommonLog.debug("configuring HTTP connection properties");
		}

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty("Content-Type", "text/plain");
		con.setAllowUserInteraction(false);

		// Write the arguments as post data
		ObjectOutputStream out = null;

		try {
			if (_Debug) {
				CommonLog.debug("Creating new http output stream");
			}

			out = new ObjectOutputStream(con.getOutputStream());

			if (_Debug) {
				CommonLog.debug("Created new http output stream.");
				CommonLog.debug("Writing command and data to servlet...");
			}

			int numObjects = iObjs.length;

			if (_Debug) {
				CommonLog.debug("Num objects: " + numObjects);
			}

			for (int i = 0; i < numObjects; i++) {
				out.writeObject(iObjs[i]);

				if (_Debug) {
					CommonLog.debug("Just wrote a serialized object on " + "output stream... " + iObjs[i].getClass().getName());
				}
			}
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Exception caught in " + "ServletWriter::postObjects()");
				CommonLog.debug(e.getMessage());
			}

			CommonLog.error(e.getMessage(),e);
			throw e;
		}

		try {
			if (_Debug) {
				CommonLog.debug("Flushing Object Output Stream.");
			}
			out.flush();
		} catch (IOException ioe) {
			if (_Debug) {
				CommonLog.debug("Caught IOException when calling " + "out.flush()");
				CommonLog.debug(ioe.getMessage());
			}
			CommonLog.error(ioe.getMessage(),ioe);
			throw ioe;
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Caught Exception when calling " + "out.flush()");
				CommonLog.debug(e.getMessage());
			}
			CommonLog.error(e.getMessage(),e);
			throw e;
		}

		try {
			if (_Debug) {
				CommonLog.debug("Closing object output stream.");
			}
			out.close();
		} catch (IOException ioe) {
			if (_Debug) {
				CommonLog.debug("Caught IOException when calling " + "out.close()");
				CommonLog.debug(ioe.getMessage());
			}

			CommonLog.error(ioe.getMessage(),ioe);
			throw ioe;
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Caught Exception when calling " + "out.close()");
				CommonLog.debug(e.getMessage());
			}

			CommonLog.error(e.getMessage(),e);
			throw e;
		}

		ObjectInputStream in;

		try {
			if (_Debug) {
				CommonLog.debug("Creating new http input stream.");
			}

			in = new ObjectInputStream(con.getInputStream());
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Exception caught in " + "ServletWriter::postObjects()");
				CommonLog.debug(e.getMessage());
			}
			CommonLog.error(e.getMessage(),e);
			throw e;
		}

		return in;
	}

}

package com.cw.wizbank.scorm.util;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;

import com.cw.wizbank.scorm.util.CMIRequest;
import com.cw.wizbank.scorm.util.CMIResponse;
import com.cwn.wizbank.utils.CommonLog;

/**
 * This class encapsulates communication between the API Adapter applet and
 * the <code>CMI</code>.
 */
public class ServletProxy
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * The URL of the target servlet.
    */
   private URL mServletURL = null;

   /**
    * Constructor
    *
    * @param iURL  The URL of the target servlet.
    */
   public ServletProxy(URL iURL)
   {
      mServletURL = iURL;
   }

   /**
    * Reads from the LMS server via the <code>LMSCMIServlet</code>; the
    * <code>SCODataManager</code> object containing all of the run-time data
    *  model elements relevant for the current user (student) and current SCO.
    *
    * @param iRequest A <code>LMSCMIServletRequest</code> object that
    *                 provides all the data neccessary to POST a call to
    *                 the <code>LMSCMIServlet</code>.
    *
    * @return The <code>LMSCMIServletResponse</code> object provided by the
    *         <code>LMSCMIServlet</code>.
    */
   public CMIResponse postLMSRequest(CMIRequest iRequest)
 {

		if (_Debug) {
			CommonLog.debug("In ServletProxy::postLMSRequest()");
		}

		CMIResponse response = new CMIResponse();

		try {
			if (_Debug) {
				CommonLog.debug("In ServletProxy::postLMSRequest()");
			}

			Serializable[] data = { iRequest };

			if (_Debug) {
				CommonLog.debug("Before postObjects()");
			}

			ObjectInputStream in = ServletWriter.postObjects(mServletURL, data);

			if (_Debug) {
				CommonLog.debug("Back In ServletProxy::postLMSRequest()");
				CommonLog.debug("Attempting to read servlet " + "response now...");
			}

			response = (CMIResponse) in.readObject();

			in.close();
			response.mError = "OK";
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Exception caught in " + "ServletProxy::postLMSRequest()");
				CommonLog.debug(e.getMessage());
			}

			CommonLog.error(e.getMessage(),e);
			response.mError = "FAILED";
		}

		return response;
	}
}

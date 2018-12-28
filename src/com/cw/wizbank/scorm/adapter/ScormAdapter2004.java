package com.cw.wizbank.scorm.adapter;

import java.applet.Applet;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import com.cw.wizbank.scorm.util.APIErrorCodes;
import com.cw.wizbank.scorm.util.APIErrorManager;
import com.cw.wizbank.scorm.adapter.ISCORM2004API;
import com.cw.wizbank.scorm.dm.DMErrorCodes;
import com.cw.wizbank.scorm.dm.DMInterface;
import com.cw.wizbank.scorm.dm.DMProcessingInfo;
import com.cw.wizbank.scorm.dm.SCODataManager;
import com.cw.wizbank.scorm.util.DebugIndicator;
import com.cw.wizbank.scorm.util.CMIRequest;
import com.cw.wizbank.scorm.util.CMIResponse;
import com.cw.wizbank.scorm.util.ServletProxy;
import com.cwn.wizbank.utils.CommonLog;

import netscape.javascript.JSObject;

/**
 * 
 * This class implements the Sharable Content Object Reference Model (SCORM)
 * Version 2004 Sharable Content Object (SCO) to Learning Management System
 * (LMS) communication API.
 */
public class ScormAdapter2004 extends Applet implements ISCORM2004API {
	/**
	 * The public version attribute of the SCORM API.
	 */
	public static final String version = "1.0";

	/**
	 * String value for the cmi.completion_status data model element.
	 */
	private static final String COMPLETION_STATUS = "cmi.completion_status";

	/**
	 * String value for the cmi.success_status data model element.
	 */
	private static final String SUCCESS_STATUS = "cmi.success_status";

	/**
	 * String value for the cmi.completion_threshold data model element.
	 */
	private static final String COMPLETION_THRESHOLD = "cmi.completion_threshold";

	/**
	 * String value for the cmi.progress_measure data model element.
	 */
	private static final String PROGRESS_MEASURE = "cmi.progress_measure";

	/**
	 * String value for the cmi.scaled_passing_score data model element.
	 */
	private static final String SCALED_PASSING_SCORE = "cmi.scaled_passing_score";

	/**
	 * String value for the cmi.scaled_score data model element.
	 */
	private static final String SCORE_SCALED = "cmi.score.scaled";

	/**
	 * String value of FALSE for JavaScript returns.
	 */
	private static final String STRING_FALSE = "false";

	/**
	 * String value of TRUE for JavaScript returns.
	 */
	private static final String STRING_TRUE = "true";

	/**
	 * Constant representing the error code for no error.
	 */
	private static final int NO_ERROR = 0;

	/**
	 * This controls display of log messages to the java console.
	 */
	private static boolean _Debug = DebugIndicator.ON;

	/**
	 * Indicates if the SCO is in an 'initialized' state.
	 */
	private static boolean mInitializedState = false;

	/**
	 * Public flag for API and Datamodel Logging.
	 */
	public boolean logging_on = false;

	/**
	 * Provides all LMS Error reporting.
	 */
	private APIErrorManager mLMSErrorManager = null;

	/**
	 * The current run-time data model values.
	 */
	private SCODataManager mSCOData = null;

	/**
	 * Indicates if the SCO is in a 'terminated' state.
	 */
	private boolean mTerminatedState = false;

	/**
	 * Indicates if the SCO is in a 'terminated' state.
	 */
	private boolean mTerminateCalled = false;

	/**
	 * URL to the location of the <code>LMSCMIServlet</code>.
	 */
	private URL mServletURL = null;

	/**
	 * ID of the activity associated with the currently launched content.
	 */
	private String mActivityID = null;

	/**
	 * ID of the activity's associated run-time data.
	 */
	private String mStateID = null;

	/**
	 * ID of the student experiencing the currently launched content.
	 */
	private String mUserID = null;

	/**
	 * Name of the student experiencing the currently launched content.
	 */
	private String mUserName = null;

	/**
	 * ID of the course of which the currently experienced activity is a part.
	 */
	private String mCourseID = null;

	/**
	 * Indicates number of the current attempt.
	 */
	private long mNumAttempts = 0L;

	/**
	 * Indicates if the current SCO is SCORM 2004 4th Edition Version 1.0.
	 */
	private boolean mScoVer2 = false;

	/**
	 * Indicates if the Suspend button was pushed.
	 */
	private boolean mLMSSuspendAllPushed = false;

	/**
	 * Indicates of the Quit button was pushed.
	 */
	private boolean mQuitButtonPushed = false;

	/**
	 * Indicates of the Previous button was pushed.
	 */
	private boolean mPreviousButtonPushed = false;

	/**
	 * Indicates of the Next button was pushed.
	 */
	private boolean mNextButtonPushed = false;

	/**
	 * Indicates that an item in the Table of Contents was clicked.
	 */
	private boolean mTOCPushed = false;

	private String mUserNavRequest = "_none_";

	private String session_id = "";

	/**
	 * Indicates of mSCOData was modified.
	 */
	private boolean dirty = false;

	/**
	 * Initializes the applet's state.
	 */
	public void init() {
		setBackground(new Color(0, 0, 0));
		if (_Debug) {
			CommonLog.debug("In API::init()(the applet Init method)");
		}

		mTerminatedState = false;
		mInitializedState = false;
		mTerminateCalled = false;
		mTOCPushed = false;

		mLMSErrorManager = new APIErrorManager(APIErrorManager.SCORM_2004_API);

		mScoVer2 = false;

		dirty = false;
		URL codebase = getCodeBase();
		String host = codebase.getHost();
		String protocol = codebase.getProtocol();
		int port = codebase.getPort();

		if (_Debug) {
			CommonLog.debug("ScormAdapter -> codebase url is " + codebase.getPath().toString());
		}

		try {
			mServletURL = new URL(protocol + "://" + host + ":" + port + "/CMI?scover=2004");

			if (_Debug) {
				CommonLog.debug("servlet url is " + mServletURL.toString());
			}
		} catch (Exception e) {
			if (_Debug) {
				CommonLog.debug("Error in INIT");
			}
			CommonLog.error(e.getMessage(),e);
			stop();
		}

	}

	/**
	 * Provides a string describing the the API applet class.
	 * 
	 * @return API Applet information string.
	 */
	public String getAppletInfo() {
		return "Title: wizBank Scorm2004 adapter.";
	}

	/**
	 * Provides information about this applet's parameters.
	 * 
	 * @return String containing information about the applet's parameters.
	 */
	public String[][] getParameterInfo() {
		String[][] info = { { "None", "", "no parameters." } };

		return info;
	}

	/**
	 * Confirms that the communication session has been initialized (
	 * <code>LMSInitialize </code> or <code>Initialize</code> has been called).
	 * 
	 * @return <code>true</code> if <code>LMSInitialize</code> or
	 *         <code>Initialize</code> has been called otherwise
	 *         <code>false</code>.
	 */
	private boolean isInitialized() {
		if ((!mInitializedState) && (mScoVer2)) {

			mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_GET_FAILURE);
		}

		return mInitializedState;
	}

	/**
	 * Initiates the communication session.
	 * 
	 * @param iParam
	 *            ("") - empty characterstring. An empty characterstring shall
	 *            be passed as a parameter.
	 * 
	 * @return The function can return one of two values. The return value shall
	 *         be represented as a characterstring.<br>
	 *         <ul>
	 *         <li><code>true</code> - The characterstring "true" shall be
	 *         returned if communication session initialization, as determined
	 *         by the LMS, was successful.</li>
	 *         <li><code>false</code> - The characterstring "false" shall be
	 *         returned if communication session initialization, as determined
	 *         by the LMS, was unsuccessful.</li>
	 *         </ul>
	 */
	public String Initialize(String iParam) {
		if (_Debug) {
			CommonLog.debug("*********************");
			CommonLog.debug("In API::Initialize");
			CommonLog.debug("*********************");
		}
		String paramVal = new String();
		String evalVal = new String();
		if (logging_on) {
			paramVal = "Called Initialize ";
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		// Assume failure
		String result = STRING_FALSE;

		if (mTerminatedState) {
			if (_Debug) {
				CommonLog.debug("mTerminatedState = " + mTerminatedState);
			}

			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.CONTENT_INSTANCE_TERMINATED);
			if (logging_on) {

				paramVal = "Initialize Returned Error Code " + APIErrorCodes.CONTENT_INSTANCE_TERMINATED;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}

			return result;
		}

		mTerminatedState = false;
		mTerminateCalled = false;

		mScoVer2 = false;

		// Make sure param is empty string "" - as per the API spec
		// Check for "null" is a workaround described in "Known Problems"
		// in the header.
		String tempParm = String.valueOf(iParam);

		if ((tempParm == null || tempParm.equals("")) != true) {
			mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
		}

		// If the SCO is already initialized set the appropriate error code
		else if (mInitializedState) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.ALREADY_INITIALIZED);
		} else {

			mLMSSuspendAllPushed = false;
			mQuitButtonPushed = false;
			mPreviousButtonPushed = false;
			mNextButtonPushed = false;
			mTOCPushed = false;
			mUserNavRequest = "_none_";

			CMIRequest request = new CMIRequest();

			// Build the local LMSCMIServlet Request Object to serialize
			// across the socket
			request.aicc_sid = session_id;
			request.mRequestType = CMIRequest.TYPE_INIT;

			Long longObj = new Long(mNumAttempts);
			request.mNumAttempt = longObj.toString();

			if (_Debug) {
				CommonLog.debug("Trying to get SCO Data from servlet...");
				CommonLog.debug("LMSCMIServlet Request contains: ");
			}

			String query = "&session_id=" + session_id;

			ServletProxy proxy = new ServletProxy(mServletURL);

			CMIResponse response = proxy.postLMSRequest(request);

			// Get the SCODataManager from the servlet response object
			mSCOData = response.mActivityData;

			mInitializedState = true;

			// No errors were detected
			mLMSErrorManager.clearCurrentErrorCode();
			result = STRING_TRUE;

		}
		if (logging_on) {
			if (_Debug) {
				CommonLog.debug("logging_on = " + logging_on);
			}

			paramVal = "Initialize Returned Error Code " + mLMSErrorManager.getCurrentErrorCode();
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		if (_Debug) {
			CommonLog.debug("*******************************");
			CommonLog.debug("Done Processing Initialize()");
			CommonLog.debug("*******************************");
		}
		if (_Debug) {
			CommonLog.debug("result to return is: " + result);
		}
		return result;
	}

	/**
	 * Terminates the communication session.
	 * 
	 * @param iParam
	 *            ("") - empty characterstring. An empty characterstring shall
	 *            be passed as a parameter.
	 * 
	 * @return The method can return one of two values. The return value shall
	 *         be represented as a characterstring.
	 *         <ul>
	 *         <li><code>true</code> - The characterstring "true" shall be
	 *         returned if termination of the communication session, as
	 *         determined by the LMS, was successful.</li>
	 *         <li><code>false</code> - The characterstring "false" shall be
	 *         returned if termination of the communication session, as
	 *         determined by the LMS, was unsuccessful.
	 *         </ul>
	 */
	public String Terminate(String iParam) {
		if (_Debug) {
			CommonLog.debug("*****************");
			CommonLog.debug("In API::Terminate");
			CommonLog.debug("*****************");
		}
		String paramVal = new String();
		String evalVal = new String();
		if (logging_on) {
			paramVal = "Called Terminate ";
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}
		mTerminateCalled = true;
		// Assume failure
		String result = STRING_FALSE;

		// already terminated
		if (mTerminatedState) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.TERMINATE_AFTER_TERMINATE);
			if (logging_on) {
				paramVal = "Terminate Returned Error Code " + APIErrorCodes.TERMINATE_AFTER_TERMINATE;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}
			return result;
		}
		if (!isInitialized()) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.TERMINATE_BEFORE_INIT);
			if (logging_on) {
				paramVal = "Terminate Returned Error Code " + APIErrorCodes.TERMINATE_BEFORE_INIT;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}

			return result;
		}

		// Make sure param is empty string "" - as per the API spec
		// Check for "null" is a workaround described in "Known Problems"
		// in the header.
		String tempParm = String.valueOf(iParam);
		if ((tempParm == null || tempParm.equals("")) != true)

		{
			mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
		} else {

			if ("VIEWONLY".equalsIgnoreCase(session_id) || !dirty) {
				result = STRING_TRUE;
			} else {
				result = Commit("");
			}

			mTerminatedState = true;

			if (!result.equals(STRING_TRUE)) {
				if (_Debug) {
					CommonLog.debug("Commit failed causing " + "Terminate to fail.");
				}
				// General Commit Failure
				mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GENERAL_COMMIT_FAILURE);
			} else {
				mInitializedState = false;

				// get value of "exit"
				DMProcessingInfo dmInfo = new DMProcessingInfo();
				int dmErrorCode = 0;
				dmErrorCode = DMInterface.processGetValue("cmi.exit", true, true, mSCOData, dmInfo);
				String exitValue = dmInfo.mValue;
				String tempEvent = "_none_";
				boolean isChoice = false;
				boolean isJump = false;
				String evalValue = null;

				if (dmErrorCode == APIErrorCodes.NO_ERROR) {
					exitValue = dmInfo.mValue;
				} else {
					exitValue = new String("");
				}

				if (exitValue.equals("time-out")) {
					tempEvent = "exitAll";
				} else if (exitValue.equals("logout")) {
					tempEvent = "exitAll";
				}
			}
		}

		if (logging_on) {
			paramVal = "Terminate Returned Error Code " + mLMSErrorManager.getCurrentErrorCode();
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		if (_Debug) {
			CommonLog.debug("***************************");
			CommonLog.debug("Done Processing Terminate()");
			CommonLog.debug("***************************");
		}
		return result;
	}

	/**
	 * Insert a backward slash (\) before each double quote (") or backslash (\)
	 * to allow the character to be displayed in the data model log. Receives
	 * the value and returns the newly formatted value
	 */
	public String formatValue(String baseString) {
		int indexQuote = baseString.indexOf("\"");
		int indexSlash = baseString.indexOf("\\");

		// Escape line return characters
		baseString = baseString.replaceAll("\r\n", " ");
		baseString = baseString.replaceAll("\n", " ");
		baseString = baseString.replaceAll("\r", " ");
		baseString = baseString.replaceAll("\\r\\n", " ");
		baseString = baseString.replaceAll("\\n", " ");
		baseString = baseString.replaceAll("\\r", " ");

		if (indexQuote >= 0 || indexSlash >= 0) {
			int index = 0;
			String strFirst = new String();
			String strLast = new String();
			char insertValue = '\\';

			while (index < baseString.length()) {
				if ((baseString.charAt(index) == '\"') || (baseString.charAt(index) == '\\')) {
					strFirst = baseString.substring(0, index);
					strLast = baseString.substring(index, baseString.length());
					baseString = strFirst.concat(Character.toString(insertValue)).concat(strLast);
					index += 2;
				} else {
					index++;
				}
			}
		}
		return baseString;
	}

	/**
	 * The function requests information from an LMS. It permits the SCO to
	 * request information from the LMS to determine among other things:
	 * <ul>
	 * <li>Values for data model elements supported by the LMS.</li>
	 * <li>Version of the data model supported by the LMS.</li>
	 * <li>Whether or not specific data model elements are supported.</li>
	 * </ul>
	 * Retrieves the current value of the specified data model element for a
	 * SCORM 2004 4th Edition SCO. The values are locally cached except for
	 * nav.event_permitted, which requires a call to LMSCMIServlet to get the
	 * current value.<br>
	 * <br>
	 * 
	 * @param iDataModelElement
	 *            The parameter represents the complete identification of a data
	 *            model element within a data model.<br>
	 * <br>
	 * 
	 * @return The method can return one of two values. If there is not error,
	 *         the return value shall be represented as a characterstring
	 *         containing the value associated with the parameter. If an error
	 *         occurs, then the API Instance shall set an error code to a value
	 *         specific to the error and return an empty characterstring ("").
	 */
	public String GetValue(String iDataModelElement) {
		if (_Debug) {
			CommonLog.debug("*******************");
			CommonLog.debug("In API::GetValue");
			CommonLog.debug("*******************");
		}
		String paramVal = new String();
		String evalVal = new String();
		if (logging_on) {
			paramVal = "Called GetValue( " + iDataModelElement + ")";
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}
		String result = "";

		// already terminated
		if (mTerminatedState) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GET_AFTER_TERMINATE);
			if (logging_on) {
				paramVal = "GetValue Returned Error Code " + APIErrorCodes.GET_AFTER_TERMINATE;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}
			return result;
		}
		if (iDataModelElement.length() == 0) {
			mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_GET_FAILURE);
			if (logging_on) {
				paramVal = "GetValue Returned Error Code " + DMErrorCodes.GEN_GET_FAILURE;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}
			return result;
		}

		if (isInitialized()) {

			if (_Debug) {
				CommonLog.debug("Request being processed: GetValue(" + iDataModelElement + ")");
			}

			DMProcessingInfo dmInfo = new DMProcessingInfo();
			int dmErrorCode = 0;
			boolean done = false;

			// Clear current error codes
			mLMSErrorManager.clearCurrentErrorCode();

			// Compare for cmi.completion_status
			/*if (iDataModelElement.equals(COMPLETION_STATUS)) {
				double completionThreshold = 0.0;
				double progressMeasure = 0.0;
				dmErrorCode = DMInterface.processGetValue(COMPLETION_THRESHOLD, false, mSCOData, dmInfo);
				if (dmErrorCode != DMErrorCodes.NOT_INITIALIZED) {
					done = true;

					completionThreshold = Double.parseDouble(dmInfo.mValue);
					dmErrorCode = DMInterface.processGetValue(PROGRESS_MEASURE, false, mSCOData, dmInfo);
					if (dmErrorCode != DMErrorCodes.NOT_INITIALIZED) {
						progressMeasure = Double.parseDouble(dmInfo.mValue);
						if (progressMeasure >= completionThreshold) {
							result = "completed";
						} else {
							result = "incomplete";
						}
					} else {
						result = "unknown";
					}

					dmErrorCode = APIErrorCodes.NO_ERROR;
				}
			}*/
			// Compare for cmi.success_status
			/*if (iDataModelElement.equals(SUCCESS_STATUS)) {
				double scaledPassingScore = 0.0;
				double scoreScaled = 0.0;
				dmErrorCode = DMInterface.processGetValue(SCALED_PASSING_SCORE, false, mSCOData, dmInfo);
				if (dmErrorCode != DMErrorCodes.NOT_INITIALIZED) {
					done = true;
					scaledPassingScore = Double.parseDouble(dmInfo.mValue);
					dmErrorCode = DMInterface.processGetValue(SCORE_SCALED, false, mSCOData, dmInfo);
					if (dmErrorCode != DMErrorCodes.NOT_INITIALIZED) {
						scoreScaled = Double.parseDouble(dmInfo.mValue);
						if (scoreScaled >= scaledPassingScore) {
							result = "passed";
						} else {
							result = "failed";
						}
					} else {
						result = "unknown";
					}

					dmErrorCode = APIErrorCodes.NO_ERROR;
				}
			}*/

			if (!done) {
				// Process 'GET'
				dmErrorCode = DMInterface.processGetValue(iDataModelElement, false, mSCOData, dmInfo);

				if (dmErrorCode == APIErrorCodes.NO_ERROR) {
					result = dmInfo.mValue;
				}
			}

			// Set the LMS Error Manager from the Data Model Error Manager
			mLMSErrorManager.setCurrentErrorCode(dmErrorCode);

			if (dmErrorCode == APIErrorCodes.NO_ERROR) {
				if (_Debug) {
					CommonLog.debug("GetValue() found!");
					CommonLog.debug("Returning: " + dmInfo.mValue);
				}
			} else {
				if (_Debug) {
					CommonLog.debug("Found the element, but the value was null");
				}
				result = new String("");
			}

			if (logging_on) {
				paramVal = "GetValue Returned the value " + formatValue(result);
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}

		}
		// not initialized
		else {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GET_BEFORE_INIT);
		}

		if (_Debug) {
			CommonLog.debug("************************************");
			CommonLog.debug("Processing done for API::LMSGetValue");
			CommonLog.debug("************************************");
		}
		if (logging_on) {

			paramVal = "GetValue Returned Error Code " + mLMSErrorManager.getCurrentErrorCode();
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		return result;
	}

	/**
	 * Request the transfer to the LMS of the value of iValue for the data
	 * element specified as iDataModelElement. This method allows the SCO to
	 * send information to the LMS for storage. Used by SCORM 2004 4th Edition
	 * SCOs. The values are locally cached until <code>Commit()</code> or
	 * <code>Terminate()</code> is called.
	 * 
	 * @param iDataModelElement
	 *            - The complete identification of a data model element within a
	 *            data model to be set.
	 * @param iValue
	 *            - The intended value of the CMI or Navigation datamodel
	 *            element. The value shall be a characterstring that shall be
	 *            convertible to the data type defined for the data model
	 *            element identified in iDataModelElement.<br>
	 * <br>
	 * 
	 * @return The method can return one of two values. The return value shall
	 *         be represented as a characterstring.
	 *         <ul>
	 *         <li><code>true</code> - The characterstring "true" shall be
	 *         returned if the LMS accepts the content of iValue to set the
	 *         value of iDataModelElement.</li>
	 *         <li><code>false</code> - The characterstring "false" shall be
	 *         returned if the LMS encounters an error in setting the contents
	 *         of iDataModelElement with the value of iValue.</li>
	 *         </ul>
	 */

	public String SetValue(String iDataModelElement, String iValue) {
		// Assume failure
		String result = STRING_FALSE;

		if (_Debug) {
			CommonLog.debug("*******************");
			CommonLog.debug("In API::SetValue");
			CommonLog.debug("*******************");
		}
		String paramVal = new String();
		String evalVal = new String();

		if (logging_on) {
			paramVal = "Called SetValue(" + iDataModelElement + ", " + formatValue(iValue) + ")";
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		// already terminated
		if (mTerminatedState) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.SET_AFTER_TERMINATE);
			if (logging_on) {
				paramVal = "SetValue Returned Error Code " + APIErrorCodes.SET_AFTER_TERMINATE;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}

			return result;
		}

		// Clear any existing error codes
		mLMSErrorManager.clearCurrentErrorCode();

		if (!isInitialized()) {
			// not initialized
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.SET_BEFORE_INIT);
			if (logging_on) {
				paramVal = "SetValue Returned Error Code " + APIErrorCodes.SET_BEFORE_INIT;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}
			return result;
		}

		// Check for "null" is a workaround described in "Known Problems"
		// in the header.
		String setValue = (iValue == null) ? "" : String.valueOf(iValue);

		if (_Debug) {
			// Construct the request
			CommonLog.debug("Request being processed: SetValue(" + iDataModelElement + "," + setValue + ")");
			CommonLog.debug("Looking for the element " + iValue);
		}

		// Send off
		// Process 'SET'
		int dmErrorCode = DMInterface.processSetValue(iDataModelElement, setValue, false, mSCOData, true);

		// Set the LMS Error Manager from the DataModel Manager
		mLMSErrorManager.setCurrentErrorCode(dmErrorCode);

		result = Boolean.toString(dmErrorCode == NO_ERROR);


		if (logging_on) {
			paramVal = "SetValue Returned Error Code " + mLMSErrorManager.getCurrentErrorCode();
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}
		if (_Debug) {
			CommonLog.debug("************************************");
			CommonLog.debug("Processing done for API::SetValue");
			CommonLog.debug("************************************");
		}

		dirty = true;
		return result;
	}

	/**
	 * Toggles the state of the LMS provided UI controls.
	 * 
	 * @param iState
	 *            <code>true</code> if the controls should be enabled, or
	 *            <code>false</code> if the controls should be disabled.
	 */
	private void setUIState(boolean iState) {
//		if (_Debug) {
//			System.out.println(" ::Toggling UI State::-> " + iState);
//		}

//		String evalCmd = "setUIState(" + iState + ");";
//		jsCall(evalCmd);
	}

	/**
	 * Requests forwarding to the persistent data store any data from the SCO
	 * that may have been cached by the API Implementation since the last call
	 * to <code>Initialize()</code> or <code>Commit()</code>, whichever occurred
	 * most recently. Used by SCORM 2004 3rd Edition and later SCOs.
	 * 
	 * @param iParam
	 *            ("") - empty characterstring. An empty characterstring shall
	 *            be passed as a parameter.<br>
	 * <br>
	 * 
	 * @return The method can return one of two values. The return value shall
	 *         be represented as a characterstring.
	 *         <ul>
	 *         <li><code>true</code> - The characterstring "true" shall be
	 *         returned if the data was successfully persisted to a long-term
	 *         data store.</li>
	 *         <li><code>false</code> - The characterstring "false" shall be
	 *         returned if the data was unsuccessfully persisted to a long-term
	 *         data store.</li>
	 *         </ul>
	 * 
	 *         The API Instance shall set the error code to a value specific to
	 *         the error encountered.
	 */
	public String Commit(String iParam) {
		if (_Debug) {
			CommonLog.debug("*************************");
			CommonLog.debug("Processing API::Commit");
			CommonLog.debug("*************************");
		}

		String paramVal = new String();
		String evalVal = new String();
		if (logging_on) {
			paramVal = "Called Commit";
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}
		// Assume failure
		String result = STRING_FALSE;

		// already terminated
		if (mTerminatedState) {
			mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_AFTER_TERMINATE);
			if (logging_on) {
				paramVal = "Commit Returned Error Code " + APIErrorCodes.COMMIT_AFTER_TERMINATE;
				evalVal = "display_log(\"" + paramVal + "\");";
				jsCall(evalVal);
			}
			return result;
		}

		// Make sure param is empty string "" - as per the API spec
		// Check for "null" is a workaround described in "Known Problems"
		// in the header.
		String tempParm = String.valueOf(iParam);

		if ((tempParm == null || tempParm.equals("")) != true) {
			mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
		} else {
			if (!isInitialized()) {
				// LMS is not initialized
				mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_BEFORE_INIT);
				if (logging_on) {
					paramVal = "Commit Returned Error Code " + APIErrorCodes.COMMIT_BEFORE_INIT;
					evalVal = "display_log(\"" + paramVal + "\");";
					jsCall(evalVal);
				}
				return result;
			} else if (mTerminatedState) {
				// LMS is terminated
				mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_AFTER_TERMINATE);
				if (logging_on) {
					paramVal = "Commit Returned Error Code " + APIErrorCodes.COMMIT_AFTER_TERMINATE;
					evalVal = "display_log(\"" + paramVal + "\");";
					jsCall(evalVal);
				}
				return result;
			} else {
				// Prepare the request before it goes across the socket
				CMIRequest request = new CMIRequest();

				request.mActivityData = mSCOData;
				request.mIsFinished = mTerminateCalled;
				request.mRequestType = CMIRequest.TYPE_SET;
				request.aicc_sid = session_id;
				Long longObj = new Long(mNumAttempts);
				request.mNumAttempt = longObj.toString();

				request.mQuitPushed = mQuitButtonPushed;
				request.mSuspendPushed = mLMSSuspendAllPushed;

				ServletProxy proxy = new ServletProxy(mServletURL);

				CMIResponse response = proxy.postLMSRequest(request);

				if (!response.mError.equals("OK")) {

					mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GENERAL_EXCEPTION);

					if (_Debug) {
						CommonLog.debug("'SET' to server was NOT successful!");
					}
				} else {
					mLMSErrorManager.clearCurrentErrorCode();

					result = STRING_TRUE;

					if (_Debug) {
						CommonLog.debug("'SET' to server succeeded!");
					}
				}

			}
		}

		if (logging_on) {
			paramVal = " Commit Returned Error Code " + mLMSErrorManager.getCurrentErrorCode();
			evalVal = "display_log(\"" + paramVal + "\");";
			jsCall(evalVal);
		}

		if (_Debug) {
			CommonLog.debug("**********************************");
			CommonLog.debug("Processing done for API::Commit");
			CommonLog.debug("**********************************");
		}

		dirty = false;
		return result;
	}

	/**
	 * /** This method requests the error code for the current error state of
	 * the API Instance. Used by SCORM 2004 4th Edition SCOs.
	 * 
	 * <br>
	 * <br>
	 * NOTE: Session and Data-Transfer API functions set or clear the error
	 * code.<br>
	 * <br>
	 * 
	 * @return The API Instance shall return the error code reflecting the
	 *         current error state of the API Instance. The return value shall
	 *         be a characterstring (convertible to an integer in the range from
	 *         0 to 65536 inclusive) representing the error code of the last
	 *         error encountered.
	 */
	public String GetLastError() {
		if (_Debug) {
			CommonLog.debug("In API::GetLastError()");
		}
		String paramVal = "Called GetLastError() ";
		String evalVal = "display_log(\"" + paramVal + "\");";
		if (logging_on) {
			jsCall(evalVal);
		}
		paramVal = "GetLastError() Returned  " + mLMSErrorManager.getCurrentErrorCode();
		evalVal = "display_log(\"" + paramVal + "\");";
		if (_Debug) {
			CommonLog.debug(paramVal);
		}
		if (logging_on) {
			jsCall(evalVal);
		}

		return mLMSErrorManager.getCurrentErrorCode();
	}

	/**
	 * The GetErrorString() function can be used to retrieve a textual
	 * description of the current error state. The function is used by a SCO to
	 * request the textual description for the error code specified by the value
	 * of the parameter. This call has no effect on the current error state; it
	 * simply returns the requested information. Used by SCORM 2004 4th Edition
	 * SCOs.
	 * 
	 * @param iErrorCode
	 *            Represents the characterstring of the error code (integer
	 *            value) corresponding to an error message.<br>
	 * <br>
	 * 
	 * @return The method shall return a textual message containing a
	 *         description of the error code specified by the value of the
	 *         parameter. The following requirements shall be adhered to for all
	 *         return values:
	 *         <ul>
	 *         <li>The return value shall be a characterstring that has a
	 *         maximum length of 256 bytes (including null terminator).</li>
	 *         <li>The SCORM makes no requirement on what the text of the
	 *         characterstring shall contain. The error codes themselves are
	 *         explicitly and exclusively defined. The textual description for
	 *         the error code is LMS specific.</li>
	 *         <li>If the requested error code is unknown by the LMS, an empty
	 *         characterstring ("") shall be returned This is the only time that
	 *         an empty characterstring shall be returned.</li>
	 *         </ul>
	 */
	public String GetErrorString(String iErrorCode) {
		if (_Debug) {
			CommonLog.debug("In API::GetErrorString()");
		}
		String paramVal = "Called GetErrorString(" + iErrorCode + ") ";
		String evalVal = "display_log(\"" + paramVal + "\");";
		if (logging_on) {
			jsCall(evalVal);
		}

		String errorCode = mLMSErrorManager.getErrorDescription(iErrorCode);
		paramVal = "GetErrorString Returned " + errorCode;
		evalVal = "display_log(\"" + paramVal + "\");";
		if (logging_on) {
			jsCall(evalVal);
		}
		if (_Debug) {
			CommonLog.debug(paramVal);
		}
		return errorCode;
	}

	/**
	 * The GetDiagnostic() function exists for LMS specific use. It allows the
	 * LMS to define additional diagnostic information through the API Instance.
	 * This call has no effect on the current error state; it simply returns the
	 * requested information. Used by SCORM 2004 4th Edition SCOs.
	 * 
	 * @param iErrorCode
	 *            An implementer-specific value for diagnostics. The maximum
	 *            length of the parameter value shall be 256 bytes (including
	 *            null terminator). The value of the parameter may be an error
	 *            code, but is not limited to just error codes.<br>
	 * <br>
	 * 
	 * @return The API Instance shall return a characterstring representing the
	 *         diagnostic information. The maximum length of the characterstring
	 *         returned shall be 256 bytes (including null terminator).
	 */
	public String GetDiagnostic(String iErrorCode) {
		if (_Debug) {
			CommonLog.debug("In API::GetDiagnostic()");
		}

		String paramVal = "Called GetDiagnostic(" + iErrorCode + ") ";
		String evalVal = "display_log(\"" + paramVal + "\");";
		if (logging_on) {
			jsCall(evalVal);
		}

		String diagnostic = mLMSErrorManager.getErrorDiagnostic(iErrorCode);
		paramVal = "GetDiagnostic Returned " + diagnostic;
		evalVal = "display_log(\"" + paramVal + "\");";
		if (logging_on) {
			jsCall(evalVal);
		}

		if (_Debug) {
			CommonLog.debug(paramVal);
		}
		return diagnostic;
	}

	/**
	 * Sets the ID of the activity associated with the currently delivered
	 * content.
	 * 
	 * @param iActivityID
	 *            The activity ID.
	 */
	public void setActivityID(String iActivityID) {
		mActivityID = iActivityID;
	}

	/**
	 * Sets the ID of the course with which the currently launched content is
	 * associated.
	 * 
	 * @param iCourseID
	 *            The course ID.
	 */
	public void setCourseID(String iCourseID) {
		mCourseID = iCourseID;
	}

	/**
	 * Sets the ID of the run-time data state of the currently launched content.
	 * 
	 * @param iStateID
	 *            The run-time data state ID.
	 */
	public void setStateID(String iStateID) {
		mStateID = iStateID;
	}

	/**
	 * Sets the ID of the student experiencing the currently launched content.
	 * 
	 * @param iUserID
	 *            The student ID.
	 */
	public void setUserID(String iUserID) {
		mUserID = iUserID;
	}

	/**
	 * Sets the name of the student experiencing the currently launched content.
	 * 
	 * @param iUserName
	 *            The student Name.
	 */
	public void setUserName(String iUserName) {
		mUserName = iUserName;
	}

	/**
	 * Sets the number of the current attempt.
	 * 
	 * @param iNumAttempts
	 *            The number of the current attempt.
	 */
	public void setNumAttempts(long iNumAttempts) {
		mNumAttempts = iNumAttempts;
	}

	/**
	 * Sets the number of the current attemptfrom a String parameter.
	 * 
	 * @param iNumAttempts
	 *            The number of the current attempt.
	 */
	public void setNumAttempts(String iNumAttempts) {
		Long tempLong = new Long(iNumAttempts);
		mNumAttempts = tempLong.longValue();
	}

	/**
	 * Clears error codes and sets mInitialedState and mTerminated State to
	 * default values.
	 */
	public void clearState() {
		mInitializedState = false;
		mTerminatedState = false;
		mTerminateCalled = false;
		mLMSErrorManager.clearCurrentErrorCode();
	}

	/**
	 * @param message
	 *            The String that is evaluated by the Java Script eval
	 *            command--usually it is a Java Script function name.<br>
	 * <br>
	 * 
	 * 
	 */
	public void jsCall(String message) {
		JSObject.getWindow(this).eval(message);
	}

	/**
	 * This method indicates that the next button was pushed.
	 */
	public void TOCPushed(String iScoID) {
		if (isInitialized()) {
			mTOCPushed = true;
			int dmErrorCode = 0;
			mUserNavRequest = "{target=" + iScoID + "}choice";

			dmErrorCode = DMInterface.processSetValue("adl.nav.request", "_none_", true, mSCOData);
		}
	}

	/**
	 * This method resets the boolean value of the variable that controls
	 * logging of API and Datamodel calls.
	 */
	public void resetLoggingVariable() {
		logging_on = !logging_on;
	}

	/**
	 * Set the cmi URL that LMS used to tracking the SCO.
	 * 
	 * @param url
	 */
	public void setCmiURL(String url) {
		try {
			mServletURL = new URL(url + "scover=2004");
			CommonLog.debug("set cmi url : " + url + "scover=2004");
		} catch (MalformedURLException e) {
			CommonLog.error(e.getMessage(),e);
		}
	}

	public void setSessionId(String sess_id) {
		session_id = sess_id;
	}
}

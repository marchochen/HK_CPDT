package com.cw.wizbank.scorm.util;

import java.io.Serializable;
import java.util.Vector;

import com.cw.wizbank.scorm.dm.SCODataManager;

/**
 * This class contains the data that the <code>CMI</code> sends across
 * the socket to the <code>ScormAdapter2004</code>.<br>
 * <br>
 * 
 */
public class CMIResponse implements Serializable {

	private static final long serialVersionUID = -4519579043558506065L;

	/**
	 * The run-time data associated with the activity.
	 */
	public SCODataManager mActivityData = null;

	/**
	 * The state of the UI in relation to the current activity.
	 */
//	public ADLValidRequests mValidRequests = null;

	/**
	 * Indicates if an activity is avaliable for immediate delivery.
	 */
	public boolean mAvailableActivity = false;

	/**
	 * Provides time out tracking data for the LMS Client.
	 */
	public Vector mTimeoutTracking = null;

	/**
	 * Indicates if the user 'logged out'.
	 */
	public boolean mLogout = false;

	/**
	 * Indicates any error that occured while processing the request.
	 */
	public String mError = null;

	/**
	 * Indicates if control mode is flow.
	 */
	public boolean mFlow = true;

	/**
	 * Indicates if control mode is choice.
	 */
	public boolean mChoice = false;

	/**
	 * Indicates if control mode is autoadvance.
	 */
	public boolean mAuto = false;

	/**
	 * Default constructor
	 */
	public CMIResponse() {
	}
}

package com.cw.wizbank.scorm.util;

import java.io.Serializable;

import com.cw.wizbank.scorm.dm.SCODataManager;

/**
 * This class contains the data that the <code>ScormAdapter2004</code> needs
 * to send across the socket to the <code>LMSCMIServlet</code>.<br><br>
 * <br>
 */
public class CMIRequest implements Serializable
{
   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_UNKNOWN = 0;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_INIT = 1;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_GET = 2;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_TIMEOUT = 3;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_SET = 4;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_NAV = 5;

   /**
    * The run-time data that is being send from the client
    */
   public SCODataManager mActivityData = null;

   /**
    * Indicates if the request is being sent due to an LMSFinish
    */
   public boolean mIsFinished = false;

   /**
    * The activity ID of the activity that caused a time out.
    */
   public String mTimeoutActivity = null;

   /**
    * The type of the current Request.
    */
   public int mRequestType = CMIRequest.TYPE_UNKNOWN;

   /**
    * The attempt count associated with the run-time data.
    */
   public String mNumAttempt = null;

   /**
    * Whether or not the Quit button was pressed.
    */
   public boolean mQuitPushed = false;

   /**
    * Whether or not the Suspend button was pressed.
    */
   public boolean mSuspendPushed = false;
   
   /**
    * aicc_sid
    */
   public String aicc_sid;
   /**
    * Default constuctor.
    */
   public CMIRequest()
   {
   }

}


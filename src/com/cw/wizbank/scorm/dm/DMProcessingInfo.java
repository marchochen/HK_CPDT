package com.cw.wizbank.scorm.dm;

import java.util.Vector;

/**
 * Encapsulation of information required for processing a data model request.
 */
public class DMProcessingInfo {

	/**
	 * Describes the value being maintained by a data model element.
	 */
	public String mValue = null;

	/**
	 * Describes the data model element that processing should be applied to.
	 */
	public DMElement mElement = null;

	/**
	 * Describes the set this data model element is contained in.
	 */
	public Vector mRecords = null;

	/**
	 * Describes whether this data model element is initialized or not.
	 */
	public boolean mInitialized = false;

	/**
	 * Describes if this data model element's value was set by the SCO or not.
	 */
	public boolean mSetBySCO = false;
}

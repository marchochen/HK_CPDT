package com.cw.wizbank.scorm.dm;

import java.io.Serializable;

/**
 * Encapsulation of all information required to describe one dot-notation bound
 * delimiter. This information will be used to create instances of delimiters
 * assocaited with data model elements.
 */
public class DMDelimiterDescriptor implements Serializable {

	/**
	 * Describes the name of this delimiter
	 */
	public String mName = null;

	/**
	 * Describes if the default value of this delimiter
	 */
	public String mDefault = null;

	/**
	 * Describes the SPM for the value.
	 */
	public int mValueSPM = -1;

	/**
	 * Describes the method used to validate the value of this delimiter.
	 */
	public DMTypeValidator mValidator = null;

	/**
	 * Provides a way to store delimiter information such as name, default
	 * value, and type of validator.
	 * 
	 * @param iName
	 *            The name of the delimiter
	 * @param iDefault
	 *            The default value for the delimiter
	 * @param iValidator
	 *            The validator associated with the delimiter
	 */
	public DMDelimiterDescriptor(String iName, String iDefault, DMTypeValidator iValidator) {
		mName = iName;
		mDefault = iDefault;
		mValidator = iValidator;
	}

	/**
	 * Provides a way to store delimiter information such as name, default
	 * value, and type of validator.
	 * 
	 * @param iName
	 *            The name of the delimiter
	 * 
	 * @param iDefault
	 *            The default value for the delimiter
	 * 
	 * @param iValueSPM
	 *            The smallest permitted maximum size allowed for this delimiter
	 * 
	 * @param iValidator
	 *            The validator associated with the delimiter
	 */
	public DMDelimiterDescriptor(String iName, String iDefault, int iValueSPM, DMTypeValidator iValidator) {
		mName = iName;
		mDefault = iDefault;
		mValueSPM = iValueSPM;
		mValidator = iValidator;
	}

}

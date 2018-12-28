package com.cw.wizbank.scorm.dm.datatypes;

import com.cw.wizbank.scorm.dm.DMTypeValidator;
import com.cw.wizbank.scorm.dm.DMErrorCodes;

import java.util.Vector;
import java.io.Serializable;

/**
 * Provides support for the Integer data type within a specified range.
 */
public class IntRangeValidator extends DMTypeValidator implements Serializable {

	/**
	 * Describes the maxium value allowed in the range
	 */
	private int mMax = 0;

	/**
	 * Describes the minimum value allowed in the range
	 */
	private int mMin = 0;

	/**
	 * Default constructor required for serialization.
	 */
	public IntRangeValidator() {
	}

	/**
	 * Defines an integer range.
	 * 
	 * @param iMax
	 *            Defines the upper bound for this integer range.
	 */
	public IntRangeValidator(int iMax) {
		mMax = iMax;
	}

	/**
	 * Defines an integer range.
	 * 
	 * @param iMin
	 *            Defines the lower bound for this integer range.
	 * 
	 * @param iMax
	 *            Defiens the upper bound for this integer range.
	 */
	public IntRangeValidator(int iMin, int iMax) {
		mMax = iMax;
		mMin = iMin;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	
	 Public Methods
	
	-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	/**
	 * Compares two valid data model elements for equality.
	 * 
	 * @param iFirst
	 *            The first value being compared.
	 * 
	 * @param iSecond
	 *            The second value being compared.
	 * 
	 * @param iDelimiters
	 *            The common set of delimiters associated with the values being
	 *            compared.
	 * 
	 * @return Returns <code>true</code> if the two values are equal, otherwise
	 *         <code>false</code>.
	 */
	public boolean compare(String iFirst, String iSecond, Vector iDelimiters) {
		boolean equal = true;
		boolean done = false;

		int val1 = 0;
		int val2 = 0;

		try {
			val1 = Integer.parseInt(iFirst);
			val2 = Integer.parseInt(iSecond);
		} catch (NumberFormatException nfe) {
			equal = false;
			done = true;
		}

		if (!done) {
			equal = val1 == val2;
		}

		return equal;
	}

	/**
	 * Validates the provided string against a known format.
	 * 
	 * @param iValue
	 *            The value being validated.
	 * 
	 * @return An abstract data model error code indicating the result of this
	 *         operation.
	 */
	public int validate(String iValue) {
		// Assume the value is valid
		int valid = DMErrorCodes.NO_ERROR;

		if (iValue == null) {
			// A null value can never be valid
			return DMErrorCodes.UNKNOWN_EXCEPTION;
		}

		try {
			int value = Integer.parseInt(iValue);

			if (value < mMin || value > mMax) {
				valid = DMErrorCodes.TYPE_MISMATCH;
			}
		} catch (NumberFormatException nfe) {
			valid = DMErrorCodes.TYPE_MISMATCH;
		}

		return valid;
	}

} // end IntRangeValidator


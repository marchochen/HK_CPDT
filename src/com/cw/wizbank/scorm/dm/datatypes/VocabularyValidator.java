package com.cw.wizbank.scorm.dm.datatypes;

import com.cw.wizbank.scorm.dm.DMErrorCodes;
import com.cw.wizbank.scorm.dm.DMTypeValidator;
import java.io.Serializable;

/**
 * VocabularyValidator
 */
public class VocabularyValidator extends DMTypeValidator implements Serializable {

	/**
	 * A array of vocabularies values
	 */
	String[] mVocabList = null;

	/**
	 * Constructor required for vocabulary initialization.
	 * 
	 * @param iVocab
	 *            The array of vocabulary strings to be used in initialization.
	 */
	public VocabularyValidator(String[] iVocab) {
		mVocabList = iVocab;
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
		// Assume the value is not valid
		int valid = DMErrorCodes.TYPE_MISMATCH;

		// Check first if mVocablist is null
		if (mVocabList != null) {
			for (int i = 0; i < mVocabList.length; i++) {
				String tmpVocab = mVocabList[i];

				// Check if tmpVocab is null
				if (tmpVocab != null) {
					// Check to see if the element equals the input value
					if (tmpVocab.equals(iValue)) {
						valid = DMErrorCodes.NO_ERROR;
						break;
					}
				}
			}
		} else {
			// A null value can never be valid
			valid = DMErrorCodes.UNKNOWN_EXCEPTION;
		}

		return valid;
	}

}

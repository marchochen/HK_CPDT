package com.cw.wizbank.scorm.dm;

import java.io.Serializable;

/**
 * DMDelimiter
 */
public class DMDelimiter implements Serializable {

	/**
	 * Describes the properties of this delimiter.
	 */
	public DMDelimiterDescriptor mDescription = null;

	/**
	 * Describes the value of this delimiter.
	 */
	public String mValue = null;

	/**
	 * Default constructor required for serialization support.
	 */
	public DMDelimiter() {
	}

	/**
	 * Creates a <code>DMDelimiter</code> object that exhibits the qualities
	 * described in its <code>DMDelimiterDescriptor</code>.
	 * 
	 * @param iDescription
	 *            Describes this <code>DMDelimiter</code>
	 */
	public DMDelimiter(DMDelimiterDescriptor iDescription) {
		mDescription = iDescription;
	}

	/**
	 * Provides the dot-notation binding for this delimiter.
	 * 
	 * @param iDelimiters
	 *            Indicates if the data model element's default delimiters
	 *            should be included in the return string.
	 * 
	 * @return The dot-notation <code>String</code> corresponding to this
	 *         delimiter.
	 */
	public String getDotNotation(boolean iDelimiters) {
		String dot = "";

		if (mValue != null) {
			dot = "{" + mDescription.mName + "=" + mValue + "}";
		} else {
			if (iDelimiters) {
				dot = "{" + mDescription.mName + "=" + mDescription.mDefault + "}";
			}
		}

		return dot;
	}
}

/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

public class NumericException extends RuntimeException {

	private static final long serialVersionUID = -4503246625847841542L;

	public NumericException() {
		
	}

	public NumericException(String message) {
		super(message);
	}

	public NumericException(Throwable cause) {
		super(cause);
	}

	public NumericException(String message, Throwable cause) {
		super(message, cause);
	}

}

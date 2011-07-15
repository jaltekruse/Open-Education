/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

public class NodeException extends RuntimeException {

	private static final long serialVersionUID = -4417281547925532699L;

	public NodeException() {
		
	}

	public NodeException(String arg0) {
		super(arg0);
	}

	public NodeException(Throwable arg0) {
		super(arg0);
	}

	public NodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

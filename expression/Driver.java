/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package expression;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeParser parser = new NodeParser();
		String p = "2x + 5x + 5+4*6^2-sin(10^3+9^3-12^3x)";
		Node n = parser.parseNode(p);
		System.out.println(n);
		System.out.println(n.numericSimplify());
//		parser.seekFromLast("log(2)", "log", 5);
	}
}

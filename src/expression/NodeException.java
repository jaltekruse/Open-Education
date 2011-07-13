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

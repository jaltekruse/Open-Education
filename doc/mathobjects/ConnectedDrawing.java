package doc.mathobjects;

import doc.GridPoint;
import doc.Page;

public class ConnectedDrawing extends MathObject {

	protected GridPoint[] points;
	
	public ConnectedDrawing(Page p, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		// TODO Auto-generated constructor stub
	}
	
	public ConnectedDrawing(Page p){
		super(p);
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}

package doc.mathobjects;

import doc.GridPoint;
import doc.Page;

public class ArrowObject extends PolygonObject {

	public ArrowObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public ArrowObject(Page p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	public ArrowObject() {
		addInitialPoints();
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	@Override
	public void addInitialPoints() {
		// TODO Auto-generated method stub
		addVertex(new GridPoint(0, .25));
		addVertex(new GridPoint(.75, .25));
		addVertex(new GridPoint(.75, 0));
		addVertex(new GridPoint(1, .5));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.75, .75));
		addVertex(new GridPoint(0, .75));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ARROW_OBJECT;
	}

	@Override
	public void addDefaultAttributes() {
		// TODO Auto-generated method stub
		
	}

}

package doc.mathobjects;

import doc.GridPoint;
import doc.Page;

public class CubeObject extends PolygonObject{
	
	
	
	public CubeObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}
	
	public CubeObject(Page p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
		addAction(PolygonObject.FLIP_HORIZONTALLY);
	}

	@Override
	public void addInitialPoints() {
		
		addVertex(new GridPoint(0, .25));
		addVertex(new GridPoint(0, 1));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.75, .25));
		addVertex(new GridPoint(0, .25));
		addVertex(new GridPoint(.25, 0));
		addVertex(new GridPoint(1, 0));
		addVertex(new GridPoint(1, .75));
		//couldn't think of a way to draw it without re-tracing lines
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.75, .25));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(.75, .25));
		addVertex(new GridPoint(1, 0));
		addVertex(new GridPoint(.25, 0));
	}
	
	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return CUBE_OBJECT;
	}
}

package doc.mathobjects;

import doc.GridPoint;
import doc.Page;

public class ParallelogramObject extends PolygonObject {

	public ParallelogramObject(Page p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public ParallelogramObject(Page p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}

	@Override
	public void addInitialPoints() {
		addVertex(new GridPoint(.25, 0));
		addVertex(new GridPoint(1, 0));
		addVertex(new GridPoint(.75, 1));
		addVertex(new GridPoint(0, 1));
	}
	
	@Override
	public void addDefaultAttributes() {
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PARALLELOGRAM_OBJECT;
	}

}

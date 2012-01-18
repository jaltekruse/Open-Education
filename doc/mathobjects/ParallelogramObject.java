package doc.mathobjects;

import doc.GridPoint;
import doc.Page;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ParallelogramObject extends PolygonObject {

	public ParallelogramObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public ParallelogramObject(MathObjectContainer p){
		super(p);
		addInitialPoints();
		addAction(PolygonObject.FLIP_VERTICALLY);
	}

	public ParallelogramObject() {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PARALLELOGRAM_OBJ;
	}
	
	@Override
	public ParallelogramObject clone() {
		ParallelogramObject o = new ParallelogramObject(getParentContainer());
		o.removeAllAttributes();
		for ( MathObjectAttribute mAtt : getAttributes()){
			o.addAttribute( mAtt.clone());
		}
		o.removeAllLists();
		for ( ListAttribute list : getLists()){
			o.addList(list.clone());
		}
		return o;
	}
}

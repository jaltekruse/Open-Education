package doc.mathobjects;

import doc.GridPoint;
import doc.attributes.ListAttribute;
import doc.attributes.MathObjectAttribute;

public class ParallelogramObject extends PolygonObject {

	private static final GridPoint[] vertices = {new GridPoint(.25, 0), new GridPoint(1, 0),
		new GridPoint(.75, 1), new GridPoint(0, 1)};
	
	public ParallelogramObject(MathObjectContainer p, int x, int y, int w, int h, int t) {
		super(p, x, y, w, h, t);
		addAction(PolygonObject.FLIP_VERTICALLY);
	}
	
	public ParallelogramObject(MathObjectContainer p){
		super(p);
		addAction(PolygonObject.FLIP_VERTICALLY);
	}

	public ParallelogramObject() {
		addAction(PolygonObject.FLIP_VERTICALLY);
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

	@Override
	public GridPoint[] getVertices() {
		// TODO Auto-generated method stub
		return vertices;
	}
}

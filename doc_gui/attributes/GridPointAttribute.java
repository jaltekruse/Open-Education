package doc_gui.attributes;

import java.util.StringTokenizer;

import doc.GridPoint;
import doc.mathobjects.AttributeException;

import java.util.StringTokenizer;

public class GridPointAttribute extends MathObjectAttribute<GridPoint> {
	
	double xMin,xMax,yMin, yMax;
	
	public GridPointAttribute(String n) {
		super(n);
		xMin = 0;
		xMax = 1;
		yMin = 0;
		yMax = 1;
	}
	
	public GridPointAttribute(String n, GridPoint pt) {
		super(n);
		xMin = 0;
		xMax = 1;
		yMin = 0;
		yMax = 1;
		setValue(pt);
	}

	public GridPointAttribute(String n, double xMin, double xMax, double yMin, double yMax) {
		super(n);
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return GRID_POINT_ATTRIBUTE;
	}

	/* (non-Javadoc)
	 * @see doc.mathobjects.MathObjectAttribute#setValueWithString(java.lang.String)
	 * 
	 * Takes the value of the point in the format (x,y), where x and y are
	 * string representations of float values
	 */
	@Override
	public GridPoint readValueFromString(String s) throws AttributeException {
		// TODO Auto-generated method stub
		StringTokenizer st = new StringTokenizer(s, ", ( )");
		
		double x = Double.parseDouble(st.nextToken());
		double y = Double.parseDouble(st.nextToken());
		if (x < xMin || x > xMax){
			throw new AttributeException("x must be between " + xMin + " and " + xMax);
		}
		if (y < yMin || y > yMax){
			throw new AttributeException("y must be between " + yMin + " and " + yMax);
		}
		return new GridPoint(x, y);
	
	}

}

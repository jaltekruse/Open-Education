/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package math_rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import expression.Expression;
import expression.Node;

public class AbsoluteValueGraphic extends UnaryExpressionGraphic {

	private int space;
	private int overhang;
	
	//stores the width in pixels of the line on each side of the absolute valuegraphic, 
	//including space on the inside of the line
	private int widthSpaceAndLine;
	
	public AbsoluteValueGraphic(Expression v, RootNodeGraphic compExGraphic) {
		super(v, compExGraphic);
		setMostInnerNorth(this);
		setMostInnerSouth(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		if (isSelected()){
			super.getRootNodeGraphic().getGraphics().setColor(getSelectedColor());
			super.getRootNodeGraphic().getGraphics().fillRect(symbolX1, symbolY1, symbolX2 - symbolX1, symbolY2 - symbolY1);
			super.getRootNodeGraphic().getGraphics().setColor(Color.black);
		}
		
		super.getRootNodeGraphic().getGraphics().setStroke(new BasicStroke(
				(int) (1 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL)));
		
		super.getRootNodeGraphic().getGraphics().drawLine(symbolX1 + (int) Math.round(widthSpaceAndLine/2.0), 
				getY1(), getX1() + (int) Math.round(widthSpaceAndLine/2.0), getY2());
		super.getRootNodeGraphic().getGraphics().drawLine(getX2() - (int) Math.round(widthSpaceAndLine/2.0),
				getY1(), getX2() - (int) Math.round(widthSpaceAndLine/2.0), getY2());
		
		super.getRootNodeGraphic().getGraphics().setStroke(new BasicStroke());
	}
	

	@Override
	public void drawCursor(){
		int cursorPos = super.getRootNodeGraphic().getCursor().getPos();
		
		super.getRootNodeGraphic().getGraphics().setColor(Color.BLACK);
		super.getRootNodeGraphic().getGraphics().fillRect(findCursorXPos(), getY1() - 3, 2,
				getY2() - getY1()+ 5);
		return;
		
	}
	
	@Override
	public int findCursorXPos(){
		super.getRootNodeGraphic().getGraphics().setFont(getFont());
		int cursorPos = super.getRootNodeGraphic().getCursor().getPos();
		
		if (cursorPos == 0){
			return getX1() - 1;
		}
		else if (cursorPos == 1){
			return getX1() + widthSpaceAndLine + space/2;
		}
		else if (cursorPos == 2){
			return getX2() - widthSpaceAndLine - space/2;
		}
		else if (cursorPos == 3){
			return getX2();
		}
		
		//should replace this with exception thrown
		return Integer.MAX_VALUE;
			
	}
	
	@Override
	public int getMaxCursorPos(){
		return 3;
	}
	
	@Override
	public void setCursorPos(int xPixelPos){
		
		String valueString = getValue().getOperator().getSymbol();
		
		if (xPixelPos < getX1() + widthSpaceAndLine + space){
			if (xPixelPos < getX1() + widthSpaceAndLine/2){
				super.getRootNodeGraphic().getCursor().setPos(0);
				super.getRootNodeGraphic().getCursor().setValueGraphic(this);
				return;
			}
			else{
				super.getRootNodeGraphic().getCursor().setPos(1);
				super.getRootNodeGraphic().getCursor().setValueGraphic(this);
				return;
			}
		}
		else{
			if (xPixelPos > getX2() - widthSpaceAndLine - space){
				if (xPixelPos < getX2() - widthSpaceAndLine/2){
					super.getRootNodeGraphic().getCursor().setPos(2);
					super.getRootNodeGraphic().getCursor().setValueGraphic(this);
					return;
				}
				else{
					super.getRootNodeGraphic().getCursor().setPos(3);
					super.getRootNodeGraphic().getCursor().setValueGraphic(this);
					return;
				}
			}
			else{
				getChildGraphic().setCursorPos(xPixelPos);
				return;
			}
		}
	}
	
	@Override
	public void moveCursorWest(){
		int cursorPos = getRootNodeGraphic().getCursor().getPos();
		if (cursorPos == 3 || cursorPos == 1){
			super.getRootNodeGraphic().getCursor().setPos( cursorPos - 1); 
		}
		else if (cursorPos == 0){
			if (getWest() != null){
				getWest().sendCursorInFromEast((getY2() - getY1())/2, this);
				return;
			}
		}
		else{
			getChildGraphic().getMostInnerEast().sendCursorInFromEast((getY2() - getY1())/2, this);
			return;
		}
	}
	
	@Override
	public void moveCursorEast(){
		int cursorPos = getRootNodeGraphic().getCursor().getPos();
		if (cursorPos == 2 || cursorPos == 0){
			super.getRootNodeGraphic().getCursor().setPos( cursorPos + 1); 
		}
		else if (cursorPos == 3){
			if (getEast() != null){
				getEast().sendCursorInFromWest((getY2() - getY1())/2, this);
				return;
			}
		}
		else{
			getChildGraphic().getMostInnerWest().sendCursorInFromWest((getY2() - getY1())/2, this);
			return;
		}
	}
	
	@Override
	public void moveCursorNorth(){
		if (getNorth() == null)
		{
			return;
		}
		else
		{
			getNorth().sendCursorInFromSouth(findCursorXPos(), this);
			return;
		}
	}
	
	@Override
	public void moveCursorSouth(){
		if (getSouth() == null)
		{
			return;
		}
		else
		{
			getSouth().sendCursorInFromNorth(findCursorXPos(), this);
			return;
		}
	}
	
	@Override
	public void sendCursorInFromEast(int yPos, NodeGraphic vg){
		if (super.containedBelow(vg)){
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			super.getRootNodeGraphic().getCursor().setPos(0);
		}
		else{
			super.getRootNodeGraphic().getCursor().setValueGraphic(getChildGraphic().getMostInnerEast());
			super.getRootNodeGraphic().getCursor().setPos(getChildGraphic().getMostInnerEast().getMaxCursorPos());
		}
	}
	
	@Override
	public void sendCursorInFromWest(int yPos, NodeGraphic vg){
		if (super.containedBelow(vg)){
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			super.getRootNodeGraphic().getCursor().setPos(getMaxCursorPos());
		}
		else{
			super.getRootNodeGraphic().getCursor().setValueGraphic(getChildGraphic().getMostInnerWest());
			super.getRootNodeGraphic().getCursor().setPos(0);
		}
	}
	
	@Override
	public void sendCursorInFromNorth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}
	
	@Override
	public void sendCursorInFromSouth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}

	@Override
	public int[] requestSize(Graphics g, Font f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] requestSize(Graphics g, Font f, int x1, int y1)
			throws Exception {
		//g is used to decide the size of the text to display for this element
		g.setFont(f);
		
		space = (int) (2 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL);
		overhang =  (int) (1 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL);
		widthSpaceAndLine =  (int) (2 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL);
		
		//to draw this element later, the font must be the same, so its stored in this object
		setFont(f);
		
		int[] childSize = {0,0};
		int[] symbolSize = {0, 0};
		int[] totalSize = {0, 0};

		// The call to getChild() skips the first paren inside of the operator, the parens are needed to have
		// an expression inside of a UnaryOp, but they are not usually displayed
		// if a user wants to show parens, the can use  two pairs of parens: abs((5/6))
		Node tempChild = (super.getValue()).getChild(0);
		
		NodeGraphic childValGraphic = makeNodeGraphic(tempChild);
		childSize = childValGraphic.requestSize(g, f, x1 + widthSpaceAndLine + space, y1 + overhang);
		
		//set the west and east fields for inside an outside of the expression
		setMostInnerWest(this);
		childValGraphic.getMostInnerEast().setEast(this);
		
		setMostInnerEast(this);
		childValGraphic.getMostInnerWest().setWest(this);
		
		setChildGraphic(childValGraphic);
		super.getRootNodeGraphic().getComponents().add(childValGraphic);
		
		widthSpaceAndLine += (int) Math.round(childSize[1]/14.0);
		childValGraphic.shiftToX1(x1 + widthSpaceAndLine + space);
		
		symbolSize[0] = childSize[0] + space * 2 + widthSpaceAndLine * 2;
		symbolSize[1] = childSize[1] + overhang * 2;
		
		symbolY1 = y1;
		symbolY2 = symbolY1 + symbolSize[1];
		symbolX1 = x1;
		symbolX2 = x1 + symbolSize[0];
		
		setUpperHeight(childValGraphic.getUpperHeight() + overhang);
		setLowerHeight(childValGraphic.getLowerHeight() + overhang);
		
		totalSize[0] = symbolSize[0];
		totalSize[1] = symbolSize[1];
		
		//set the outer bounds of this element
		super.setX1(x1);
		super.setY1(y1);
		super.setX2(x1 + totalSize[0]);
		super.setY2(y1 + totalSize[1]);
		
		return totalSize;
	}
}
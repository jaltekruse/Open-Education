/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package math_rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import expression.Expression;
import expression.Node;

public class UnaryPostGraphic extends UnaryExpressionGraphic {

private int space;
	
	public UnaryPostGraphic(Expression v,
			RootNodeGraphic compExGraphic) {
		super(v, compExGraphic);
		setMostInnerNorth(this);
		setMostInnerSouth(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		if (isSelected()){
			getRootNodeGraphic().getGraphics().setColor(getSelectedColor());
			getRootNodeGraphic().getGraphics().fillRect(symbolX1, symbolY1, symbolX2 - symbolX1, symbolY2 - symbolY1);
			getRootNodeGraphic().getGraphics().setColor(Color.black);
		}
		getRootNodeGraphic().getGraphics().setFont(getFont());
		getRootNodeGraphic().getGraphics().drawString(getValue().getOperator().getSymbol(),
				symbolX1, symbolY2);
	}
	
	@Override
	public void drawCursor(int pos){
		
	}

	@Override
	public int[] requestSize(Graphics g, Font f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] requestSize(Graphics g, Font f, int x1, int y1)
			throws Exception {
		g.setFont(f);
		setFont(f);
		
		space = (int) (2 * super.getRootNodeGraphic().DOC_ZOOM_LEVEL);
		
		Node tempChild = getValue().getChild(0);
		NodeGraphic childValGraphic = null;
		int[] childSize = {0,0};
		int[] symbolSize = {0, 0};
		int[] totalSize = {0, 0};
		
		childValGraphic = makeNodeGraphic(tempChild);
		
		setChildGraphic(childValGraphic);
		super.getRootNodeGraphic().getComponents().add(childValGraphic);
		
		childSize = childValGraphic.requestSize(g, f, x1 + symbolSize[0], y1);
		
		//set the west and east fields for inside an outside of the expression
		setMostInnerWest(childValGraphic);
		setWest(childValGraphic.getMostInnerEast());
		childValGraphic.getMostInnerEast().setEast(this);
		setMostInnerEast(this);
		
		symbolSize[0] = super.getRootNodeGraphic().getStringWidth(value.getOperator().getSymbol(), f);
		symbolSize[1] = super.getRootNodeGraphic().getFontHeight(f);
		
		symbolY1 = y1 + childValGraphic.getUpperHeight() - (int) Math.round(symbolSize[1]/2.0);
		symbolY2 = symbolY1 + symbolSize[1];
		symbolX1 = x1 + space + childSize[0];
		symbolX2 = x1 + symbolSize[0];
		
		setUpperHeight(childValGraphic.getUpperHeight());
		setLowerHeight(childValGraphic.getLowerHeight());
		
		totalSize[0] = symbolSize[0] + childSize[0];
		totalSize[1] = childSize[1];
		super.setX1(x1);
		super.setY1(y1);
		super.setX2(x1 + totalSize[0]);
		super.setY2(y1 + totalSize[1]);
		// TODO Auto-generated method stub
		return totalSize;
	}

	@Override
	public void drawCursor(){
		String opString = getValue().getOperator().getSymbol();
		
		int xPos = symbolX1 + super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				opString.substring(0, super.getRootNodeGraphic().getCursor().getPos()));
		
		if ( super.getRootNodeGraphic().getCursor().getPos() == getMaxCursorPos()){
			xPos += space;
		}
		super.getRootNodeGraphic().getGraphics().setColor(Color.BLACK);
		super.getRootNodeGraphic().getGraphics().fillRect(xPos, getY1() - 3, 2, getY2() - getY1() + 5);
		
	}
	
	@Override
	public int getMaxCursorPos(){
		return getValue().getOperator().getSymbol().length();
	}
	
	@Override
	public void setCursorPos(int xPixelPos){
		
		String numberString = getValue().getOperator().getSymbol();
		
		if (xPixelPos < super.symbolX1){
			getChildGraphic().setCursorPos(xPixelPos);
			return;
		}
			
		else if (xPixelPos > super.symbolX2){
			super.getRootNodeGraphic().getCursor().setPos(numberString.length());
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			return;
		}
		
		int startX, endX, xWidth;
		
		startX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				numberString.substring(0, 0)) + symbolX1 - space;
		endX = super.getRootNodeGraphic().getGraphics().getFontMetrics().stringWidth(
				numberString.substring(0, 1)) + symbolX1 + space;
		xWidth = endX - startX;
		
		if (startX < xPixelPos && endX > xPixelPos)
		{//if the x position is inside of a character, check if it is on the first or second
			//half of the character and set the cursor accordingly
			if (endX - xPixelPos > xWidth/2){
				super.getRootNodeGraphic().getCursor().setPos( 0 );
			}
			else{
				super.getRootNodeGraphic().getCursor().setPos( 1 );
			}
			super.getRootNodeGraphic().getCursor().setValueGraphic(this);
			return;
		}
	}
	
	@Override
	public void moveCursorWest(){
		if (super.getRootNodeGraphic().getCursor().getPos() > 0){
			super.getRootNodeGraphic().getCursor().setPos( super.getRootNodeGraphic().getCursor().getPos() - 1); 
		}
		else{
			if (getWest() == null)
			{
				return;
			}
			else
			{
				getWest().sendCursorInFromEast((getY2() - getY1())/2, this);
				return;
			}
		}
	}
	
	@Override
	public void moveCursorEast(){
		if (super.getRootNodeGraphic().getCursor().getPos() < getMaxCursorPos()){
			super.getRootNodeGraphic().getCursor().setPos( super.getRootNodeGraphic().getCursor().getPos() + 1); 
		}
		else{
			if (getEast() == null)
			{
				return;
			}
			else
			{
				getEast().sendCursorInFromWest((getY2() - getY1())/2, this);
				return;
			}
		}
	}
	
	@Override
	public void sendCursorInFromEast(int yPos, NodeGraphic vg){
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(getMaxCursorPos() - 1);
	}
	
	@Override
	public void sendCursorInFromWest(int yPos, NodeGraphic vg){
		super.getRootNodeGraphic().getCursor().setValueGraphic(this);
		super.getRootNodeGraphic().getCursor().setPos(1);
	}
	
	@Override
	public void sendCursorInFromNorth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}
	
	@Override
	public void sendCursorInFromSouth(int xPos, NodeGraphic vg){
		setCursorPos(xPos);
	}
	
}

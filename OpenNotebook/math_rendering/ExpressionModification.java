/*
   This file is part of OpenNotebook.

   OpenNotebook is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OpenNotebook is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
    along with OpenNotebook.  If not, see <http://www.gnu.org/licenses/>.
 */

package math_rendering;

import java.util.Vector;

import expression.NodeException;

public class ExpressionModification {

	Vector<NodeGraphic> expressions;
	
	public ExpressionModification(){
		expressions = new Vector<NodeGraphic>();
	}
	
	public void draw() throws NodeException{
		for (NodeGraphic vg : expressions){
			vg.drawAllBelow();
		}
	}
	
	public void addVG(NodeGraphic vg){
		expressions.add(vg);
	}
	
	public int getHeight(){
		if (expressions.size() > 0){
			return expressions.get(0).getHeight();
		}
		return 0;
	}
}

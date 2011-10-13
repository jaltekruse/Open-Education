package expression;

import java.util.Vector;

public class Selection {

	private Vector<Integer> positions;
	
	public Selection(Vector<Integer> positions) {
		this.positions = positions;
	}
	
	public Selection() {
		this.positions = new Vector<Integer>();
		
	}
	
	public int positionAt(int level) {
		return positions.get(level);
	}
	
	public int depth() {
		return positions.size();
	}
	
	public int first() {
		return positionAt(0);
	}
	
	public Selection pop() {
		@SuppressWarnings("unchecked")
		Vector<Integer> newPositions = (Vector<Integer>) positions.clone();
		newPositions.remove(0);
		return new Selection(newPositions);
	}
	
	public Selection push(int position) {
		@SuppressWarnings("unchecked")
		Vector<Integer> newPositions = (Vector<Integer>) positions.clone();
		newPositions.add(0, position);
		return new Selection(newPositions);
	}
	
	public String toString() {
		return positions.toString();
	}
	
	/**
	 * Finds a common parent of both Selections.
	 * Does not take into account addition or multiplication
	 * commutativity.
	 * @param other
	 * @return
	 */
	public Selection merge(Selection other) {
		Vector<Integer> common = new Vector<Integer>();
		for (int i = 0 ; (i < depth()) && (i < other.depth()) ; i++) {
			if (this.positionAt(i) == other.positionAt(i)) {
				common.add(positionAt(i));
			} else {
				break;
			}
		}
		return new Selection(common);
	}
	
}

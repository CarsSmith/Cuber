import java.awt.Color;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class FaceSquare extends JPanel {

	private Color squareColor;
	private final Color intendedColor;
	private final int row;
	private final int col;
	
	public FaceSquare(Color initColor, MouseInputAdapter myListener, int col, int row) {
		squareColor = initColor;
		intendedColor = initColor;
		this.row = row;
		this.col = col;
		this.setBackground(squareColor);
		this.addMouseListener(myListener);
	    this.addMouseMotionListener(myListener);
	}
	
	public Color getCurrentColor() {
		return squareColor;
	}
	
	public Color getIntendedColor() {
		return intendedColor;
	}
	
	public int getIntendedPanel() {
		if(intendedColor == Color.WHITE) {
			return 0;
		} else if(intendedColor == Color.RED) {
			return 1;
		} else if(intendedColor == Color.BLUE) {
			return 2;
		} else if(intendedColor == Color.ORANGE) {
			return 3;
		} else if(intendedColor == Color.GREEN) {
			return 4;
		} else {
			return 5;
		}
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setSquareColor(Color colorValue) {
		squareColor = colorValue;
		this.setBackground(squareColor);
	}
}

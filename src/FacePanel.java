import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class FacePanel extends JPanel {

	private Color baseColor;
	private int col;
	private int row;
	private FacePanel right;
	private FacePanel left;
	private FacePanel up;
	private FacePanel down;
	private FacePanel back;
	private FaceSquare[][] squareArray;
	private MouseInputAdapter myListener;
	private int DIM;
	
	public FacePanel(Color baseColor, int col, int row, MouseInputAdapter myListener, int DIM) {
		this.DIM = DIM;
		this.setLayout(new GridBagLayout());
		this.baseColor = baseColor;
		this.col = col;
		this.row = row;
		this.myListener = myListener;
		this.setBackground(Color.BLACK); //For the outline of the squares.
		squareArray = new FaceSquare[DIM][DIM]; //9 for now.
		
		createFaceSquarePanels();
	}
	
	public void createFaceSquarePanels() {
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(1, 1, 1, 1);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		for(int i = 0; i < squareArray.length; i++) {
			for(int j = 0; j < squareArray[i].length; j++) {
				FaceSquare square = new FaceSquare(baseColor, myListener, i, j); //Making the square
				squareArray[i][j] = square; //Adding it to the array
				constraints.gridx = i;      //setting the row location it's placed in the FacePanel
				constraints.gridy = j;      //setting the col location it's placed in the FacePanel
				this.add(square, constraints); //Adding it to the FacePanel.
			}
		}
	}

	public FaceSquare[][] getSquareArray() {
		return squareArray;
	}
	
	public int getXValue() {
		return col;
	}

	public int getYValue() {
		return row;
	}
	
	public void setReferences(FacePanel up, FacePanel down, FacePanel right, FacePanel left, FacePanel back) {
		this.up = up;
		this.down = down;
		this.right = right;
		this.left = left;
		this.back = back;
	}
	
	public FacePanel getUpRef() {
		return up;
	}
	
	public FacePanel getDownRef() {
		return down;
	}
	
	public FacePanel getRightRef() {
		return right;
	}
	
	public FacePanel getLeftRef() {
		return left;
	}
	
	public FacePanel getBackRef() {
		return back;
	}
	
	public void rotateSquaresLeft() {
		//clockwise
		for(int x = 0; x < (DIM / 2); x++) {
			
			for(int y = x; y < DIM-x-1; y++) {
				//Get value from the top
				Color temp = squareArray[x][y].getCurrentColor();
				
				//Move values from left to the top
				squareArray[x][y].setSquareColor(squareArray[DIM-1-y][x].getCurrentColor());
				
				//Move values from bottom to the left
				squareArray[DIM-1-y][x].setSquareColor(squareArray[DIM-1-x][DIM-1-y].getCurrentColor());
				
				//Move values from right to bottom
				squareArray[DIM-1-x][DIM-1-y].setSquareColor(squareArray[y][DIM-1-x].getCurrentColor());
				
				//move temp to the right
				squareArray[y][DIM-1-x].setSquareColor(temp);
			}
		}
	}
	public void rotateSquaresRight() {
		//anti-clockwise
		for(int x = 0; x < DIM / 2; x++) { 
            // Consider elements in group of 4 in  
            // current square 
            for(int y = x; y < DIM-x-1; y++) { 
                // store current cell in temp variable 
                Color temp = squareArray[x][y].getCurrentColor(); 
       
                // move values from right to top 
                squareArray[x][y].setSquareColor(squareArray[y][DIM-1-x].getCurrentColor());
       
                // move values from bottom to right
                squareArray[y][DIM-1-x].setSquareColor(squareArray[DIM-1-x][DIM-1-y].getCurrentColor()); 
       
                // move values from left to bottom 
                squareArray[DIM-1-x][DIM-1-y].setSquareColor(squareArray[DIM-1-y][x].getCurrentColor());
                
                // assign temp to left 
                squareArray[DIM-1-y][x].setSquareColor(temp); 
            } 
        } 
	}
}

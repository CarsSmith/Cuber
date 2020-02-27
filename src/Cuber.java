import javax.swing.*;

/**
 * This is the basic, fundamental driver for the program.
 * 
 * @author Carson Smith
 */
public class Cuber {

	public static void main(String[] args) {
        JFrame frame = new JFrame("Cuber");
        CuberPanel cuberPanel = new CuberPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(cuberPanel);
        frame.pack();
        frame.setVisible(true);
    }
}

package frames;

import java.awt.Component;
import javax.swing.JOptionPane;

public class Messages {

	public static void showInfo(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showWarning(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean showQuestion(Component parent, String message) {
		return JOptionPane.showConfirmDialog(parent, message, "QUESTION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0;
	}
	
	
}

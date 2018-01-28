package frames;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JTextArea txtrLog;
	
	public LogDialog() {
		setBounds(100, 100, 620, 446);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		txtrLog = new JTextArea();
		scrollPane.setViewportView(txtrLog);
		
		JLabel lblLog = new JLabel("Log");
		scrollPane.setColumnHeaderView(lblLog);
	}
	
	public void setText(String text) {
		txtrLog.setText(text);
	}
}

package main;

import java.awt.EventQueue;
import javax.swing.UIManager;
import frames.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		
		try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception ex) {
            System.err.println("ERROR: "+ex.toString());
        }
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
}

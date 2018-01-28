package frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Iterators;
import utils.FileCompress;
import utils.SearchAndReplace;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	static JFileChooser CHOOSER;
	static {
		CHOOSER = new JFileChooser();
		CHOOSER.setCurrentDirectory(new File("."));
		CHOOSER.setMultiSelectionEnabled(false);
		CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	private JPanel contentPane;
	private JTextField txtRootPath;
	private JCheckBox chckbxCreateBackup;
	private JTextArea txtSearch, txtReplace;
	
	public MainFrame() {
		setTitle("JSearchAndReplace");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblTitle = new JLabel("JSearchAndReplace");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 18));
		
		JPanel panelBottom = new JPanel();
		
		JPanel browsePanel = new JPanel();
		
		JPanel panelTexts = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(browsePanel, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelTexts, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(panelBottom, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle)
					.addGap(18)
					.addComponent(browsePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelTexts, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelBottom, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
		);
		panelBottom.setLayout(new BorderLayout(0, 0));
		
		chckbxCreateBackup = new JCheckBox("Create backup");
		panelBottom.add(chckbxCreateBackup, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panelBottom.add(panel, BorderLayout.EAST);
		
		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyActionPerformed(e, true);
			}
		});
		panel.add(btnAnalyze);
		
		JButton btnApply = new JButton("Apply");
		panel.add(btnApply);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyActionPerformed(e, false);
			}
		});
		panelTexts.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPaneLeft = new JScrollPane();
		panelTexts.add(scrollPaneLeft);
		
		txtSearch = new JTextArea();
		txtSearch.setText("Text to search");
		scrollPaneLeft.setViewportView(txtSearch);
		
		JLabel lblSearch = new JLabel("Search");
		scrollPaneLeft.setColumnHeaderView(lblSearch);
		
		JScrollPane scrollPaneRight = new JScrollPane();
		panelTexts.add(scrollPaneRight);
		
		txtReplace = new JTextArea();
		txtReplace.setText("Text to replace");
		scrollPaneRight.setViewportView(txtReplace);
		
		JLabel lblReplace = new JLabel("Replace");
		scrollPaneRight.setColumnHeaderView(lblReplace);
		browsePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRootPath = new JLabel("Root path:");
		browsePanel.add(lblRootPath, BorderLayout.WEST);
		
		txtRootPath = new JTextField();
		browsePanel.add(txtRootPath, BorderLayout.CENTER);
		txtRootPath.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse...");
		browsePanel.add(btnBrowse, BorderLayout.EAST);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseButtonActionPerformed(e);
			}
		});
		contentPane.setLayout(gl_contentPane);
		setLocationRelativeTo(null);
	}
	
	private void browseButtonActionPerformed(ActionEvent e) {
		if(CHOOSER.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File dir = CHOOSER.getSelectedFile();
			txtRootPath.setText(dir.getAbsolutePath());
		}
	}
		
	private void applyActionPerformed(ActionEvent e, boolean analyze) {
		File dir = new File(txtRootPath.getText());
		
		if(!dir.isDirectory()) {
			Messages.showError(this, "Directory \"" + dir.getName() + "\" not found");
			return;
		}
		
		if(!analyze && chckbxCreateBackup.isSelected()) {
			try {
				FileCompress.createTarXz(dir, new File(dir.getAbsolutePath()+".tar.xz"));
			} catch(Exception ex) {
				Messages.showError(this, "Cannot create backup");
				return;
			}
		}
		
		// List directory recursively
		Iterator<File> allFiles = FileUtils.iterateFiles(dir, null, true);
		
		// Convert Iterator to Array
		File[] arrayFiles = Iterators.toArray(allFiles, File.class);
		
		String search = txtSearch.getText();
		String replace = txtReplace.getText();
		
		try {
			SearchAndReplace sar = new SearchAndReplace(arrayFiles, search, replace);
			String out = "";
			if(analyze) {
				out = sar.analyze();
				
			} else {
				out = sar.execute();
				Messages.showInfo(this, "Success!!");
			}
			
			LogDialog log = new LogDialog();
			log.setText(out);
			log.setVisible(true);
			
		} catch (Exception ex) {
			Messages.showError(this, "Cannot modify files");
		}
	}
}

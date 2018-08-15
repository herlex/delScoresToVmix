package delScoresToVmix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class View extends JFrame{
	private Model model;
	
	private JTextField vmixIp = new JTextField();
	private JTextField vmixPort = new JTextField();
	private final JLabel vmixIpLabel = new JLabel("VMix IP:");
	private final JLabel vmixPortLabel = new JLabel("VMix Port:");
	private final JButton fetchDataBtn = new JButton("Daten abrufen");
	
	private final int matchAmount = 7;
	private final String[] inputs = new String[] {"null", "1.1", "1.2", "2.1", "2.2"};
	
	private JTextArea[] matchComponents = new JTextArea[matchAmount];
	private JComboBox[] inputComponents = new JComboBox[matchAmount];
	
	public View(Model model) {
		this.model = model;
		
		initMainWindow();
		initComponents();
		initLayout(getContentPane());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initMainWindow() {
		setTitle("Del Scores to VMix");
	    setSize(1000, 300);
	    //setResizable(false);
	    setLocation(50, 50);
	    setVisible(true);
	}
	
	private void initComponents() {
		vmixIp.setText("127.0.0.1");
		vmixPort.setText("8088");
		
		for(int i = 0; i < matchComponents.length; ++i) {
			JTextArea cmp = new JTextArea();
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			cmp.setBorder(BorderFactory.createCompoundBorder(border,
		            BorderFactory.createEmptyBorder(1, 1, 1, 1)));
			cmp.setEditable(false);
			matchComponents[i] = cmp;
		}
		
		for(int i = 0; i < inputComponents.length; ++i) {
			inputComponents[i] = new JComboBox<String>(inputs);
		}
	}
	
	private void initLayout(final Container pane) {
		JPanel settings = new JPanel();
		GridLayout settingsLayout = new GridLayout(0, 2);
		settings.setLayout(settingsLayout);
		settings.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		settings.add(vmixIpLabel);
		settings.add(vmixIp);
		settings.add(vmixPortLabel);
		settings.add(vmixPort);
		settings.add(fetchDataBtn);
		
		JPanel matches = new JPanel();
		matches.setBorder(BorderFactory.createTitledBorder("Matches"));
		GridLayout matchLayout = new GridLayout(0, 2);
		matches.setLayout(matchLayout);
		
		for(int i = 0; i < matchAmount; ++i) {
			matches.add(matchComponents[i]);
			matches.add(inputComponents[i]);
		}
		
		pane.add(settings, BorderLayout.NORTH);
		pane.add(matches, BorderLayout.CENTER);
	}
	
	public void addDataFetchListener(ActionListener al) {
        fetchDataBtn.addActionListener(al);
    }
	
	public void updateMatchInfo(List<String> matches) {
		for(int i = 0; i < matchComponents.length; ++i) {
			matchComponents[i].setText(matches.get(i));
		}
	}
}

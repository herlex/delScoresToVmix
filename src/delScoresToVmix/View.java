package delScoresToVmix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class View extends JFrame{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Model model;
    
	private JTextField vmixIp                           = new JTextField();
    private JTextField vmixPort                         = new JTextField();
    private JSpinner vmixAutoUpdateInterval             = new JSpinner();
    private JSpinner gameDay                         	= new JSpinner();
    private final JLabel vmixIpLabel                    = new JLabel("VMix IP:");
    private final JLabel vmixPortLabel                  = new JLabel("VMix Port:");
    private final JLabel vmixAutoUpdateIntervalLabel    = new JLabel("AutoUpdate Interval (sec):");
    private final JLabel gameDayLabel					= new JLabel("Spieltag");
    private final JButton fetchDataBtn                  = new JButton("Daten abrufen");
    private final JButton sendDataBtn                   = new JButton("Daten senden");
    private JCheckBox autoUpdate                        = new JCheckBox("Auto Update");
    
    private JTextArea[] matchComponents = new JTextArea[7];
    private JComboBox[] inputComponents = new JComboBox[7];
    
    private JTextArea logView = new JTextArea(5, 50);
    
    public View(Model model) {
    	//setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/score.png")));
    	
        this.model = model;
        
        initMainWindow();
        initComponents();
        initLayout(getContentPane());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initMainWindow() {
        setTitle("Del Scores to VMix");
        setSize(1000, 500);
        setResizable(false);
        setVisible(true);
    }
    
    private void initComponents() {
        logView.setEditable(false);
        DefaultCaret caret = (DefaultCaret)logView.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        SpinnerModel autoUpdateModel = new SpinnerNumberModel(10, 0, 100, 1);
        vmixAutoUpdateInterval.setModel(autoUpdateModel);
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)vmixAutoUpdateInterval.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        
        SpinnerModel gameDayModel = new SpinnerNumberModel(1, 1, 52, 1);
        gameDay.setModel(gameDayModel);
        JSpinner.DefaultEditor gameDaySpinnerEditor = (JSpinner.DefaultEditor)gameDay.getEditor();
        gameDaySpinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        
        for(int i = 0; i < matchComponents.length; ++i) {
            matchComponents[i] = new JTextArea();
            matchComponents[i].setEditable(false);
            
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            matchComponents[i].setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        }
        
        for(int i = 0; i < inputComponents.length; ++i) {
            inputComponents[i] = new JComboBox<String>(model.getInputKeys());
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
        settings.add(vmixAutoUpdateIntervalLabel);
        settings.add(vmixAutoUpdateInterval);
        settings.add(gameDayLabel);
        settings.add(gameDay);
        settings.add(fetchDataBtn);
        settings.add(sendDataBtn);
        settings.add(autoUpdate);
        
        JPanel matches = new JPanel();
        matches.setBorder(BorderFactory.createTitledBorder("Matches"));
        GridLayout matchLayout = new GridLayout(0, 2);
        matches.setLayout(matchLayout);
        
        for(int i = 0; i < 7; ++i) {
            matches.add(matchComponents[i]);
            matches.add(inputComponents[i]);
        }
        
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log"));
        JScrollPane scrollArea = new JScrollPane(logView);
        logPanel.add(scrollArea);
        
        pane.add(settings, BorderLayout.NORTH);
        pane.add(matches, BorderLayout.CENTER);
        pane.add(logPanel, BorderLayout.SOUTH);
    }
    
    public void addDataFetchListener(ActionListener al) {
        fetchDataBtn.addActionListener(al);
    }
    
    public void addDataSendListener(ActionListener al) {
        sendDataBtn.addActionListener(al);
    }
    
    public void addSettingsChangedListener(ActionListener al) {
        vmixIp.addActionListener(al);
        vmixPort.addActionListener(al);
    }
    
    public void addAutoUpdateChangedListener(ActionListener al) {
        autoUpdate.addActionListener(al);
    }
    
    public void updateMatchInfo(List<String> matches) {
        for(int i = 0; i < matches.size(); ++i) {
            if(matches.get(i).contains("Fulltime") || matches.get(i).contains("n.")) {
                matchComponents[i].setBackground(Color.GREEN);
            } else if(matches.get(i).contains("vor dem Spiel")){
            	matchComponents[i].setBackground(Color.RED);
            }
            else
            {
            	matchComponents[i].setBackground(Color.YELLOW);
            }
            matchComponents[i].setText(matches.get(i));
        }
    }

    public boolean isAutoUpdateEnabled() {
        return autoUpdate.isSelected();
    }
    
    public void setVmixIp(String text) {
        vmixIp.setText(text);;
    }
    
    public void setVmixPort(String text) {
        vmixPort.setText(text);
    }
    
    public void setGameDay(int day) {
        gameDay.setValue(day);
    }
    
    public void setAutoUpdateInterval(int interval) {
        vmixAutoUpdateInterval.setValue(interval);
    }
    
    public String getVmixIp() {
        return vmixIp.getText();
    }
    
    public String getVmixPort() {
        return vmixPort.getText();
    }
    
    public int getGameDay() {
        return (int) gameDay.getValue();
    }
    
    public int getAutoUpdateInterval() {
        return (int) vmixAutoUpdateInterval.getValue();
    }
    
    public String getActiveInputKey(int index) {
        if(index < inputComponents.length) {
            return inputComponents[index].getSelectedItem().toString();
        }
        
        return "null";
    }
    
    public void writeLogMessage(String msg) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss]").format(Calendar.getInstance().getTime());
        logView.append("\n" + timeStamp + " " + msg);
    }
    
    public void disableControls() {
        sendDataBtn.setEnabled(false);
        fetchDataBtn.setEnabled(false);
        autoUpdate.setEnabled(false);
    }
    
    public void enableControls() {
        sendDataBtn.setEnabled(true);
        fetchDataBtn.setEnabled(true);
        autoUpdate.setEnabled(true);
    }
}

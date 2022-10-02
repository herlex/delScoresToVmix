package delScoresToVmix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Controller {
    private Model model;
    private View view;
    private BackgroundWorker worker;
    
    public static void main(String[] args) {
        new Controller();
    }
    
    private void initModel() {    	
    	JOptionPane jop = new JOptionPane("loading data...", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    	JDialog dialog = jop.createDialog(null, "Initialization");
    	dialog.setModal(true);
    	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    	dialog.pack();
    	

    	// Set a 2 second timer
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			model = new Model();
    			dialog.dispose();
    		}

    	}).start();

    	dialog.setVisible(true);
    }
    
    public Controller() {
    	initModel();
        
        view = new View( model );

        initView();
        connectListeners();
        
        if(!model.isModelReady) {
            view.writeLogMessage("Konfiguration konnte nicht initialisiert werden. Bitte neustarten.");
            view.disableControls();
        }
        
        view.setVisible(true);
    }
    
    private void initView() {
        view.setVmixIp("127.0.0.1");
        view.setVmixPort("8088");
        view.setAutoUpdateInterval(30);
    }
    
    private void connectListeners() {
        view.addDataFetchListener(new DataFetchListener());
        view.addDataSendListener(new DataSendListener());
        view.addAutoUpdateChangedListener(new AutoUpdateChangedListener());
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                model.shutdown();
            }
        });
    }
    
    public void sendData() throws IOException, ConnectException {
        if(!model.getUpcomingMatches().isEmpty()) {
            String url = "http://" + view.getVmixIp() + ":" + view.getVmixPort() + "/API/?Function=SetText&Input=%s&SelectedName=%s&Value=%s";
            for(int i = 0; i < 7; ++i) {
                String inputKey = view.getActiveInputKey(i);
                if(inputKey != "null") {
                    String[] input_ident = inputKey.split("\\.");
                    
                    if(input_ident[1].equals("1")) {
                        String send_url = String.format(url, model.getInput(inputKey), "Heim_1", model.getUpcomingMatches().get(i).scoreHome);
                        send(send_url);
                        
                        send_url = String.format(url, model.getInput(inputKey), "Gast_1", model.getUpcomingMatches().get(i).scoreAway);
                        send(send_url);
                        
                        send_url = String.format(url, model.getInput(inputKey), "Zeit_1", model.getUpcomingMatches().get(i).actualTime);
                        send(send_url);
                    } else {
                        String send_url = String.format(url, model.getInput(inputKey), "Heim_2", model.getUpcomingMatches().get(i).scoreHome);
                        send(send_url);
                        
                        send_url = String.format(url, model.getInput(inputKey), "Gast_2", model.getUpcomingMatches().get(i).scoreAway);
                        send(send_url);
                        
                        send_url = String.format(url, model.getInput(inputKey), "Zeit_2", model.getUpcomingMatches().get(i).actualTime);
                        send(send_url);
                    }
                }
            }
            view.writeLogMessage("Daten gesendet");
        }
        else
        {
            view.writeLogMessage("Keine Daten verfuegbar");
        }
    }
    
    public void getData() {
        boolean success = model.fetchUpcomingMatches(Long.valueOf(view.getGameDay()));
        if(success) {
            view.updateMatchInfo(model.getUpcomingMatchesAsString());
            view.writeLogMessage("Daten abgerufen");
        } else {
            view.writeLogMessage("Keine Daten verfuegbar");
        }
    }
    
    private void send(String url) throws IOException, ConnectException {
        url = url.replaceAll(" ", "+");
        //view.writeLogMessage(url);
        URL vmixURL = new URL(url);
        
    	HttpURLConnection vmixCon = (HttpURLConnection) vmixURL.openConnection();
    	vmixCon.setRequestMethod("GET");
    	vmixCon.connect();
    	
    	/*int returnCode =*/ vmixCon.getResponseCode();
    }
    
    public void toggleBackgroundWorker(boolean enable) {
        if (enable) {
            view.writeLogMessage("Automatischer Upload aktiviert");
            worker = new BackgroundWorker(this, view);
            worker.start();
        } else {
            view.writeLogMessage("Automatischer Upload deaktiviert");
            worker.interrupt();
        }
    }
    
    /**
     * Listener for Fetch data button
     */
    class DataFetchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getData();
        }
    }
    
    class DataSendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                sendData();
            } catch (IOException e1) {
                view.writeLogMessage("Daten konnten nicht gesendet werden");
            }
        }
    }
    
    class AutoUpdateChangedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            toggleBackgroundWorker(view.isAutoUpdateEnabled());
        }
    }
}

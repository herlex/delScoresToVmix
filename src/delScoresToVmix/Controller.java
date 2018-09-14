package delScoresToVmix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller {
	private Model model;
	private View view;
	private BackgroundWorker worker;
	
	public static void main(String[] args) {
        Controller controller = new Controller();
    }
	
	public Controller() {
		model = new Model();
        view = new View( model );

        initView();
        connectListeners();
        
        view.setVisible(true);
	}
	
	private void initView() {
		view.setVmixIp("127.0.0.1");
        view.setVmixPort("8088");
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
	
	public void sendData() throws IOException {
		if(!model.getUpcomingMatches().isEmpty()) {
			String url = "http://" + view.getVmixIp() + ":" + view.getVmixPort() + "/API/?Function=SetText&Input=%s&SelectedName=%s&Value=%s";
			for(int i = 0; i < 7; ++i) {
				String inputKey = view.getActiveInputKey(i);
				//if(inputKey != "null") {
					url = String.format(url, model.getInput(inputKey), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).teamHome);
					send(url);
					
					url = String.format(url, model.getInput(inputKey), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).teamAway);
					send(url);
					
					url = String.format(url, model.getInput(inputKey), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).scoreHome);
					send(url);
					
					url = String.format(url, model.getInput(inputKey), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).scoreAway);
					send(url);
					
					url = String.format(url, model.getInput(inputKey), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).liveTime);
					send(url);
				//}
			}
			view.writeLogMessage("Daten gesendet");
		}
		else
		{
			view.writeLogMessage("Keine Daten verfuegbar");
		}
	}
	
	public void getData() {
		model.fetchUpcomingMatches();
        view.updateMatchInfo(model.getUpcomingMatchesAsString());
        view.writeLogMessage("Daten abgerufen");
	}
	
	private void send(String url) throws IOException {
		URL vmixURL = new URL(url);
		HttpURLConnection vmixCon = (HttpURLConnection) vmixURL.openConnection();
		vmixCon.setRequestMethod("GET");
		vmixCon.connect();

		int code = vmixCon.getResponseCode();
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

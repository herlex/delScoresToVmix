package delScoresToVmix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import flashscoreScraper.MatchInfo;

public class Controller {
	private Model model;
	private View view;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		connectListeners();
	}
	
	private void connectListeners() {
		view.addDataFetchListener(new DataFetchListener());
		view.addDataSendListener(new DataSendListener());
		view.addSettingsChangedListener(new SettingsChangedListener());
		view.addInputChangeListener(new InputChangeListener());
		view.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            model.shutdown();
	        }
	    });
	}
	
	public void sendDataToVmix() throws IOException {
		String url = "http://" + model.vmixIP + ":" + model.vmixPort + "/API/?Function=SetText&Input=%s&SelectedName=%s&Value=%s";
		for(int i = 0; i < model.getUpcomingMatches().size(); ++i) {
			url = String.format(url, model.getActiveInputs().get(i), "INPUTTEXTNAME", model.getUpcomingMatches().get(i).teamHome);
			send(url);
		}
	}
	
	private void send(String url) throws IOException {
		URL vmixURL = new URL(url);
		HttpURLConnection vmixCon = (HttpURLConnection) vmixURL.openConnection();
	}
	
	/**
	 * Listener for Fetch data button
	 */
	class DataFetchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	model.fetchUpcomingMatches();
            view.updateMatchInfo(model.getUpcomingMatchesAsString());
        }
    }
	
	class DataSendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	try {
				sendDataToVmix();
			} catch (IOException e1) {
				// Nothing to do
				// Maybe inform gui?
			}
        }
    }
	
	class SettingsChangedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	view.syncSettings();
        }
    }
	
	class InputChangeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.updateActiveInputs(view.getInputs());
        }
    }
}

package delScoresToVmix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

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
		view.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            model.shutdown();
	        }
	    });
	}
	
	/**
	 * Listener for Fetch data button
	 */
	class DataFetchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.updateMatchInfo(model.getUpcomingMatches());
        }
    }
}

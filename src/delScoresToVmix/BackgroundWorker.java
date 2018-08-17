package delScoresToVmix;

public class BackgroundWorker extends Thread {

	Controller controller;
	View view;

	public BackgroundWorker(Controller controller, View view) {
		this.controller = controller;
		this.view = view;
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(10000L);
				controller.getData();
				//controller.sendData();
			} catch (InterruptedException e) {
				//GUI.setStatus("automatischer Upload deaktiviert");
				break;
			}
		}
	}
}

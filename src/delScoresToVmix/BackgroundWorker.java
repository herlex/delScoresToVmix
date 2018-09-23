package delScoresToVmix;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

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
            	int timeOut = view.getAutoUpdateInterval();
                Thread.sleep(TimeUnit.SECONDS.toMillis(timeOut));
                controller.getData();
                controller.sendData();
            } catch (InterruptedException | IOException e) {
                view.writeLogMessage("Daten konnten nicht gesendet werden. Laeuft Vmix?");
            }
        }
    }
}

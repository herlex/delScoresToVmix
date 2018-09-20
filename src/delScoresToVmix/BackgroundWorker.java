package delScoresToVmix;

import java.io.IOException;
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
                Thread.sleep(TimeUnit.SECONDS.toMillis(view.getAutoUpdateInterval()));
                controller.getData();
                controller.sendData();
            } catch (InterruptedException | IOException e) {
                //view.writeLogMessage("FAIL");
                break;
            }
        }
    }
}

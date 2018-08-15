package delScoresToVmix;

public class Main {
    //... Create model, view, and controller.  They are
    //    created once here and passed to the parts that
    //    need them so there is only one copy of each.
    public static void main(String[] args) {
        
    	System.setProperty("webdriver.gecko.driver", "C:\\downloads\\geckodriver-v0.21.0-win64\\geckodriver.exe");
    	
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
        
        view.setVisible(true);
    }
}

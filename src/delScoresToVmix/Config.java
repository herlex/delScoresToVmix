package delScoresToVmix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    
    private final String configPath = "settings.cfg";
    private Map<String, String> inputs = new HashMap<String, String>();
    private String geckoDriverPath = "";
    private boolean showBrowser = true;
    
    public Config() {
        inputs.put("null", "null");
    }
    
    public boolean read() {
        File configFile = new File(configPath);
        Scanner sc;
        String category = "";
        try {
            sc = new Scanner(configFile);
            while(sc.hasNextLine()) {
                String newLine = sc.nextLine();
                if(newLine.equalsIgnoreCase("[INPUTS]") ||
                	newLine.equalsIgnoreCase("[Paths]") ||
                	newLine.equalsIgnoreCase("[MISC]")) {
                    category = newLine;
                    continue;
                }
                
                String[] key_value = newLine.split("=");
                if(category.equalsIgnoreCase("[INPUTS]")) {
                    if(key_value.length == 2) {
                        inputs.put(key_value[0], key_value[1]);
                    }
                }
                else if(category.equalsIgnoreCase("[PATHS]")) {
                    if(key_value.length == 2) {
                        if(key_value[0].equalsIgnoreCase("geckoDir")) {
                            geckoDriverPath = key_value[1];

                            File geckoDriver = new File(geckoDriverPath);
                            if(!geckoDriver.exists() || geckoDriver.isDirectory()) { 
                                return false;
                            }
                        }
                    }
                }
                else if(category.equalsIgnoreCase("[Misc]")) {
                    if(key_value.length == 2) {
                        if(key_value[0].equalsIgnoreCase("showBrowser")) {
                            showBrowser = stringToBool(key_value[1]);
                        }
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        
        return true;
    }
    
    public Map<String, String> getInputs() {
        return inputs;
    }
    
    public String getGeckoDriverPath() {
        return geckoDriverPath;
    }
    
    public boolean showBrowser() {
    	return showBrowser;
    }
    
    private boolean stringToBool(String s) {
    	if(s.equalsIgnoreCase("true")) {
    		return true;
    	}
    	
    	return false;
    }
}

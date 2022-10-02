package delScoresToVmix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    
    private final String configPath = "settings.cfg";
    private Map<String, String> inputs = new HashMap<String, String>();
    
    public Config() {
        inputs.put("null", "null");
    }
    
    public boolean read() {
        File configFile = getConfigFile();
        Scanner sc;
        String category = "";
        try {
            sc = new Scanner(configFile);
            while(sc.hasNextLine()) {
                String newLine = sc.nextLine();
                if(newLine.equalsIgnoreCase("[INPUTS]")) {
                    category = newLine;
                    continue;
                }
                
                String[] key_value = newLine.split("=", 2);
                if(category.equalsIgnoreCase("[INPUTS]")) {
                    if(key_value.length == 2) {
                        inputs.put(key_value[0], key_value[1]);
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        
        return true;
    }
    
    private File getConfigFile() {
    	File configFile = new File(configPath);
    	if(!configFile.exists()) {
    		try {
				configFile.createNewFile();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(configPath));
			    writer.write("[INPUTS]\n");
			    writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	return configFile;
    }
    
    public Map<String, String> getInputs() {
        return inputs;
    }
}

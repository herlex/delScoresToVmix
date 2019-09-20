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
    private String url = "http://m.flashscore.de/eishockey/";
    private String league = "DEUTSCHLAND: DEL";
    
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
                if(newLine.equalsIgnoreCase("[INPUTS]") || newLine.equalsIgnoreCase("[MISC]")) {
                    category = newLine;
                    continue;
                }
                
                String[] key_value = newLine.split("=", 2);
                if(category.equalsIgnoreCase("[INPUTS]")) {
                    if(key_value.length == 2) {
                        inputs.put(key_value[0], key_value[1]);
                    }
                } else if(category.equalsIgnoreCase("[MISC]")) {
                	if(key_value[0].equalsIgnoreCase("url")) {
                		url = key_value[1];
                	} else if(key_value[0].equalsIgnoreCase("league")) {
                		league = key_value[1];
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
			    writer.write("[MISC]\n");
			    writer.write("url=" + url + "\n");
			    writer.write("league=" + league);
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
    
    public String getUrl() {
    	return url;
    }
    
    public String getLeague() {
    	return league;
    }
}

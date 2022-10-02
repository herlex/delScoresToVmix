package delScoresToVmix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data_api.DEL;
import data_api.GameInfo;

public class Model {
    
    private DEL leagueFetcher;
    private Map<String, String> inputs = new HashMap<String, String>();
    
    public boolean isModelReady = false;
    
    public Model() {
    	leagueFetcher = new DEL(1l);
        Config cfg = new Config();
        boolean success = cfg.read();
        
        if(success) {
            inputs = cfg.getInputs();
            
            isModelReady = true;
        }
    }
    
    public List<String> getUpcomingMatchesAsString() {
        List<String> result = new ArrayList<String>();
        for(GameInfo match : leagueFetcher.gameDay) {
            result.add(match.getFormatted());
        }
        
        return result;
    }
    
    public void shutdown() {
        // Nothing to do...
    }
    
    public boolean fetchUpcomingMatches(Long gameDay) {
        if(leagueFetcher != null) {
        	leagueFetcher.refreshGameDay(gameDay);
            if(leagueFetcher.gameDay.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    public List<GameInfo> getUpcomingMatches() {
        return leagueFetcher.gameDay;
    }
    
    public String[] getInputKeys() {
        Set<String> keys = inputs.keySet();
        String[] result = keys.toArray(new String[keys.size()]);
        return result;
    }
    
    public String getInput(String key) {
        return inputs.get(key);
    }
}

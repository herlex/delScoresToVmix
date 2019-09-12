package delScoresToVmix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flashscoreScraper.MatchInfo;
import flashscoreScraper.league.DEL;

public class Model {
    
    private DEL leagueFetcher;
    private List<MatchInfo> upcomingMatches = new ArrayList<MatchInfo>();
    private Map<String, String> inputs = new HashMap<String, String>();
    
    public boolean isModelReady = false;
    
    public Model() {
        Config cfg = new Config();
        boolean success = cfg.read();
        
        if(success) {
            inputs = cfg.getInputs();
            leagueFetcher = new DEL();
            
            isModelReady = true;
        }
    }
    
    public List<String> getUpcomingMatchesAsString() {
        List<String> result = new ArrayList<String>();
        for(MatchInfo match : upcomingMatches) {
            result.add(match.getFormatted());
        }
        
        return result;
    }
    
    public void shutdown() {
        // Nothing to do...
    }
    
    public boolean fetchUpcomingMatches() {
        if(leagueFetcher != null) {
            upcomingMatches = leagueFetcher.getUpcomingMatches();
            if(upcomingMatches.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    public List<MatchInfo> getUpcomingMatches() {
        return upcomingMatches;
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

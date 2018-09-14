package delScoresToVmix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import flashscoreScraper.MatchInfo;
import flashscoreScraper.league.DEL;

public class Model {
	
	private DEL leagueFetcher = new DEL();
	private List<MatchInfo> upcomingMatches = new ArrayList<MatchInfo>();
	private Map<String, String> inputs = new HashMap<String, String>();
	
	public Model() {
		inputs.put("null", "null");
		
		// Read input config
		File config = new File("settings.cfg");
		Scanner sc;
		try {
			sc = new Scanner(config);
			while(sc.hasNextLine()) {
				String[] key_value = sc.nextLine().split("=");
				
				if(key_value.length == 2) {
					inputs.put(key_value[0], key_value[1]);
				}
			}
		} catch(FileNotFoundException e) {
				e.printStackTrace();
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
		leagueFetcher.finalize();
	}
	
	public void fetchUpcomingMatches() {
		upcomingMatches = leagueFetcher.getUpcomingMatches();
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

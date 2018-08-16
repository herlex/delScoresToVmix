package delScoresToVmix;

import java.util.ArrayList;
import java.util.List;

import flashscoreScraper.MatchInfo;
import flashscoreScraper.league.DEL;

public class Model {
	
	private DEL leagueFetcher = new DEL();
	private final String[] inputValues = new String[] {"null", "1.1", "1.2", "2.1", "2.2"};
	private List<String> activeInputs = new ArrayList<String>();
	private List<MatchInfo> upcomingMatches = new ArrayList<MatchInfo>();
	public String vmixIP = "127.0.0.1";
	public String vmixPort = "8088";
	
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
	
	public String[] getInputs() {
		return inputValues;
	}
	
	public void updateActiveInputs(List<String> inputs) {
		activeInputs = inputs;
	}
	
	public void fetchUpcomingMatches() {
		upcomingMatches = leagueFetcher.getUpcomingMatches();
	}
	
	public List<MatchInfo> getUpcomingMatches() {
		return upcomingMatches;
	}
	
	public List<String> getActiveInputs() {
		return activeInputs;
	}
}

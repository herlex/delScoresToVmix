package delScoresToVmix;

import java.util.ArrayList;
import java.util.List;

import flashscoreScraper.MatchInfo;
import flashscoreScraper.league.DEL;

public class Model {
	
	private DEL leagueFetcher = new DEL();
	private List<MatchInfo> upcomingMatches = new ArrayList<MatchInfo>();
	
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
}

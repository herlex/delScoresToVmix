package delScoresToVmix;

import java.util.ArrayList;
import java.util.List;

import flashscoreScraper.MatchInfo;
import flashscoreScraper.league.DEL;

public class Model {
	private DEL leagueFetcher = new DEL();
	
	public List<String> getUpcomingMatches() {
		List<MatchInfo> matches = leagueFetcher.getUpcomingMatches();
		
		List<String> result = new ArrayList<String>();
		for(MatchInfo match : matches) {
			result.add(match.getFormatted());
		}
		
		return result;
	}
	
	public void shutdown() {
		leagueFetcher.finalize();
	}
}

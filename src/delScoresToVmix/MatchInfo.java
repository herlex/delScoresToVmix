package delScoresToVmix;

public class MatchInfo {
    public String teamHome;
    public String teamAway;
    public String scoreHome;
    public String scoreAway;
    public String liveTime;
    public String startTime;
    
    public boolean isLive = false;
    
    public MatchInfo() {
        teamHome = "#teamHome#";
        teamAway = "#teamAway#";
        scoreHome = "0";
        scoreAway = "0";
        liveTime = "";
        startTime = "0";
    }
    
    public String getLiveTime() {
        if(helperFunctions.isStringNullOrWhiteSpace(liveTime)) {
            return "Vor dem Spiel";
        } else {
            return liveTime;
        }
    }
    
    public void cleanUp() {
    	startTime = startTime.replace("\n", "").replace("\r", "");
        liveTime = liveTime.replace("\n",  "").replace("\r", "");
        teamHome = teamHome.replace("\n",  "").replace("\r", "");
        teamAway = teamAway.replace("\n",  "").replace("\r", "");
        scoreHome = scoreHome.replace("\n",  "").replace("\r", "").replace("TOR", "");
        scoreAway = scoreAway.replace("\n",  "").replace("\r", "").replace("TOR", "");
        
        if(liveTime.equalsIgnoreCase("Verlängerung")) {
        	liveTime = "Verlaengerung";
        } else if(liveTime.equalsIgnoreCase("Nach Verlängerung")) {
        	liveTime = "n.V.";
        } else if(liveTime.equalsIgnoreCase("Nach Penaltyschiessen")) {
        	liveTime = "n.P.";
        }
        else {
            String[] liveTimeSplitted = liveTime.split("(?<=Drittel) ");
            if(liveTimeSplitted.length != 0) {
                liveTime = liveTimeSplitted[0];
            }
        }
    }

    public String getFormatted() {
    	if(isLive) {
    		return "LIVE | " + getLiveTime() + " | " + teamHome + " " + scoreHome + " - " + scoreAway + " " + teamAway;
    	} else {
    		return getLiveTime() + " | " + startTime + " Uhr | " + teamHome + " " + scoreHome + " - " + scoreAway + " " + teamAway;
    	}
    }
}
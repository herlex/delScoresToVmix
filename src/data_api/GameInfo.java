package data_api;

public class GameInfo {
	public String actualTime;
	public String teamHome;
	public String teamAway;
	public String scoreHome;
	public String scoreAway;
	
	public String apiUrl = "https://s3-eu-west-1.amazonaws.com/de.hokejovyzapis.cz/matches/{game}/game-header.json";
	public Long gameID;
	
	public GameInfo(Long gameID) {
		this.gameID = gameID;
		apiUrl = apiUrl.replace("{game}", String.valueOf(gameID));
    }
	
	public String getFormatted() {
		String time = actualTime;
		if(actualTime.contains("n. Verlängerung"))
		{
			time = "n.V.";
		}
		else if(actualTime.contains("n. Penalty"))
		{
			time = "n.P.";
		}
		else if(actualTime.contains("Ende"))
		{
			time = "Fulltime";
		}
		
    	return time + " | " + teamHome + " " + scoreHome + " - " + scoreAway + " " + teamAway;
    }
}

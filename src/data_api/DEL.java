package data_api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DEL {
	private Long gameDayNo;
	
	public List<GameInfo> gameDay = new ArrayList<GameInfo>();
	private Client client;
	
	public DEL(Long gameDay) {
		gameDayNo = gameDay;
		final File folder = new File("del");
		try {
			initGameDay(folder);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client = ClientBuilder.newClient();
	}
	
	public void refreshGameDay(Long no) {
		GameInfo gameToRemove = null;
		if(no != gameDayNo)
		{
			gameDay.clear();
			gameDayNo = no;
			final File folder = new File("del");
			try {
				initGameDay(folder);
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONParser parser = new JSONParser();
		for(GameInfo game : gameDay)
		{
			try {
				Response response = client.target(game.apiUrl).request().get();
	
				JSONObject gameInfo = (JSONObject) parser.parse(response.readEntity(String.class));
				game.actualTime = (String) gameInfo.get("actualTimeName");
				game.teamHome = (String) ((JSONObject) ((JSONObject) gameInfo.get("teamInfo")).get("home")).get("name");
				game.teamAway = (String) ((JSONObject) ((JSONObject) gameInfo.get("teamInfo")).get("visitor")).get("name");
				game.scoreHome = String.valueOf((Long) ((JSONObject) ((JSONObject) ((JSONObject) gameInfo.get("results")).get("score")).get("final")).get("score_home"));
				game.scoreAway = String.valueOf((Long) ((JSONObject) ((JSONObject) ((JSONObject) gameInfo.get("results")).get("score")).get("final")).get("score_guest"));
								
			} catch (Exception e) {
				gameToRemove = game;
			}
		}
		
		gameDay.remove(gameToRemove);
	}
	
	private void initGameDay(final File folder) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
	    for (final File fileEntry : folder.listFiles())
	    {
	        if (fileEntry.isDirectory())
	        {
	        	initGameDay(fileEntry);
	        }
	        else
	        {
	        	String path = fileEntry.getAbsolutePath();
	        	JSONObject games = (JSONObject) parser.parse(new FileReader(path));
	        	JSONArray matches = (JSONArray)games.get("matches");
	        	JSONObject game = null;
	        	for(int i = 0; i < matches.size(); ++i)
	        	{
	        		game = (JSONObject)matches.get(i);
	        		Long round = Long.valueOf((String)game.get("round"));
	        		if(round.equals(gameDayNo))
    				{
	        			break;
    				}
	        	}
				
	        	Long gameID = (Long) game.get("id");
	        	boolean skip = false;
	        	for(GameInfo g : gameDay)
	        	{
	        		if(g.gameID.equals(gameID))
	        		{
	        			skip = true;
	        			break;
	        		}
	        	}
	        	
	        	if(!skip)
	        	{
	        		gameDay.add(new GameInfo(gameID));
	        	}
	        }
	    }
	}
}

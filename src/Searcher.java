import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.*;
import com.BoxOfC.LevenshteinAutomaton.LevenshteinAutomaton;

public class Searcher {
	
	private static int EDIT_DISTANCE_THRESHOLD = 3;

	
	public static JSONObject[] findCards(String[] cards, JSONObject data){
		
		ArrayList<JSONObject> cardData = new ArrayList<JSONObject>();
		
		for(String card : cards){
			cardData.addAll(findCard(WordUtils.capitalize(card), data));
		}
		
		return cardData.toArray(new JSONObject[cardData.size()]);
		
	}
	
	public static List<JSONObject> findCard(String card, JSONObject data){
		
		
		ArrayList<JSONObject> matches = new ArrayList<JSONObject>();
		
		
		
		//If we have an exact match, just return that.
		if(data.containsKey(card)){
			matches.add((JSONObject) data.get(card));
			return matches;
		}
		
		//Otherwise we iterate through the list to find all cards within an edit distance of the given threshold.
		for(Object key : data.keySet()){
			
			int ed = LevenshteinAutomaton.computeEditDistance(card, (String) key);
				
			if(Math.abs(ed) < EDIT_DISTANCE_THRESHOLD){
				matches.add((JSONObject) data.get(key));
					
			}		
		}
		
		
		Collections.sort(matches, (c1, c2) ->
					Math.abs(LevenshteinAutomaton.computeEditDistance(card, (String) c1.get("name"))) - 
					Math.abs(LevenshteinAutomaton.computeEditDistance(card, (String) c2.get("name")))
				);
		
		return matches.subList(0, Math.min(matches.size(), 3));
	}

	public static String[] sumCards(JSONObject[] cardData) {
		String[] s = new String[cardData.length];
		int i = 0;
		for(JSONObject obj : cardData){
			if(i > 5)
				break;
			s[i++] = sumCard(obj);
		}
		return s;
	}
	
	public static String sumCard(JSONObject card){
		String name = (String) card.get("name");
		String cost = (String) card.get("manaCost");
		String text = (String) card.get("text");
		String type = (String) card.get("type");
		
		String reply = "**"+name+"** \t" + cost + "\n" + type; 
		
		if(card.containsKey("power")){
			String power = (String) card.get("power");
			String toughness = (String) card.get("toughness");
			reply += " \t[" + power + "/" + toughness + "]\n";
		}else{
			reply += "\n";
		}
		
		reply += text;
		
		return reply;
	}

}

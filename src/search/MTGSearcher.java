package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;

import data_package.DataStoreJWDist; 

public class MTGSearcher implements Searcher {
	
	private static double EDIT_DISTANCE_THRESHOLD = 0.85;

	
	public List<JSONObject> findCardListByName(String card, JSONObject data){
		
		List<JSONObject> results = new ArrayList<JSONObject>();
		
		//A list of all matching cards
		ArrayList<DataStoreJWDist> edit_distance_macthes = new ArrayList<DataStoreJWDist>();
		
		//Otherwise we iterate through the list to find all cards within an edit distance of the given threshold.
		for(Object key : data.keySet()){
			
			String name = (String) key;
			
			//If we find an exact match, just return that.
			if(StringUtils.equalsIgnoreCase(name, card)){
				//Make sure we are not returning anything else.
				results.clear();
				results.add((JSONObject)data.get(key));
				return results;
			}
			//Get the difference between this string and the target string as a percentage.
			double dist = StringUtils.getJaroWinklerDistance(card, name);
			
			if(dist > EDIT_DISTANCE_THRESHOLD){
				edit_distance_macthes.add(new DataStoreJWDist((JSONObject) data.get(key), dist));
			}
			//Also if there is an exact substring.
			else if (StringUtils.containsIgnoreCase(name, card)){
				results.add((JSONObject)data.get(key));
				
			}
		}
		results.addAll(DataStoreJWDist.getSortedList(edit_distance_macthes, -1));
		Collections.sort(results, (c1, c2) -> sortByLegendary(c1, c2));
		
		
		return results.subList(0, Math.min(results.size(), 3));
	}
	
	private static int sortByLegendary(JSONObject c1, JSONObject c2){
		int sum = 0;
		
		JSONArray c1_types  = (JSONArray)c1.get("supertypes");
		JSONArray c2_types = (JSONArray)c2.get("supertypes");
		
		if(c1_types != null && c1_types.lastIndexOf("Legendary") > -1){
			sum -= 100;
		}
		
		if(c2_types != null && c2_types.lastIndexOf("Legendary") > -1){
			sum += 100;
		}
		return sum;
	}

	public String[] cardListToString(List<JSONObject> cardData) {
		String[] s = new String[cardData.size()];
		int i = 0;
		for(JSONObject obj : cardData){
			if(i > 5)
				break;
			s[i++] = cardToString(obj);
		}
		return s;
	}
	
	public String cardToString(JSONObject card){
		String name = (String) card.get("name");
		String cost = (String) card.get("manaCost");
		String text = (String) card.get("text");
		String type = (String) card.get("type");
		
		if(cost == null)
			cost = "";
		if(text == null)
			text = "";
		
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

	@Override
	public String failedSearchMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printCardList(List<JSONObject> card_data) {
		String[] card_summery = cardListToString(card_data);
		 
		 if(card_summery.length > 0){
			 String message = "\n";
			 for(String s : card_summery){
				 message += s + "\n";
			 }
			 return (message);
		 }
		 else{
			 return ("I know none by that name");
		 }
	}

}

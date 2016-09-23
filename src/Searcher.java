import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.*; 

public class Searcher {
	
	private static double EDIT_DISTANCE_THRESHOLD = 0.85;

	
	public static JSONObject[] findCards(String[] cards, JSONObject data){
		
		ArrayList<JSONObject> cardData = new ArrayList<JSONObject>();
		
		for(String card : cards){
			cardData.addAll(findCard(card, data));
		}
		
		return cardData.toArray(new JSONObject[cardData.size()]);
		
	}
	
	public static List<JSONObject> findCard(String card, JSONObject data){
		
		List<JSONObject> results = new ArrayList<JSONObject>();
		
		
		ArrayList<DataStoreJWDist> matches = new ArrayList<DataStoreJWDist>();
		
		//Otherwise we iterate through the list to find all cards within an edit distance of the given threshold.
		for(Object key : data.keySet()){
						
			double dist = StringUtils.getJaroWinklerDistance(card, (String)key);
			
			if(dist > EDIT_DISTANCE_THRESHOLD){
				matches.add(new DataStoreJWDist((JSONObject) data.get(key), dist));
			}
			
			else if (StringUtils.containsIgnoreCase((String)key, card)){
				results.add((JSONObject)data.get(key));
				
			}
		}
		results.addAll(DataStoreJWDist.getSortedList(matches, -1));
		Collections.sort(results, (c1, c2) -> sortByLegendary(c1, c2));
		
		
		return results.subList(0, Math.min(results.size(), 3));
	}
	
	public static Object getIgnoreCase(JSONObject jobj, String key) {

	    Iterator<String> iter = jobj.keySet().iterator();
	    while (iter.hasNext()) {
	        String key1 = iter.next();
	        if (key1.equalsIgnoreCase(key)) {
	            return jobj.get(key1);
	        }
	    }

	    return null;

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

}

package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;

import data_package.DataStoreJWDist;
import updater.MTGJsonUpdater; 

public class MTGSearcher extends Searcher {
	
	public List<JSONObject> findCardListByName(String card){
		
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
		
		
		return getResultSubList(results);
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
	public void loadData() {
		System.out.println("Loading Data Set");
		MTGJsonUpdater update = new MTGJsonUpdater();
		//Update the local data if it is out of date.
		update.update();
		//Once we know that our data is up to date we load it.
		data = update.loadJSONFile();
		System.out.println("Data: " + data);			
	}

	@Override
	public void setData(Object data) {
		super.data = (JSONObject) data;
		
	}

}

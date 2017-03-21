package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;

import data_package.DataStoreJWDist;
import updater.MTGJsonUpdater; 

public class MTGSearcher extends Searcher {
	
	private static String[] cull_list = {"Vanguard", "Conspiracy", "Phenomenon", "Plane", "Scheme"};
	
	public List<JSONObject> findCardListByName(String card){
		
		List<JSONObject> results = new ArrayList<JSONObject>();
		
		//A list of all matching cards
		ArrayList<DataStoreJWDist> edit_distance_matches = new ArrayList<DataStoreJWDist>();
		
		//Otherwise we iterate through the list to find all cards within an edit distance of the given threshold.
		for(Object key : data.keySet()){
			
			String name = (String) key;
			
			JSONObject card_data = (JSONObject)data.get(key);
			
			//If we find an exact match, just return that.
						
			if(StringUtils.equalsIgnoreCase(name, card) && !StringUtils.containsAny((String) card_data.get("type"), cull_list)){
				//Make sure we are not returning anything else.
				//We have the exact card we want.
				results.clear();
				results.add(card_data);
				System.out.println(name);
				return results;
			}
			//Get the difference between this string and the target string as a percentage.
			double dist = StringUtils.getJaroWinklerDistance(card, name);
			
			
			if(dist > EDIT_DISTANCE_THRESHOLD){
				edit_distance_matches.add(new DataStoreJWDist(card_data, dist));
			}
			else if(name.contains(",")){
				String substring_title = name.split(",")[0];
				//System.out.println(substring_title);
				double substring_dist = StringUtils.getJaroWinklerDistance(card, substring_title);
				
				if(substring_dist > EDIT_DISTANCE_THRESHOLD){
					edit_distance_matches.add(new DataStoreJWDist(card_data, substring_dist));
				}
			}
			//Also if there is an exact substring.
			else if (StringUtils.containsIgnoreCase(name, card)){
				results.add(card_data);
			}
		}
		System.out.println("Starting Culling");
		results = cullSpecialCardTypes(results);
		System.out.println("Finished Culling");
		
		results.addAll(DataStoreJWDist.getSortedList(edit_distance_matches, -1));
		
		//Priorities Legendary creatures, as these are special characters in MTG, and are more likely the subject of the search.
		Collections.sort(results, (c1, c2) -> sortByLegendary(c1, c2));
		
		
		return getResultSubList(results);
	}
	
	private static List<JSONObject> cullSpecialCardTypes(List<JSONObject> results){
		
		List<JSONObject> culled_results = new ArrayList<JSONObject>();
		System.out.println(results);
		for(JSONObject obj : results){
			String type_line = (String) obj.get("type");
			System.out.println(type_line);
			if(!StringUtils.containsAny(type_line, cull_list)){
				System.out.println("Culled");
				culled_results.add(obj);
			}
			
		}
		
		return culled_results;
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
		
		String reply = "__**"+name+"**__ \t" + cost + "\n" + type; 
		
		if(card.containsKey("power")){
			String power = (String) card.get("power");
			String toughness = (String) card.get("toughness");
			reply += " \t[" + power + "/" + toughness + "]\n";
		}else if(card.containsKey("loyalty")){
			Integer loyalty = (int) card.get("loyalty");
			
			reply += "\t{" + loyalty + "}";
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
	}

	@Override
	public void setData(Object data) {
		super.data = (JSONObject) data;
		
	}

}

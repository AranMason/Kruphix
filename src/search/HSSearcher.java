package search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data_package.DataStoreJWDist;
import updater.HSJsonUpdater;

public class HSSearcher extends Searcher{
	
	private JSONArray hs_data;

	@Override
	public List<JSONObject> findCardListByName(String card) {
		
		List<JSONObject> substring_matches = new ArrayList<JSONObject>();
		List<DataStoreJWDist> edit_dist_matches = new ArrayList<DataStoreJWDist>();
		
		Iterator<JSONObject> hs_search = hs_data.iterator();
		while(hs_search.hasNext()){
			JSONObject next = hs_search.next();
			
			String name = (String)next.get("name");
			
			if(StringUtils.equalsIgnoreCase(name, card)){
				List<JSONObject> result = new ArrayList<JSONObject>();
				result.add(next);
				return result;
			}
			else if(StringUtils.containsIgnoreCase(name, card)){
				substring_matches.add(next);
			} else{
				double edit_dist = StringUtils.getJaroWinklerDistance(name, card);
				if(edit_dist > EDIT_DISTANCE_THRESHOLD){
					edit_dist_matches.add(new DataStoreJWDist(next, edit_dist));
				}
			}
		}
		
		substring_matches.addAll(DataStoreJWDist.getSortedList(edit_dist_matches, -1));
		
		return getResultSubList(substring_matches);
	}

	@Override
	public String cardToString(JSONObject card) {
		String name = (String)card.get("name");
		String text = fixBolding((String)card.get("text"));
		String type = (String)card.get("type");
		
		String output = "**" + name + "**" + (card.containsKey("cost") ? (" - " + card.get("cost")) : "") + "\n" +
				type + "\n" +
				text;
		
		if(type.equals("MINION")){
			output += "\n[" + card.get("attack") + "/" + card.get("health") + "]";
		} else if(type.equals("WEAPON")){
			output += "\n[" + card.get("attack") + "/" + card.get("durability") + "]";
			
		}
		
		return output;
	}
	
	private String fixBolding(String text){
		return text.replaceAll("<\\/*b>", "**");
	}
	
	@Override
	public void setData(Object o){
		this.hs_data = (JSONArray)o;
	}

	@Override
	public void loadData() {
		HSJsonUpdater hs = new HSJsonUpdater();
		
		hs.update();
		
		this.setData(hs.loadJSONFile());
	}

}

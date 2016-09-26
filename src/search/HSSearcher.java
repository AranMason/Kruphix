package search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import data_package.DataStoreJWDist;
import updater.HSJsonUpdater;

public class HSSearcher extends Searcher{

	@Override
	public List<JSONObject> findCardListByName(String card) {
		
		List<JSONObject> substring_matches = new ArrayList<JSONObject>();
		List<DataStoreJWDist> edit_dist_matches = new ArrayList<DataStoreJWDist>();
		
		for(Object key : super.data.keySet()){
			if(StringUtils.equalsIgnoreCase((String)key, card)){
				List<JSONObject> result = new ArrayList<JSONObject>();
				result.add((JSONObject) data.get(key));
				return result;
			}
			else if(StringUtils.containsIgnoreCase((String)key, card)){
				substring_matches.add((JSONObject) data.get(key));
			} else{
				double edit_dist = StringUtils.getJaroWinklerDistance((String)key, card);
				if(edit_dist > EDIT_DISTANCE_THRESHOLD){
					edit_dist_matches.add(new DataStoreJWDist((JSONObject)data.get(key), edit_dist));
				}
			}
		}
		
		substring_matches.addAll(DataStoreJWDist.getSortedList(edit_dist_matches, -1));
		
		return getResultSubList(substring_matches);
	}

	@Override
	public String cardToString(JSONObject card) {
		String name = (String)card.get("name");
		int cost = (int)card.get("cost");
		String text = (String)card.get("text");
		String type = (String)card.get("type");
		
		String output = "**" + name + "** - " + cost + "\n" +
				type + "\n" +
				text;
		
		if(type.equals("MINION")){
			output += "\n[" + card.get("attack") + "/" + card.get("health") + "]";
		} else if(type.equals("WEAPON")){
			output += "\n[" + card.get("attack") + "/" + card.get("durability") + "]";
			
		}
		
		return output;
	}

	@Override
	public void loadData() {
		HSJsonUpdater hs = new HSJsonUpdater();
		
		hs.update();
		
		super.setData(hs.loadJSONFile());
	}

}

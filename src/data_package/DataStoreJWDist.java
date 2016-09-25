package data_package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;

public class DataStoreJWDist {
	
	private JSONObject obj;
	private double dist;
	
	public DataStoreJWDist(JSONObject obj, double dist){
		this.obj = obj;
		this.dist = dist;
	}
	
	public JSONObject getObject(){
		return obj;
	}
	
	public double getDistance(){
		return dist;
	}
	
	public static List<JSONObject> getSortedList(List<DataStoreJWDist> data, int MAX_RESULTS){
		
		Collections.sort(data, (d1, d2) ->
					(int)((d2.getDistance() - d1.getDistance())*100)
				);
		
		List<JSONObject> results = new ArrayList<JSONObject>();
		
		for(int i = 0; (i < MAX_RESULTS || MAX_RESULTS == -1) && i < data.size(); i++){
			results.add(data.get(i).getObject());
		}
		
		return results;
		
	}

}

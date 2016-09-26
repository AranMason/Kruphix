package search;

import java.util.List;
import java.util.Timer;

import org.json.simple.JSONObject;

import updater.TimeUpdateChecker;

public abstract class Searcher {
	
	public Searcher(){
		Timer t = new Timer();
		t.schedule(new TimeUpdateChecker(this), 0);
	}
		
	public abstract List<JSONObject> findCardListByName(String card);
	
	public abstract String[] cardListToString(List<JSONObject> cardData);
	
	public abstract String cardToString(JSONObject card);

	public abstract String printCardList(List<JSONObject> card_data);
	
	public abstract void setData(JSONObject data);
	
	public abstract void loadData();
}

package search;

import java.util.List;

import org.json.simple.JSONObject;

public abstract interface Searcher {
		
	public abstract List<JSONObject> findCardListByName(String card, JSONObject data);
	
	public abstract String[] cardListToString(List<JSONObject> cardData);
	
	public String cardToString(JSONObject card);
	
	public String failedSearchMessage();

	String printCardList(List<JSONObject> card_data);
}

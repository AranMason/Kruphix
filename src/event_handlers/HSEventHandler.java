package event_handlers;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import parsers.CardSearchParser;
import parsers.CardSearchParser.SEARCH_REGEX;
import search.HSSearcher;
import search.Searcher;

public class HSEventHandler extends ListenerAdapter{

	private Searcher HS_SEARCH;
	
	public HSEventHandler(){
		HS_SEARCH = new HSSearcher();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		if (CardSearchParser.containsMatches(
				 event.getMessage().getContent(), 
				 SEARCH_REGEX.HEARTHSTONE)){
			String[] cards = CardSearchParser
					.getMatchingCards(event.getMessage().getContent(), 
							SEARCH_REGEX.HEARTHSTONE);
			List<JSONObject> card_data = new ArrayList<JSONObject>();
			
			for(String card_name : cards){
				card_data.addAll(HS_SEARCH.findCardListByName(card_name));
			}
					 
			event.getChannel().sendMessage(HS_SEARCH.printCardList(card_data));
		 }
	}
}

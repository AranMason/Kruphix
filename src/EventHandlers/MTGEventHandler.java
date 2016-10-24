package EventHandlers;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import parsers.CardSearchParser;
import parsers.CardSearchParser.SEARCH_REGEX;
import search.MTGSearcher;
import search.Searcher;

public class MTGEventHandler extends ListenerAdapter{
	
	
	private Searcher MTG_SEARCH;
	
	public MTGEventHandler() {
		MTG_SEARCH = new MTGSearcher();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
	
			if (CardSearchParser.containsMatches(
				 event.getMessage().getContent(), 
				 SEARCH_REGEX.MAGIC_THE_GATHERING)){
				//We parse the string that we need to match out of the message. Using a set regex.
				String[] cards = CardSearchParser
						 .getMatchingCards(event.getMessage().getContent(), 
								 SEARCH_REGEX.MAGIC_THE_GATHERING);
					 
				//
				List<JSONObject> cardData = new ArrayList<JSONObject>();
				//For each of the entries in the message to search for, we search for a list of potential matches.
				for(String card_name : cards){
					 cardData.addAll(MTG_SEARCH.findCardListByName(card_name));
				}
					 
				//We ask the Searcher to summaries the list of cards for printing as a message.
				event.getChannel().sendMessage(MTG_SEARCH.printCardList(cardData));
			 
			} 
	}

}

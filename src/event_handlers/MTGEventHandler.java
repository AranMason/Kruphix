package event_handlers;

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
		
		String message = event.getMessage().getContent();
	
			if (CardSearchParser.containsMatches(
				 message, 
				 SEARCH_REGEX.MAGIC_THE_GATHERING)){
				
				//We parse the string that we need to match out of the message. Using a set regex.
				String[] cards = CardSearchParser
						 .getMatchingCards(message, 
								 SEARCH_REGEX.MAGIC_THE_GATHERING);
					 
				//
				
				//For each of the entries in the message to search for, we search for a list of potential matches.
				//Each card request is its own message.
				for(int i = 0; i < cards.length; i++){
					
					
					
					String output = "";
					
					List<JSONObject> cardData = new ArrayList<JSONObject>();
					//We find potential cards
					cardData.addAll(MTG_SEARCH.findCardListByName(cards[i]));
					//We add the card to the message
					output += MTG_SEARCH.printCardList(cardData);
					//Send the message
					event.getChannel().sendMessage(output);			
					
				}				
			 
			} 
	}

}

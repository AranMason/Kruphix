
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;
import channel_moderation.ChannelHost;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import parsers.CardSearchParser;
import parsers.CardSearchParser.SEARCH_REGEX;
import search.HSSearcher;
import search.MTGSearcher;
import search.Searcher;




public class Kruphix extends ListenerAdapter{
	
	private ChannelHost CHANNEL_HOST;
	
	private Searcher MTG_SEARCH;
	private Searcher HS_SEARCH;

	public static void main(String[] args) 
	{
		JDA jda;
		try {
			jda = new JDABuilder().setBotToken(args[0]).buildBlocking();
			jda.addEventListener(new Kruphix());
		} catch (Exception e){
			System.err.println(e);
		}
		
	}
	
	public Kruphix(){		
		
		MTG_SEARCH = new MTGSearcher();
		HS_SEARCH = new HSSearcher();
		
		CHANNEL_HOST = new ChannelHost();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		
		
		 if(StringUtils.startsWithIgnoreCase(event.getMessage().getContent(),
				 CHANNEL_HOST.getChannelCreateCommand())){
			
			 //We make sure that the nunber
			 if(event.getGuild().getVoiceChannels().size() < ChannelHost.MAX_NUMBER_OF_CHANNELS)
				 CHANNEL_HOST.createChannel(event);
			 else
				 event.getChannel().sendMessage("Too many channels.");			 
		 }
		 else {
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
}


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.security.auth.login.LoginException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import channel_moderation.ChannelHost;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import parsers.CardSearchParser;
import parsers.FileReader;
import parsers.CardSearchParser.SEARCH_REGEX;
import search.MTGSearcher;
import search.Searcher;
import updater.MTGJsonUpdater;
import updater.TimeUpdateChecker;




public class Kruphix extends ListenerAdapter{
	
	private ChannelHost channel_host;

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
		
		new MTGSearcher().loadData();
		
		this.channel_host = new ChannelHost();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		
		
		 if(event.getMessage().getContent()
				 .startsWith(channel_host.getChannelCreateCommand())){
			
			 //We make sure that the nunber
			 if(event.getGuild().getVoiceChannels().size() < ChannelHost.MAX_NUMBER_OF_CHANNELS)
				 channel_host.createChannel(event);
			 else
				 event.getChannel().sendMessage("Too many channels.");			 
		 }
		 else if (CardSearchParser.containsMatches(
				 event.getMessage().getContent(), 
				 SEARCH_REGEX.MAGIC_THE_GATHERING)){
			 //We create a search object for the given card game
			 Searcher m = new MTGSearcher();
			 //We parse the string that we need to match out of the message. Using a set regex.
			 String[] cards = CardSearchParser
					 .getMatchingCards(event.getMessage().getContent(), 
							 SEARCH_REGEX.MAGIC_THE_GATHERING);
			 
			 //
			 List<JSONObject> cardData = new ArrayList<JSONObject>();
			 //For each of the entries in the message to search for, we search for a list of potential matches.
			 for(String card_name : cards){
				 cardData.addAll(m.findCardListByName(card_name));
			 }
			 //We ask the Searcher to summaries the list of cards for printing as a message.
			 event.getChannel().sendMessage(m.printCardList(cardData));
		 
		 }
		 	
	}
}

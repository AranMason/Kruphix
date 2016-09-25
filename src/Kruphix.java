
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import channel_moderation.ChannelHost;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import parsers.CardSearchParser;
import parsers.CardSearchParser.SEARCH_REGEX;
import search.MTGSearcher;
import search.Searcher;


//https://discordapp.com/oauth2/authorize?client_id=228458079337971722&scope=bot&permissions=0

public class Kruphix extends ListenerAdapter{
	
	private JSONObject data;
	private ChannelHost channel_host;

	public static void main(String[] args) 
	{
		JDA jda;
		try {
			jda = new JDABuilder().setBotToken("MjI4NDU4MDc5MzM3OTcxNzIy.CsU_sA.vkvhsgO5CvvasFHsKt8agQdpGHg").buildBlocking();
			jda.addEventListener(new Kruphix());
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Kruphix(){
		this.loadJSON();
		this.channel_host = new ChannelHost();
	}
	
	public void loadJSON(){
		
		
		JSONParser p = new JSONParser();
		
		try {
			FileInputStream fi = new FileInputStream("data/AllCards.json");
			InputStreamReader f = new InputStreamReader(fi, "UTF-8");
			
			
			data = (JSONObject)p.parse(f);
			
			System.out.println("Loading Cards Complete.");
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				 cardData.addAll(m.findCardListByName(card_name, data));
			 }
			 //We ask the Searcher to summaries the list of cards for printing as a message.
			 event.getChannel().sendMessage(m.printCardList(cardData));
		 
		 }
		 	
	}
}

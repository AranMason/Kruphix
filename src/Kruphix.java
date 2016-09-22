import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;


//https://discordapp.com/oauth2/authorize?client_id=228458079337971722&scope=bot&permissions=0

public class Kruphix extends ListenerAdapter{
	
	private JSONObject data;

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
	}
	
	public void loadJSON(){
		
		
		JSONParser p = new JSONParser();
		
		try {
			FileInputStream fi = new FileInputStream("../Kruphix/data/AllCards.json");
			InputStreamReader f = new InputStreamReader(fi, "UTF-8");
			
			
			data = (JSONObject)p.parse(f);
			
			System.out.println("Loading Cards Complete.");
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 @Override
	 public void onMessageReceived(MessageReceivedEvent event)
	 {
		 String[] cards = MessageParser.getCards(event.getMessage().getContent());
		 
		 JSONObject[] cardData = Searcher.findCards(cards, data);
		 
		 String[] card_summery = Searcher.sumCards(cardData);
		 
		 if(card_summery.length > 0){
			 String message = "\n";
			 for(String s : card_summery){
				 message += s + "\n";
			 }
			 event.getChannel().sendMessage(message);
		 }
		 
		 //event.getChannel().sendMessage(cards.length + "");
		 	
	}
}
package event_handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class MCIEventHandler extends ListenerAdapter{
	
	String BASE_URL = "http://magiccards.info/query?q=";
	String COMMAND = "!mci ";
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		if(StringUtils.startsWithIgnoreCase(event.getMessage().getContent(), COMMAND)){
			String s = event.getMessage().getContent().substring(COMMAND.length(), event.getMessage().getContent().length());
			try {
				event.getChannel().sendMessage(BASE_URL + URLEncoder.encode(s, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

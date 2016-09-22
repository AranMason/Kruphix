package channel_moderation;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ChannelHost {

	private final static String CHANNEL_CREATE_COMMAND = "!server";
	private final static String WIPE_POPUP_CHANNELS = "!wipe";
	
	//Channel ID's and Expiry time in Epoch.
	HashMap<String, Long> hosted_channels = new HashMap<String, Long>();
	
	public ChannelHost(){
		
	}

	public String getChannelCreateCommand() {
		// TODO Auto-generated method stub
		return CHANNEL_CREATE_COMMAND;
	}
	
	public void wipeAllChannels(MessageReceivedEvent event){
		
		for(Entry<String, Long> key : hosted_channels.entrySet()){
			VoiceChannel v = getVoiceChannelById(key.getKey(), event);
			if(v.getUsers().isEmpty()){
				
			}
			
		}
		
	}
	
	private VoiceChannel getVoiceChannelById(String id, MessageReceivedEvent event){
		for(VoiceChannel v : event.getGuild().getVoiceChannels()){
			if(v.getId().equals(id))
				return v;
		}
		return null;
	}
	 

	public void createChannel(MessageReceivedEvent event) {
		
		System.out.println("Creating Channel");
		
		String[] args = event.getMessage().getContent().split("\\s+");
		
		System.out.println("~" + args.length);
		//If we don't have a name we can create a channel.
		if(args.length <= 1){
			event.getChannel().sendMessage("Every new world has a name.");
			return;
		}
		
		for(VoiceChannel v : event.getGuild().getVoiceChannels()){
			if(v.getName().equals(args[1]) || args[1].equals("")){
				event.getChannel().sendMessage("Please be more original");
				return;
			}
		};
		
		event.getGuild().createVoiceChannel(args[1]);
		String id = "";
		for(VoiceChannel v : event.getGuild().getVoiceChannels()){
			if(v.getName().getClass().equals(args[1])){
				id = v.getId();
				break;
			}
		};
		
		//Default set expiry time to be in 12 hours.
		hosted_channels.put(id, System.currentTimeMillis()+43200);
		
		
		
		
		
		
		
		// TODO Auto-generated method stub
		
	}
}

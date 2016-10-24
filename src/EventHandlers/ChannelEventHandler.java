package EventHandlers;

import org.apache.commons.lang3.StringUtils;

import channel_moderation.ChannelHost;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class ChannelEventHandler extends ListenerAdapter{
	
	private ChannelHost CHANNEL_HOST;
	
	public ChannelEventHandler(){
		CHANNEL_HOST = new ChannelHost();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		
		
		 if(StringUtils.startsWithIgnoreCase(event.getMessage().getContent(),
				 CHANNEL_HOST.getChannelCreateCommand())){
			
			 //We make sure that the number
			 if(event.getGuild().getVoiceChannels().size() < ChannelHost.MAX_NUMBER_OF_CHANNELS)
				 CHANNEL_HOST.createChannel(event);
			 else
				 event.getChannel().sendMessage("Too many channels.");			 
		 }
		 	
	}

}

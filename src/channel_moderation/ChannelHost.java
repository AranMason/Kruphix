package channel_moderation;

import java.util.Timer;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.ChannelManager;

public class ChannelHost {

	//The command to invoke the creation of a new voice channel.
	private final static String CHANNEL_CREATE_COMMAND = "!channel";
	//The default name for any new channel, when a name is not given.
	private final static String DEFAULT_CHANNEL_NAME = "TempChannel";

	public String getChannelCreateCommand() {
		return CHANNEL_CREATE_COMMAND;
	}

	public void createChannel(MessageReceivedEvent event) {
		
		String content = event.getMessage().getContent();
		//Get the name of the channel, otherwise use a default name.
		String channel_name = 
				(content.length() > CHANNEL_CREATE_COMMAND.length()+1) ?
						content.substring(CHANNEL_CREATE_COMMAND.length()+1, content.length()) :
							DEFAULT_CHANNEL_NAME;
		//If the channels name exceeds the character limit just crop it to the character limit.				
		if(channel_name.length() > 100)
			channel_name = channel_name.substring(0, 100);
		
		//Create the channel.
		ChannelManager channel = event.getGuild().createVoiceChannel(channel_name);
		
		//Set up a delayed event for the channel to self delete.
		Timer t = new Timer();
		t.schedule(new ChannelHandler(channel), ChannelHandler.CHANNEL_LIFE_SPAN);		
	}
}
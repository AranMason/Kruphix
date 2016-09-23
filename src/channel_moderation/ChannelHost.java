package channel_moderation;

import java.util.Timer;

import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.ChannelManager;

public class ChannelHost {

	private final static String CHANNEL_CREATE_COMMAND = "!server";
	private final static String WIPE_POPUP_CHANNELS = "!wipe";
		
	public ChannelHost(){
		
	}

	public String getChannelCreateCommand() {
		return CHANNEL_CREATE_COMMAND;
	}
	
	public String getWipeChannelsCommand(){
		return WIPE_POPUP_CHANNELS;
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
		
		ChannelManager channel = event.getGuild().createVoiceChannel(args[1]);
		System.out.println(channel);
		
		//Default set expiry time to be in 12 hours.
		Timer t = new Timer();
		
		t.schedule(new ChannelHandler(channel), ChannelHandler.CHANNEL_LIFE_SPAN);		
	}
}

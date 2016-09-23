package channel_moderation;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.managers.ChannelManager;

public class ChannelHandler extends TimerTask{
	
	private ChannelManager v;
	//Initial Life Span is 2 hours
	public static final long CHANNEL_LIFE_SPAN = 7200000;
	//Will check for extensions every 30 minutes.
	public static final long CHANNEL_EXTENTION_TIME = 1800000;
	
	public ChannelHandler(ChannelManager v) {
		this.v = v;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if(v.getChannel().getUsers().isEmpty())
			v.delete();	
		else{
			Timer t = new Timer();
			t.schedule(new ChannelHandler(v), CHANNEL_EXTENTION_TIME);
		}
	}

}

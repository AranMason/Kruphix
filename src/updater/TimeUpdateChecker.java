package updater;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import channel_moderation.ChannelHandler;

public class TimeUpdateChecker extends TimerTask {
	
	private static final long CHECK_FREQUENCY = 0;
	private Updater ud;
	
	public TimeUpdateChecker(Kruphix host, Updater ud) {
		// TODO Auto-generated constructor stub
		
		
	}

	@Override
	public void run() {
		
		if(!ud.checkForUpdates()){
			ud.update();
		}
		Timer t = new Timer();
		t.schedule(new TimeUpdateChecker(host), CHECK_FREQUENCY);
	}

}

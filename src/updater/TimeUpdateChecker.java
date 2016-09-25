package updater;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import channel_moderation.ChannelHandler;
import search.Searcher;

public class TimeUpdateChecker extends TimerTask {
	
	private static final long CHECK_FREQUENCY = 0;
	private Updater ud;
	private Searcher host;
	
	public TimeUpdateChecker(Searcher host, Updater ud) {
		// TODO Auto-generated constructor stub
		this.ud = ud;
		this.host = host;
		
	}

	@Override
	public void run() {
		
		if(!ud.checkForUpdates()){
			ud.update();
		}
		Timer t = new Timer();
		t.schedule(new TimeUpdateChecker(host, ud), CHECK_FREQUENCY);
	}

}

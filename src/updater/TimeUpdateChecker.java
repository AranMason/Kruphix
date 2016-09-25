package updater;

import java.util.Timer;
import java.util.TimerTask;

import search.Searcher;

public class TimeUpdateChecker extends TimerTask {
	
	private static final long CHECK_FREQUENCY = 0;
	private Updater ud;
	private Searcher host;
	
	public TimeUpdateChecker(Searcher host, Updater ud) {
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

package updater;

import java.util.Timer;
import java.util.TimerTask;

import search.Searcher;

public class TimeUpdateChecker extends TimerTask {
	
	private static final long CHECK_FREQUENCY = 0;
	private Searcher host;
	
	public TimeUpdateChecker(Searcher host) {
		this.host = host;
	}

	@Override
	public void run() {
		
		host.loadData();
		
		Timer t = new Timer();
				t.schedule(new TimeUpdateChecker(host), CHECK_FREQUENCY);;
		
		
	}

}

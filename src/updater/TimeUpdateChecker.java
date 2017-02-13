package updater;

import java.util.Timer;
import java.util.TimerTask;

import search.Searcher;

public class TimeUpdateChecker extends TimerTask {
	
	//Currently checks every day.
	private static final long CHECK_FREQUENCY = 86400000;
	private Searcher host;
	
	public TimeUpdateChecker(Searcher host) {
		this.host = host;
	}

	@Override
	public void run() {
		System.out.println("Checking for Updates for " + host.toString());
		
		host.loadData();
		
		Timer t = new Timer();
		t.schedule(new TimeUpdateChecker(host), CHECK_FREQUENCY);
		
		
	}

}

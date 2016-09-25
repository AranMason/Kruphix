package updater;

public interface Updater {

	public abstract boolean checkForUpdates();
	
	public abstract boolean GetLatestJSON();
	
	public abstract void update();
}

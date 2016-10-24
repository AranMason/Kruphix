package updater;

import java.io.IOException;
import java.net.MalformedURLException;

public interface Updater {

	public abstract boolean checkForUpdates() throws MalformedURLException, IOException;
	
	public abstract Object loadJSONFile();
	
	public abstract void update();
}

package updater;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import parsers.FileReader;

public class HSJsonUpdater implements Updater {
	
	private static final String WEB_CARDS = "https://api.hearthstonejson.com/v1/latest/enUS/cards.json";
	private static final String LOCAL_CARDS = "data/hs-cards.json";

	
	@Override
	public boolean checkForUpdates() throws MalformedURLException, IOException {
		//Currently no meaningful way to check for updates outside of just redownloading
		//the card file.
		return false;
	}

	@Override
	public Object loadJSONFile() {
		try {
			return FileReader.loadJSON(LOCAL_CARDS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update() {
		//FileReader.downloadFile(WEB_CARDS, LOCAL_CARDS);
	}

}
package updater;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;

public class MTGJsonUpdater {

	private static final String ALLCARDS_URI = "https://mtgjson.com/json/AllCards.json";
	private static final String VERSION_URI = "https://mtgjson.com/json/version.json";
	private static final String LOCAL_VERSION = "data/version.json";
	private static final String LOCAL_ALLCARDS = "data/AllCards.json";
	
	/**
	 * Return true if the current version is up to date with the current version on mtgjson.
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static boolean checkForUpdates() throws MalformedURLException, IOException{
		
		InputStream is = new URL(VERSION_URI).openStream();
	    try {
	    	
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = (rd.toString());
	      
	      JSONObject local_ver = parsers.FileReader.loadJSON(LOCAL_VERSION);
	      
	      //If they are equal then we are still up to date, otherwise there is a version difference.
	      
	      System.out.println("Local: " + local_ver.toString());
	      System.out.println("Web Ver: " + jsonText.toString());
	      return !local_ver.toString().equals(jsonText);
	    	
	    } finally {
	      is.close();
	    }
	}
	
	private static JSONObject downloadMTGJson(String uri){
		return null;
		
	}

	public static JSONObject GetLatestJSON() {
		return parsers.FileReader.loadJSON(LOCAL_ALLCARDS);
	}
}

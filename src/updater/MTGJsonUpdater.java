package updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;

import parsers.FileReader;

public class MTGJsonUpdater implements Updater{

	private static final String ALLCARDS_URI = "https://mtgjson.com/json/AllCards.json";
	private static final String VERSION_URI = "https://mtgjson.com/json/version-full.json";
	private static final String LOCAL_VERSION = "data/version.json";
	private static final String LOCAL_ALLCARDS = "data/AllCards.json";
	
	/**
	 * Return true if the current version is up to date with the current version on mtgjson.
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public boolean checkForUpdates() throws MalformedURLException, IOException{
		
		InputStream is = new URL(VERSION_URI).openStream();
	    try {
	    	System.out.println("Creating Buffer");
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      	System.out.println("Reading Buffer");
	      
		      //Reading Web File to verify version.
		      StringBuilder sb = new StringBuilder();
	
		      String line;
		      rd = new BufferedReader(new InputStreamReader(is));
		      while ((line = rd.readLine()) != null) {
		          sb.append(line);
		      }
		      //Reading Local Version
		      String webVersion = sb.toString();
		  
		      JSONObject local_ver = (JSONObject)FileReader.loadJSON(LOCAL_VERSION);
		      String local = local_ver.toJSONString();
		      
		      //If they are equal then we are still up to date, otherwise there is a version difference.
		      System.out.println("local: " + local);
		      System.out.println("Net :" + webVersion);
		      
		      //Compare the two versions.
		      return (local.equals(webVersion));
	    	
	    } catch(Exception e){
	    	  //If we can't find the file, then we must be out of date.
	    	  System.out.println(e);
	    	  return false;
	    }finally {
	    	is.close();
	    }
	}

	public JSONObject loadJSONFile() {
		try{
			return (JSONObject) parsers.FileReader.loadJSON(LOCAL_ALLCARDS);
		} catch(Exception e){
			System.err.println(e);
		}
		return null;
	}

	public void update() {
		//Updates the local file of the JSON object to the most recent one.
		
		//Check if the current version is up to date.
		try {
			if(!this.checkForUpdates()){
				System.out.println("Local Copies out of date.");
				FileReader.downloadFile(ALLCARDS_URI, LOCAL_ALLCARDS);
				FileReader.downloadFile(VERSION_URI, LOCAL_VERSION);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

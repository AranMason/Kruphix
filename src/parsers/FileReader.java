package parsers;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileReader {

	
	public static JSONObject loadJSON(String address){
		
		
		JSONParser p = new JSONParser();
		
		try {
			FileInputStream fi = new FileInputStream(address);
			InputStreamReader f = new InputStreamReader(fi, "UTF-8");
			
			
			return (JSONObject)p.parse(f);
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

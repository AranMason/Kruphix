package parsers;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileReader {

	
	public static JSONObject loadJSON(String address) throws IOException, ParseException{
		
		
		JSONParser p = new JSONParser();
		
		
			FileInputStream fi = new FileInputStream(address);
			InputStreamReader f;
			try {
				f = new InputStreamReader(fi, "UTF-8");
				return (JSONObject)p.parse(f);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			
		
		return null;
	}
}

package parsers;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileReader {

	
	public static Object loadJSON(String address) throws IOException, ParseException{
		
		
		JSONParser p = new JSONParser();
		
		
			FileInputStream fi = new FileInputStream(address);
			InputStreamReader f;
			try {
				f = new InputStreamReader(fi, "UTF-8");
				return p.parse(f);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fi.close();
			}
			return null;
	}

	public static void downloadFile(String uri, String target) {
		System.out.println("Downloading File from: " + uri);
		ReadableByteChannel rbc;
		try {
			URL website = new URL(uri);
			rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(target);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			System.out.println("Download Failed");
			e.printStackTrace();
		}
		System.out.println("Download Complete");
	}
}

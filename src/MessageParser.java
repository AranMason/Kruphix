
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {

	private static final String regex = "<<([^<>]*)>>";
	
	public static String[] getCards(String message){
		Pattern p = Pattern.compile(regex);
		
		Matcher m = p.matcher(message);

		ArrayList<String> s = new ArrayList<String>();

		while(m.find()){			
			s.add(truncateEdge(m.group()));
		}

		return s.toArray(new String[s.size()]);
				
	}
	
	public static String truncateEdge(String s){
		return (String) s.subSequence(2, s.length()-2);
		
	}

}

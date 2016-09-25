package parsers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardSearchParser {
	
	public enum SEARCH_REGEX{
		MAGIC_THE_GATHERING {
			public String toString() {
		        return "<<([^<>]*)>>";
		    }
		};
	}
	
	//TODO outsource the regex definition?
	public static String[] getMatchingCards(String message, SEARCH_REGEX regex){
		Pattern p = Pattern.compile(regex.toString());
		
		Matcher m = p.matcher(message);

		ArrayList<String> cards = new ArrayList<String>();

		while(m.find()){	
			String temp = truncateEdge(m.group());
			if(!cards.contains(temp))
				cards.add(truncateEdge(m.group()));
		}

		return cards.toArray(new String[cards.size()]);
				
	}
	
	public static boolean containsMatches(String s, SEARCH_REGEX regex){
		Pattern p = Pattern.compile(regex.toString());
		
		Matcher m = p.matcher(s);
		
		return m.find();
	}
	
	public static String truncateEdge(String s){
		return (String) s.subSequence(2, s.length()-2);
		
	}

}

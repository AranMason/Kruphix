package parsers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CardSearchParser {
	private static double EDIT_DISTANCE_THRESHOLD = 0.95;
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
		ArrayList<String> noDupCards = new ArrayList<String>();
		for(String card: cards){
			boolean noMatch = true;
			//Check if a card already in the list matches
			for(String cardND: noDupCards){
				double dist = StringUtils.getJaroWinklerDistance(card, cardND);
				if(dist > EDIT_DISTANCE_THRESHOLD){
					noMatch = false;
					break;
				}
			}
			//If not add it
			if(noMatch == true){
				noDupCards.add(card);
			}
		}
		
		return noDupCards.toArray(new String[noDupCards.size()]);
				
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


import event_handlers.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;




public class Kruphix{

	public static void main(String[] args) 
	{
		JDA jda;
		try {
			jda = new JDABuilder().setBotToken(args[0]).buildBlocking();
			jda.addEventListener(new MTGEventHandler());
			jda.addEventListener(new ChannelEventHandler());
			jda.addEventListener(new HSEventHandler());
			jda.addEventListener(new MCIEventHandler());
			jda.addEventListener(new VideoClientEventHandler());
		} catch (Exception e){
			System.err.println(e);
		}
	}	
}

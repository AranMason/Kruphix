package event_handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.*;
import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class VideoClientEventHandler extends ListenerAdapter {
	
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		System.out.println("testing");
		if(StringUtils.startsWithIgnoreCase(event.getMessage().getContent(),
				 "!")){
			//ScriptEngineManager sem = new ScriptEngineManager();
			//ScriptEngine engine = sem.getEngineByName("JavaScript");
			try {
				
				ScriptEngineManager manager = new ScriptEngineManager();
		        ScriptEngine engine = manager.getEngineByName("JavaScript");
		        
		        

		        // JavaScript code in a String. This code defines a script object 'obj'
		        // with one method called 'hello'.        
		        Reader r = null;
				try {
					r = new FileReader("javascript/test.js");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        // evaluate script
		        engine.eval("function run() { println('run called'); }");

		        // javax.script.Invocable is an optional interface.
		        // Check whether your script engine implements or not!
		        // Note that the JavaScript engine implements Invocable interface.
		        Invocable inv = (Invocable) engine;

		        // get script object on which we want to call the method
		        Object obj = engine.get("obj");

		        // invoke the method named "hello" on the script object "obj"
		        inv.invokeMethod(obj, "hello", "Script Method !!" );
				
				
				
				
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

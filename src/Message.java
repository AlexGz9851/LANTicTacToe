import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Message{
	private JsonObject message, data;
	private Socket conection;
	
	public Message(JsonPrimitive from, JsonPrimitive to, JsonPrimitive action, JsonObject data, Socket conection) {
		this.message = new JsonObject();
		this.data = data;
		this.conection = conection;
		this.message.add("from", from);
		this.message.add("to", to);
		this.message.add("action", action);
		this.message.add("data", data);
	}
	
	public Message(String from, String to, Action action, JsonObject data, Socket conection) {
		this(new JsonPrimitive(from), to!=null ? new JsonPrimitive(to):null, new JsonPrimitive(action.toString()), data!=null?data:null, conection);
	}
	
	public boolean sendMessage() {
		try {
			Writer out;
			out = new BufferedWriter(new OutputStreamWriter(
			       conection.getOutputStream(),"UTF8"));
			out.append(this.message.toString());
		 	out.append((char)0);
		 	out.flush();
		 	return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static JsonObject readMessage(Socket conection) {
		try {
			Reader in = new BufferedReader(new InputStreamReader(
	        		conection.getInputStream(), "UTF8"
	        ));
	
	        int c;
	        StringBuilder response = new StringBuilder();
	        while ((c = in.read()) != 0) {
	    		response.append( (char)c ) ;
	        }
	        
	        String responseString = response.toString();
	        
	                        
	        System.out.println("The start message is: " + responseString);
	
	        JsonObject json = new JsonObject();
        	JsonParser parser = new JsonParser();
            json = (JsonObject) parser.parse(responseString);
            return json;
        
		}
        catch (JsonParseException ex) {
            System.out.println(ex);
        } catch (UnsupportedEncodingException ex) {
        	System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return new JsonObject();
	}
	
	@Override
	public String toString() {
		return this.message.toString();
	}
	
}

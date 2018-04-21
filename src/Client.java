import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Client {
	String user;
    String server;
    JsonObject logIn;
    JsonObject messages;
    Socket conection;
    
    public Client() {
    	try {
    		this.user = "Aletzin";
        	this.server = "192.168.43.185";
			this.conection = new Socket(server, 8081);
			this.logIn = new JsonObject();
	    	
			
	    	JsonObject data = new JsonObject();
	    	JsonPrimitive user = new JsonPrimitive(this.user);
	    	data.add("userName", user);
	    	
	    	Message msg = new Message(this.user, null, "newUser", data, this.conection);
	    	msg.sendMessage();
    		Thread.sleep(5000);
    		
    		JsonObject data2 = new JsonObject();
	    	JsonPrimitive spam = new JsonPrimitive("trash");
	    	data2.add("spam", spam);
    		Message something = new Message(this.user, this.user, "something", data2, this.conection);
    		for(int i = 0; i < 5; i++) {
	    		something.sendMessage();
	    		Thread.sleep(5000);
	    		Reader in=new BufferedReader(new InputStreamReader(
		           conection.getInputStream(),"UTF8"
				));
	            int c;
		        StringBuilder message = new StringBuilder();
		        while ((c=in.read()) != 0){
			        message.append((char)c);
		        }
		        System.out.println(message);
    		}
    		Message logout =  new Message(this.user, null, "logOut", null, conection);
    		logout.sendMessage();
	        while(true) {
	        	Thread.sleep(100);
            }	
	    	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    }
    
    public static void main(String[] args) {
    	Client c = new Client();
    }
    
}

import java.io.IOException;
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
    		this.user = "Ivan";
        	this.server = "127.0.0.1";
			this.conection = new Socket(server, 8081);
			System.out.println(this.conection.getInetAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.logIn = new JsonObject();
    	
    	JsonPrimitive action = new JsonPrimitive("newUser");
    	this.logIn.add("action", action);
    	System.out.println(this.logIn);
    	
    }
    
    public static void main(String[] args) {
    	Client c = new Client();
    }
    
}

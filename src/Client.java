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
	private static String user;
    private static Socket connection;
    private static Client client = new Client();
    
    private Client() 
    {
		try {
			this.connection = new Socket("192.168.43.185", 8081);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static Client getClient() {
    	return Client.client;
    }
    

    
    public boolean tryLogin(String user) {
    	Client.user = user;
    	
    	JsonObject data = new JsonObject();
    	JsonPrimitive userName = new JsonPrimitive(Client.user);
    	data.add("userName", userName);
    	Message msg = new Message(Client.user, null, "newUser", data, Client.connection);
    	msg.sendMessage();
    	
    	JsonObject answer = Message.readMessage(Client.connection);
    	
    	String action = answer.get("action").getAsString();
    	if(!action.equals("error"))
    		return true;
    	else
    		return false;
    }
    
    public JsonObject read() {
    	return Message.readMessage(Client.connection);
    }
    
    public void send(Message msg) {
    	msg.sendMessage();
    }
}

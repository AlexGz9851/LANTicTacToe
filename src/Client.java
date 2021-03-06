//Luis Iv�n Morett Ar�valo		   A01634417
//Jes�s Alejandro Gonz�lez S�nchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.io.IOException;
import java.net.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Client {
	private String user, opponent;
    private Socket connection;
    private static Client client = new Client();
    
    private Client() 
    {
		this.reconnect();
    }
    
    public static Client getClient() {
    	return Client.client;
    }
    
    public void reconnect() {
    	try {
	    	if(connection != null)
	    		this.connection.close();
    		this.connection = new Socket("192.168.43.165", 8081);
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
    } 

    
    public boolean tryLogin(String user) {
    	this.user = user;
    	
    	JsonObject data = new JsonObject();
    	JsonPrimitive userName = new JsonPrimitive(this.user);
    	data.add("userName", userName);
    	this.send(null, Action.NEWUSER, data);
    	
    	JsonObject answer = Message.readMessage(this.connection);
    	
    	String action = answer.get("action").getAsString();
    	if(!action.equals("error"))
    		return true;
    	else
    		return false;
    }
    
    public JsonObject read() {
    	return Message.readMessage(this.connection);
    }
    
    public void send(Message msg) {
    	msg.sendMessage();
    }
    
    public void send(String to, Action action, JsonObject data) {
    	new Message(this.user, to, action, data, this.connection).sendMessage();
    }
    
    public String getUser() {return this.user;}
    
    public void setOpponent(String opponent) {this.opponent = opponent;}
    public String getOpponent() {return this.opponent;}
    
   
}

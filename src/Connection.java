import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;;

public class Connection extends Thread{
    
    private final JsonObject data;
    private final Socket socket;
    ConcurrentHashMap<String, User>connectionMap;
    private String user;
   
    public Connection(JsonObject data, Socket socket, ConcurrentHashMap<String, User> conectionMap)
    {
        this.data = data;
        this.socket = socket;
        this.connectionMap = conectionMap;
    }
   
    @Override
    public void run()
    {
    	try {
	    	Action action = Action.getValue(data.get("action").getAsString());
	    	if(action == Action.NEWUSER)
	        {
	            JsonObject newUser = (JsonObject) data.get("data");
	            user = newUser.get("userName").getAsString().toString();
	            if(!connectionMap.containsKey(user)) {
	            	new Message("Server", null, Action.OK, null, this.socket).sendMessage();
	            	connectionMap.put(this.user, new User(this.user, this.socket));
	            	System.out.println("Usuario "+this.user+" Ingreso correctamente al sistema");
	            	readMessages();
	            }
	            else {
	            	new Message("Server", null, Action.ERROR, null, this.socket).sendMessage();
	            	this.closeSocket();
	        	}
	            
	        }
	    	else {
	    		this.closeSocket();
	    	}
    	}
    	catch(NullPointerException ex) {
    		this.logOut();
    		this.closeSocket();	
    	}
    	
    	
    }
   
    private void readMessages() throws NullPointerException
    {
        while(socket.isConnected() && !socket.isClosed())
        {        
            JsonObject json = new JsonObject();
            try
            {
                json = Message.readMessage(socket);
                Action action = Action.getValue(json.get("action").getAsString());
                if(action == Action.LOGOUT)
                {
                    logOut();
                    break;
                }
                else if(action == Action.USERLIST)
                {
                	Enumeration<String> keys =this.connectionMap.keys();
                	String[] users = new String[this.connectionMap.size()];
                	for(int i = 0; keys.hasMoreElements(); i++)
                	{
                		String value = keys.nextElement();
                		if(!value.equals(this.user))
                		users[i] = keys.nextElement();
                	}
                	JsonObject data = new JsonObject();
                	JsonPrimitive userList = new JsonPrimitive(new Gson().toJson(users));
                	data.add("userList", userList);
                	Message msg = new Message("server", null, Action.USERLIST, data, this.socket);
                	msg.sendMessage();
                }
                else {
                    String to = json.get("to").getAsString();
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                       connectionMap.get(to).getSocket().getOutputStream(), "UTF8"));
                    out.append(json.toString());
                    out.append((char)0);
                    out.flush();
                }
            }
            catch(IOException e)
            {
                if(e.getMessage().equals("Connection reset"))
                	logOut();
                System.out.println(e.getMessage());
                
            } 
            catch (JsonParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    
    private void logOut()
    {
        this.connectionMap.remove(this.user);
        this.closeSocket();
    }
    
    private void closeSocket() {
    	try {
	    	this.socket.close();
	        this.finalize();
	        System.out.println("Socket cerrado: " + this.socket.getInetAddress());
    	}
	    catch(IOException ex)
	    {
	        System.out.println(ex.getMessage());
	    } 
	    catch (Throwable ex) {
	        System.out.println(ex.getMessage());
	    } 
    }
}


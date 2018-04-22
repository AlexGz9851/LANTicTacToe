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
    ConcurrentHashMap<String, Socket>connectionMap;
    private String user;
   
    public Connection(JsonObject data, Socket socket, ConcurrentHashMap<String, Socket>conectionMap)
    {
        this.data = data;
        this.socket = socket;
        this.connectionMap = conectionMap;
    }
   
    @Override
    public void run()
    {
    	if(data.get("action").getAsString().equals("newUser"))
        {
            JsonObject newUser = (JsonObject) data.get("data");
            user = newUser.get("userName").toString();
            if(!connectionMap.containsKey(user)) {
            	new Message("Server", null, "ok", null, this.socket).sendMessage();
            	connectionMap.put(this.user, this.socket);
            	System.out.println("Usuario "+this.user+" Ingreso correctamente al sistema");
            	readMessages();
            }
            else {
            	new Message("Server", null, "error", null, this.socket).sendMessage();
            	this.closeSocket();
        	}
            
        }
    	else {
    		this.closeSocket();
    	}
    	
    	
    }
   
    private void readMessages()
    {
        while(socket.isConnected() && !socket.isClosed())
        {        
            JsonObject json = new JsonObject();
            try
            {
                json = Message.readMessage(socket);
                String action = json.get("action").getAsString();
                if(action.equals("logOut"))
                {
                    logOut();
                    break;
                }
                else if(action.equals("userList")) // TODO change the equals to the correct action Name
                {
                	Enumeration<String> keys =this.connectionMap.keys();
                	String[] users = new String[this.connectionMap.size()];
                	for(int i = 0; keys.hasMoreElements(); i++)
                	{
                		users[i] = keys.nextElement();
                	}
                	JsonObject data = new JsonObject();
                	JsonPrimitive userList = new JsonPrimitive(new Gson().toJson(users));
                	data.add("userList", userList);
                	Message msg = new Message("server", null, "userList", data, this.socket);
                	msg.sendMessage();
                }
                else {
                    String to = json.get("to").toString();
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                       connectionMap.get(to).getOutputStream(), "UTF8"));
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


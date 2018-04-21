import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;;

public class Connection extends Thread{
    
    private final JsonObject data;
    private final Socket socket;
    ConcurrentHashMap<String, Socket>conectionMap;
    private String user;
   
    public Connection(JsonObject data, Socket socket, ConcurrentHashMap<String, Socket>conectionMap)
    {
        this.data = data;
        this.socket =socket;
        this.conectionMap =conectionMap;
    }
   
    @Override
    public void run()
    {
    	if(data.get("action").getAsString().equals("newUser"))
        {
            JsonObject newUser = (JsonObject) data.get("data");
            user = newUser.get("userName").toString();
            if(!conectionMap.containsKey(user)) {
            	conectionMap.put(this.user, this.socket);
            	System.out.println("Usuario "+this.user+" Ingreso correctamente al sistema");
            	readMessages();
            }
            else {
            	JsonObject data = new JsonObject();
            	JsonPrimitive message = new JsonPrimitive("Existing user");
            	JsonPrimitive code = new JsonPrimitive("1");
            	data.add("message", message);
            	data.add("errorCode", code);
            	Message msg = new Message("Server", null, "error", data, this.socket);
            	msg.sendMessage();
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
                if(json.get("action").getAsString().equals("logOut"))
                {
                    logOut();
                    break;
                }
                else {
                    String to = json.get("to").toString();
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                       conectionMap.get(to).getOutputStream(), "UTF8"));
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
        this.conectionMap.remove(this.user);
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


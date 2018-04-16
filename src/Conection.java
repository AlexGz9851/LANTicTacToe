import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;;

public class Conection extends Thread{
    
    private final JsonObject data;
    private final Socket socket;
    ConcurrentHashMap<String, Socket>conectionMap;
    private String user;
   
    public Conection(JsonObject data, Socket socket, ConcurrentHashMap<String, Socket>conectionMap)
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
            	readMessages();
            }
            
        }
    	logOut();
    }
   
    private void readMessages()
    {
        while(true)
        {
            String message;
            JsonObject json = new JsonObject();
            try
            {
                Reader in = new BufferedReader(new InputStreamReader(                		socket.getInputStream(), "UTF8"
                ));
                int c;
                StringBuilder response= new StringBuilder();
                while ((c = in.read()) != -1) {
                    response.append( (char)c ) ;  
                }
                message = response.toString();

                System.out.println("The input message is: " + message);
                JsonParser parser = new JsonParser();
                json = (JsonObject) parser.parse(message);
                
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
        try{
            this.conectionMap.remove(this.user);
            this.socket.close();
            System.out.println("Sesion cerrada del usuario: " + this.user);
            this.finalize();
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


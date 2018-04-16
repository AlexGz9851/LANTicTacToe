import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        ArrayList<Socket> sockets = new ArrayList<>();
        ArrayList<Conection> conectionsList = new ArrayList<Conection>();
        ConcurrentHashMap<String, Socket> socketsMap = new ConcurrentHashMap<>();
        try
        {
            serverSocket = new ServerSocket(8081);
            System.out.println("Server Started!");
            while(true)
            {
                sockets.add(serverSocket.accept());
                System.out.println("New conection accepted!");

                Socket last = sockets.get(sockets.size()-1);
                Reader in = new BufferedReader(new InputStreamReader(
                		last.getInputStream(), "UTF8"
                ));

                int c;
                StringBuilder response = new StringBuilder();
                while ((c = in.read()) != -1) {
            		response.append( (char)c ) ;
                }
                
                String responseString = response.toString();
                
                                
                System.out.println("The incoming message is: " + responseString);

                JsonObject json = new JsonObject();
                try
                {
                	JsonParser parser = new JsonParser();
                    json = (JsonObject) parser.parse(responseString);
                }
                catch (JsonParseException ex) {
                    System.out.println(ex);
                }
                Conection conection = new Conection(json, last, socketsMap);
                conectionsList.add(conection);
                conection.start();
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
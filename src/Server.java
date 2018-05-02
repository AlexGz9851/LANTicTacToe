//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.JsonObject;


public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Socket> sockets;
    private ArrayList<Connection> conectionsList ;
    private ConcurrentHashMap<String, User> socketsMap;
    
	public Server() {
        sockets = new ArrayList<>();
        conectionsList = new ArrayList<Connection>();
        socketsMap = new ConcurrentHashMap<>();
        try
        {
            serverSocket = new ServerSocket(8081);
            System.out.println("Server Started!");
            while(true)
            {
            	Socket newConnection = serverSocket.accept();
                sockets.add(newConnection);
                System.out.println("New conection accepted!");

                JsonObject json = Message.readMessage(newConnection);
                
                Connection conection = new Connection(json, newConnection, socketsMap);
                conectionsList.add(conection);
                conection.start();
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
	}

	
    public static void main(String[] args) {
    	Server s = new Server();
    }
}
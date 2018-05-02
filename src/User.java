//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.net.Socket;

public class User {
	private String name;
	private boolean status;
	private Socket socket;
	
	public User(String name, Socket socket) {
		this.name = name;
		this.status = false;
		this.socket = socket;
	}
	
	private void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setBusy() {
		this.setStatus(true);
	}
	
	public void setFree() {
		this.setStatus(false);
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public boolean isBusy() {return this.status;}
}

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


public class UserSelector extends JFrame implements ActionListener{

	private Thread request;
	private Client client;
	private JButton btAceptar;
	private JComboBox<String> cbUserList;
	private JPanel panel;
	public UserSelector() {
		super();//TODO el nombre de la ventana
		client = Client.getClient();
		client.send(null, Action.USERLIST, null);
		JsonObject response = client.read().get("data").getAsJsonObject();
		String[] userList = new Gson().fromJson(response.get("userList").getAsString(), String[].class);
		cbUserList = new JComboBox<>(userList);
		btAceptar = new JButton("Aceptar");
		btAceptar.addActionListener(this);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.add(panel);
		panel.add(cbUserList);
		panel.add(btAceptar, BorderLayout.EAST);
		UserSelector self = this;
		this.request = new Thread() {
			@Override
			public void run() {
				while(true) {
					JsonObject request = client.read();
					Action action = Action.getValue(request.get("action").getAsString());
					String from = request.get("from").getAsString();
					JsonObject data;
					try {
						data = request.get("data").getAsJsonObject();
					}
					catch(IllegalStateException ex) {
						data = null;
					}
					if(action==Action.GAMEREQUEST) {
						if(data == null) { 
							self.panel.setEnabled(false);
							int answer = JOptionPane.showConfirmDialog(self, "Solicitud recibida de \""+from+"\" deseas aceptarla?", "Nueva solicitud", JOptionPane.YES_NO_OPTION);
							if(answer == JOptionPane.YES_OPTION) {
								acceptGameRequest(true, from);
							}
							else {
								acceptGameRequest(false, from);
								self.panel.setEnabled(true);
							}
						}
						else {
							if(data.get("accepted").getAsBoolean()) {
								Random r = new Random();
								Boolean starting = r.nextInt(2) == 0;		
								self.client.setOpponent(from);
								self.gameStart(starting);
								break;
							}
							else {
								JOptionPane.showMessageDialog(self, "Solicitud denegada");
								self.panel.setEnabled(true);
							}
						}
					}
					else if(action == Action.INICIOJUEGO) {
						self.client.setOpponent(from);
						self.gameStart(!data.get("start").getAsBoolean());
						break;
					}
				}
			}
		};
		this.setVisible(true);
		this.request.run();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.panel.setEnabled(false);
		this.client.send((String)this.cbUserList.getSelectedItem(), Action.GAMEREQUEST, null);
	}
	
	private void acceptGameRequest(boolean value, String from) {
		JsonObject dataToSend = new JsonObject();
		dataToSend.add("accepted", new JsonPrimitive(value));;
		this.client.send(from, Action.GAMEREQUEST, dataToSend);
	}
	
	private void gameStart(boolean who) {
		if(who) {
			client.send(this.client.getOpponent(), Action.INICIOJUEGO, null);
			//TODO open tictactoe window and the current player starts (will be the first to send message)
		}
		else {
			// TODO open tictactoe window and the other player starts (will start with a disabled panel and waiting for message)
		}
	}
}

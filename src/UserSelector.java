import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class UserSelector extends JFrame implements ActionListener{

	private Thread request;
	
	public UserSelector() {
		super();//TODO el nombre de la ventana
		Client client = Client.getClient();
		client.send(null, Action.USERLIST, null);
		JsonObject response = client.read().get("data").getAsJsonObject();
		String[] userList = new Gson().fromJson(response, String[].class);
		JComboBox cbUserList = new JComboBox(userList);
		JButton btAceptar = new JButton("Aceptar");
		btAceptar.addActionListener(this);
		this.add(cbUserList);
		this.add(btAceptar);
		UserSelector self = this;
		this.request = new Thread() {
			@Override
			public void run() {
				JsonObject request = client.read().get("data").getAsJsonObject();
				Action action = Action.getValue(request.get("action").getAsString());
				String from = request.get("from").getAsString();
				if(action==Action.GAMEREQUEST) {
					int answer = JOptionPane.showConfirmDialog(self, "Solicitud recibida de \""+from+"\" deseas aceptarla?", "Nueva solicitud", JOptionPane.YES_NO_OPTION);
					if(answer == JOptionPane.YES_OPTION) {
						//TODO send the start game message
					}
					else {
						//TODO send the denied request
					}
					
				}
			}
		};
		this.request.run();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		this.request.stop();
		//TODO send request to the selected user
	}
	
	
}

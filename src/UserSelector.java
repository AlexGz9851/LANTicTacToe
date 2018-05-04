//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import sun.awt.image.PixelConverter.Bgrx;


public class UserSelector extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread request;
	private Client client;
	private JButton btAceptar,
					btRefresh;
	private JComboBox<String> cbUserList;
	//Colors and Fonts
	private static final Color XCOLOR= new Color(249, 38, 114);
	private static final Color OCOLOR= new Color(102, 217, 239);
	private static final Color BKG=new Color(39,40,34);
	private static final Font FONT=new Font("Arial",Font.BOLD,20);
	private static final Font FONT2=new Font("Arial",Font.BOLD,30);
	private Dimension dimen;
	private JLabel jLab, jLab2;
	private FrameUserSelector fus;
	
	private JRadioButton rbStrict, rbFree;
	
	public UserSelector(FrameUserSelector fus) {
		super();
		this.fus=fus;
		client = Client.getClient();
		client.send(null, Action.USERLIST, null);
		JsonObject response = client.read().get("data").getAsJsonObject();
		String[] userList = new Gson().fromJson(response.get("userList").getAsString(), String[].class);
		
		this.setBackground(BKG);
		this.dimen=new Dimension(435, 300);
		this.setPreferredSize(dimen);

		
		cbUserList = new JComboBox<>();
		this.refreshUserList(userList);
		cbUserList.setBounds(0, 0, 300, 200);
		
		this.rbStrict=new JRadioButton("Strict", true);
		this.rbStrict.setFont(FONT);
		this.rbStrict.setBackground(BKG);
		this.rbStrict.setForeground(Color.WHITE);
		this.rbFree=new JRadioButton("Free");
		this.rbFree.setFont(FONT);
		this.rbFree.setBackground(BKG);
		this.rbFree.setForeground(Color.WHITE);
		ButtonGroup bg= new ButtonGroup();
		bg.add(rbStrict);
		bg.add(rbFree);
		cbUserList.setFont(FONT2);
		cbUserList.setForeground(BKG);
		cbUserList.setBackground(Color.WHITE);
		btAceptar = new JButton("Aceptar");
		btRefresh =new JButton("Refresh");
		jLab= new JLabel("Choose an oponent to play!");
		jLab.setForeground(Color.WHITE);
		jLab.setFont(FONT2);
		btAceptar.setFont(FONT);
		btRefresh.setFont(FONT);
		btAceptar.setBackground(XCOLOR);
		btRefresh.setBackground(OCOLOR);
		btAceptar.setForeground(Color.WHITE);
		btAceptar.addActionListener(this);
		btRefresh.addActionListener(this);
		JLabel a=new JLabel("x                                                          ");
		a.setForeground(BKG);
		this.add(a);
		this.add(jLab);
		JLabel b=new JLabel("x                                                                                                     ");
		b.setForeground(BKG);
		this.add(b);
		this.add(new JLabel("x                                    "));
		this.add(cbUserList);
		this.add(new JLabel("x                                              "));
		this.add(new JLabel("x                                    "));		
		this.add(rbStrict);
		this.add(new JLabel("x    "));
		this.add(rbFree);
		this.add(new JLabel("x                                    "));
		this.add(new JLabel("x                                                                                                                  "));

		this.add(btAceptar);
		this.add(btRefresh);


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
							self.setEnabled(false);
							int answer = JOptionPane.showConfirmDialog(self, "Solicitud recibida de \""+from+"\" deseas aceptarla?", "Nueva solicitud", JOptionPane.YES_NO_OPTION);
							if(answer == JOptionPane.YES_OPTION) {
								acceptGameRequest(true, from);
							}
							else {
								acceptGameRequest(false, from);
								self.setEnabled(true);
							}
						}
						else {
							if(data.get("accepted").getAsBoolean()) {
								Random r = new Random();
								Boolean starting = r.nextInt(2) == 0;		
								self.client.setOpponent(from);
								JsonObject start = new JsonObject();
								start.add("start", new JsonPrimitive(starting));
								start.add("typeGame",new JsonPrimitive(self.getTypeOfGame()));
								client.send(self.client.getOpponent(), Action.INICIOJUEGO, start);
								self.gameStart(starting);
								break;
							}
							else {
								JOptionPane.showMessageDialog(self, "Solicitud denegada");
								self.setEnabled(true);
							}
						}
					}
					else if(action == Action.INICIOJUEGO) {
						self.client.setOpponent(from);
						self.gameStart(!data.get("start").getAsBoolean(), data.get("typeGame").getAsBoolean());
						break;
					}
					else if(action == Action.USERLIST) {
						self.cbUserList.removeAll();
						self.refreshUserList(new Gson().fromJson(data.get("userList").getAsString(), String[].class));
					}
					else if(action == Action.ERROR) {
						JOptionPane.showMessageDialog(null, "The selected player is currently in a game, please select another or wait and try again.");
					}
				}
			}
		};
		this.setVisible(true);
		this.request.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btAceptar) {
			this.setEnabled(false);
			this.client.send((String)this.cbUserList.getSelectedItem(), Action.GAMEREQUEST, null);
		}else {
			client.send(null, Action.USERLIST, null);
		}
	}
	
	private void acceptGameRequest(boolean value, String from) {
		JsonObject dataToSend = new JsonObject();
		dataToSend.add("accepted", new JsonPrimitive(value));
		this.client.send(from, Action.GAMEREQUEST, dataToSend);
	}
	
	private void gameStart(boolean who) {
		JuegoCont jc= new JuegoCont(who, client, rbStrict.isSelected());
		fus.dispose();
	}
	
	private void gameStart(boolean who, boolean strict) {
		JuegoCont jc= new JuegoCont(who, client, strict);
		fus.dispose();
	}
	
	private boolean getTypeOfGame() {
		return this.rbStrict.isSelected();
	}
	
	private void refreshUserList(String[] userList) {
		this.cbUserList.removeAllItems();
		for(String item:userList)
			if(item!=null)
				this.cbUserList.addItem(item);
	}
}
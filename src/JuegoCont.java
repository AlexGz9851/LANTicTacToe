import java.util.Random;

import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JuegoCont {
	
	private PanelJuego pj;//EL PANEL JUEGO QUE CONTROLARÁ
	private boolean turno;
	private  final String[][] WINCASES= {
			{"A0","A1","A2"},{"A3","A4","A5"},{"A6","A7","A8"},{"B0","B1","B2"},{"B3","B4","B5"},{"B6","B7","B8"},
			{"C0","C1","C2"},{"C3","C4","C5"},{"C6","C7","C8"},
			
			{"A0","B0","C0"},{"A1","B1","C1"},{"A2","B2","C2"},{"A3","B3","C3"},{"A4","B4","C4"},{"A5","B5","C5"},
			{"A6","B6","C6"},{"A7","B7","C7"},{"A8","B8","C8"},
			
			{"A0","A3","A6"},{"A1","A4","A7"},{"A2","A5","A8"},{"B0","B3","B6"},{"B1","B4","B7"},{"B2","B5","B8"},
			{"C0","C3","C6"},{"C1","C4","C7"},{"C2","C5","C8"},			
			
			{"A0","A4","A8"},{"A2","A4","A6"},{"B0","B4","B8"},{"B2","B4","B6"},{"C0","C4","C8"},{"C2","C4","C6"},
			
			{"A0","B1","C2"},{"A2","B1","C0"},{"A3","B4","C5"},{"A5","B4","C3"},{"A6","B7","C8"},{"A8","B7","C6"},

			{"A0","B3","C6"},{"A6","B3","C0"},{"A1","B4","C7"},{"A7","B4","C1"},{"A2","B5","C8"},{"A8","B5","C2"},
			
			{"A0","B4","C8"},{"A2","B4","C6"},{"A6","B4","C2"},{"A8","B4","C0"}
			};
	VentanaJuego vj;
	Client client;
	String j1,j2;
	
	public JuegoCont(boolean whoStart,Client client) {
		this.setJC( whoStart, client);
	}
	
	public void move(int numeroSend, char letraBoardSend) {
		
		
		String celda=letraBoardSend+""+numeroSend;
		boolean end,end2;
		end=end2=false;
		boolean valido;
		
		valido=this.validateMove(numeroSend, letraBoardSend);
		
		if(valido) {
			//send the cell as data.
			end=this.calculate( numeroSend,letraBoardSend, turno);//set new state in current cell, calculates if game ends.
			this.pj.repaint();
			this.turno=false;
			this.pj.setBoardEnable(turno); //disable the board.		
			this.pj.setTurno((turno)?j1:j2);//changes turn name.
			
			this.pj.repaint();
			
			//sending data.
			JsonObject dataToSend = new JsonObject();
			dataToSend.add("celda", new JsonPrimitive(celda));
			this.client.send(client.getOpponent(), Action.TURNO, dataToSend);

			
			if(!end) {
				JsonObject moveBack =this.client.read();//waiting data
				Action action = Action.getValue(moveBack.get("action").getAsString());
				String from = moveBack.get("from").getAsString();
				String data;
				try {
					data = moveBack.get("data").getAsJsonObject().get("celda").getAsString();
				}
				catch(IllegalStateException ex) {
					data = null;
				}
				
				
				if(action == Action.TURNO) {

					char letraReaded =data.charAt(0);
					int numeroReaded= data.charAt(1)-(int)('0');
					end2=this.calculate(numeroReaded, letraReaded, turno);//calculates the move back.
					this.pj.repaint();
					if(!end2) {
						this.turno=true;
						this.pj.setBoardEnable(turno);
						this.pj.setTurno((turno)?j1:j2);
						this.pj.repaint();
					}else {
						//oponent wins
						this.opponentWins(from);
					}
				}else if(action == Action.SURRENDER) {
					//TODO recieving a surrender.
					this.respondASurrender(from);
				}
			}else {
				this.clientWins();
			}
		}
	}
	
	private boolean calculate(int numeroCelda, char letraBoard, boolean turno) {
		char setCheckStatus;
		//Host siempre será O y opponent será X.
		if(turno) {
			setCheckStatus='O';
		}else {
			setCheckStatus='X';
		}

		this.pj.getGato(letraBoard).getCelda(numeroCelda).setEstado(setCheckStatus);

		//calcular si se ganó.
		boolean someoneWin=true;
		for(int i=0;i<WINCASES.length;i++) {
			for(int j=0;j<WINCASES[i].length;j++ ) {
				someoneWin=true;
				
				char boardW=WINCASES[i][j].charAt(0);
				int nCellW= 	(int)(WINCASES[i][j].charAt(1))-(int)('0');
				//GELPPPPP
				//statCe
				char checkCell=this.pj.getGato(boardW).getCelda(nCellW).getEstado();
				if(!(checkCell==setCheckStatus)) {
					someoneWin=false;
					break;
				}
			}
			if(someoneWin) {
				break;
			}
		}
		return someoneWin;
	}

	private boolean validateMove(int numeroCelda, char letraBoard) {
		//validates if user clicked in a empty cell. 
		char status;
		if(letraBoard=='A'||letraBoard=='B'||letraBoard=='C') {
			status=this.pj.getGato(letraBoard).getCelda(numeroCelda).getEstado();

		}else {
			//win-lose.
			status='N';//cell default state.
		}
		if(status=='N')
			return true;
		else
			return false;
	}
	
	private void cleanCells() {
		for(char b : new char[] {'A', 'B', 'C'}) {
			for(int i = 0; i < 9; i++)
				this.pj.getGato(b).getCelda(i).setEstado('N');
		}
		
	}
	
	public void setJC(boolean whoStart,Client client) {

		this.client=client;
		j1=this.client.getUser();
		j2=this.client.getOpponent();
		this.turno=whoStart;
		
		vj= new VentanaJuego();
		this.pj.setJc(this);
		this.pj=vj.getPj();
		this.pj.setControllerOnBoards();

		this.cleanCells();
		
		this.pj.setUsuario((whoStart)?j1:j2);
		this.pj.setOponente((whoStart)?j2:j1);
		this.pj.setTurno((whoStart)?j1:j2);
		this.pj.setBoardEnable(whoStart);
		

		this.cleanCells();//METODO NO TERMINADO, SET CELLS STATUS IN 'N'
		this.pj.repaint();
		if(!whoStart) {
			boolean end2;
			JsonObject moveBack =this.client.read();//waiting data
			Action action = Action.getValue(moveBack.get("action").getAsString());
			String data;
			try {
				data = moveBack.get("data").getAsJsonObject().get("celda").getAsString();
			}
			catch(IllegalStateException ex) {
				data = null;
			}
			
			
			if(action == Action.TURNO) {

				char letraReaded =data.charAt(0);
				int numeroReaded= data.charAt(1)-(int)('0');
				end2=this.calculate(numeroReaded, letraReaded, turno);//calculates the move back.
				this.pj.repaint();
				if(!end2) {
					this.turno=true;
					this.pj.setBoardEnable(turno);
					this.pj.setTurno((turno)?j1:j2);
					this.pj.repaint();
				}
			}
		}
	}
	
	public void respondASurrender(String fromx) {
		this.playAgain( fromx,"win");
	}
	public void opponentWins(String fromx) {
		this.playAgain( fromx,"lose");
	}
	public void playAgain(String fromx, String loseOrWin) {

		this.turno=false;
		int answer = JOptionPane.showConfirmDialog(null, "You "+loseOrWin+"! Do you wanna play again?", "Play again?", JOptionPane.YES_NO_OPTION);
		boolean playAgain = answer==JOptionPane.YES_OPTION;
		JsonObject valueToSend = new JsonObject();
		valueToSend.add("playAgain", new JsonPrimitive(playAgain));
		this.client.send(client.getOpponent(), Action.FINJUEGO, valueToSend);
		if(playAgain) {
			JsonObject newGameRequest = this.client.read();
			if(newGameRequest.get("data").getAsJsonObject().get("playAgain").getAsBoolean()) {
				Boolean starting = new Random().nextInt(2) == 0;		
				this.client.setOpponent(fromx);
				JsonObject start = new JsonObject();
				start.add("start", new JsonPrimitive(starting));
				client.send(this.client.getOpponent(), Action.INICIOJUEGO, start);
				// TODO restart this class, dispose currently active windows
				this.setJC(starting, client);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "The other player doesn't want to play again, sorry :c.");
				//TODO dispose windows and open user selector
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
			}
		}else {
			JOptionPane.showMessageDialog(null, "You "+loseOrWin+"! Thanks for playing!");
			//TODO dispose windows and open user selector
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
		}
	}
	
	public void surrender() {
		this.pj.setBoardEnable(false);
		this.pj.getNewGame().setEnabled(false);
		this.client.send(client.getOpponent(), Action.SURRENDER, new JsonObject());
		//no se si enviar un surrender o un new game.
	}
	
	public void clientWins() {
		this.turno=false;
		JsonObject playAgain = this.client.read();
		if(playAgain.get("data").getAsJsonObject().get("playAgain").getAsBoolean()) {
			int answer = JOptionPane.showConfirmDialog(null, "You win! Do you wanna play again?", "Play again?", JOptionPane.YES_NO_OPTION);
			boolean playAgain2 = answer==JOptionPane.YES_OPTION;
			JsonObject valueToSend = new JsonObject();
			valueToSend.add("playAgain", new JsonPrimitive(playAgain2));
			this.client.send(client.getOpponent(), Action.GAMEREQUEST, valueToSend);
			if(playAgain2) {
				JsonObject newGameRequest = this.client.read();
				boolean starting = !newGameRequest.get("data").getAsJsonObject().get("start").getAsBoolean();
				// TODO create here the constructor
				// Dispose windows... doesn´t neeeded dispose anything
				this.setJC(starting, client);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "The other player does't want to play again, Thanks for playing.");	
				//TODO dispose windows and open user selector
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "You Win! The other player does't want to play again, sorry :c.");
			//TODO dispose windows and open user selector
			FrameUserSelector fus= new FrameUserSelector();
			this.vj.dispose();
		}
	}
}

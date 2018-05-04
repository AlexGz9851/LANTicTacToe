//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.util.Random;

import javax.swing.JOptionPane;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JuegoCont {
	
	private PanelJuego pj;//EL PANEL JUEGO QUE CONTROLARÁ
	private boolean turno, strict;
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
	private VentanaJuego vj;
	private Client client;
	private String j1,j2;
	private int caseW;
	private char strictBoardEnable;
	
	public JuegoCont(boolean whoStart,Client client, boolean typeOfGame) {
		this.setJC( whoStart, client, typeOfGame);
	}
	public void move(int numeroSend, char letraBoardSend) {
		Thread controller = new Thread() {
			public void run() {
				
				boolean end;
				boolean valido;
				
				valido=validateMove(numeroSend, letraBoardSend);
				
				if(valido) {
					end=sendData(numeroSend,letraBoardSend);
					
					if(!end) {
						moveBack();
					}else {
						clientWins();
					}
				}
			}
		};
		controller.start();
	}
	
	private boolean sendData(int numeroSend,char letraBoardSend) {
		boolean end;
		//send the cell as data.
		end=this.calculate( numeroSend,letraBoardSend, turno);//set new state in current cell, calculates if game ends.
		this.pj.repaint();
		this.turno=false;
		this.pj.setBoardEnable(turno); //disable the board.		
		this.pj.setTurno((turno)?j1:j2);//changes turn name.
		
		this.pj.repaint();
		String celda=letraBoardSend+""+numeroSend;
		//sending data.
		JsonObject dataToSend = new JsonObject();
		dataToSend.add("celda", new JsonPrimitive(celda));
		this.client.send(client.getOpponent(), Action.TURNO, dataToSend);
		return end;
	}
	
	private void moveBack() {
		boolean end2;
		JsonObject moveBack =this.client.read();//waiting data
		Action action = Action.getValue(moveBack.get("action").getAsString());
		String from = moveBack.get("from").getAsString();
		String data;
		
		if(action == Action.TURNO) {
			try {
				data = moveBack.get("data").getAsJsonObject().get("celda").getAsString();
			}
			catch(IllegalStateException ex) {
				data = null;
			}
			char letraReaded =data.charAt(0);
			int numeroReaded= data.charAt(1)-(int)('0');
			end2=this.calculate(numeroReaded, letraReaded, turno);//calculates the move back.
			if(strict) 
				calculatePermitedBoard(numeroReaded);
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
			this.respondASurrender(from);
		} else if(action == Action.FINJUEGO) {
			if(moveBack.get("data").getAsJsonObject().get("playAgain").getAsBoolean()) {
				int answer = JOptionPane.showConfirmDialog(null, "Do you wanna play again?", "Play again?", JOptionPane.YES_NO_OPTION);
				boolean playAgain2 = answer==JOptionPane.YES_OPTION;
				JsonObject valueToSend = new JsonObject();
				valueToSend.add("playAgain", new JsonPrimitive(playAgain2));
				this.client.send(client.getOpponent(), Action.GAMEREQUEST, valueToSend);
				if(playAgain2) {
					JsonObject newGameRequest = this.client.read();
					boolean starting = !newGameRequest.get("data").getAsJsonObject().get("start").getAsBoolean();
					// creates a new game.
					
					this.vj.dispose();
					this.setJC(starting, client,this.strict);
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Thanks for playing.");	
					FrameUserSelector fus= new FrameUserSelector();
					this.vj.dispose();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Thanks for playing.");
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
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
		char boardW,checkCell;
		int nCellW;
		boolean someoneWin=true;
		for(int i=0;i<WINCASES.length;i++) {
			for(int j=0;j<WINCASES[i].length;j++ ) {
				someoneWin=true;
				
				boardW=WINCASES[i][j].charAt(0);
				nCellW= 	(int)(WINCASES[i][j].charAt(1))-(int)('0');
				checkCell=this.pj.getGato(boardW).getCelda(nCellW).getEstado();
				if(!(checkCell==setCheckStatus)) {
					someoneWin=false;
					break;
				}
			}
			if(someoneWin) {
				caseW=i;
				this.pj.setLine(this.pj.getGato(WINCASES[caseW][0].charAt(0)).getCelda((int)(WINCASES[caseW][0].charAt(1))-(int)('0')).getX0(),
						this.pj.getGato(WINCASES[caseW][0].charAt(0)).getCelda((int)(WINCASES[caseW][0].charAt(1))-(int)('0')).getY0(),
						this.pj.getGato(WINCASES[caseW][2].charAt(0)).getCelda((int)(WINCASES[caseW][2].charAt(1))-(int)('0')).getX0(),
						this.pj.getGato(WINCASES[caseW][2].charAt(0)).getCelda((int)(WINCASES[caseW][2].charAt(1))-(int)('0')).getY0());
				break;
			}
		}
		
		this.pj.setSomeOneWin(someoneWin);

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
	
	public void setJC(boolean whoStart,Client client, boolean strict) {
		this.strict=strict;
		this.client=client;
		this.strictBoardEnable='N';
		j1=this.client.getUser();
		j2=this.client.getOpponent();
		this.turno=whoStart;
		this.caseW=-1;//out of range.
		vj= new VentanaJuego(this);
		this.pj=vj.getPj();
		this.pj.setJc(this);
		
		this.pj.setControllerOnBoards();

		this.cleanCells();
		
		this.pj.setStrict(strict);
		this.pj.setUsuario((whoStart)?j1:j2);
		this.pj.setOponente((whoStart)?j2:j1);
		this.pj.setTurno((whoStart)?j1:j2);
		this.pj.setBoardEnable(whoStart);
		

		this.cleanCells();//METODO NO TERMINADO, SET CELLS STATUS IN 'N'
		this.pj.repaint();
		if(!whoStart) {
			moveBack();
		}
	}
	
	public char getStrictBoardEnable() {
		return strictBoardEnable;
	}

	public void setStrictBoardEnable(char strictBoardEnable) {
		this.strictBoardEnable = strictBoardEnable;
	}

	public void respondASurrender(String fromx) {
		this.turno=false;
		int answer = JOptionPane.showConfirmDialog(null, "The other player surrender! Do you wanna play again?", "Play again?", JOptionPane.YES_NO_OPTION);
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
				this.vj.dispose();
				this.setJC(starting, client,this.strict);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "The other player doesn't want to play again, sorry :c.");
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
			}
		}else {
			JOptionPane.showMessageDialog(null, "Thanks for playing!");
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
		}
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
				
				this.vj.dispose();
				this.setJC(starting, client,this.strict);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "The other player doesn't want to play again, sorry :c.");
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
			}
		}else {
			JOptionPane.showMessageDialog(null, "You "+loseOrWin+"! Thanks for playing!");
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
		}
	}
	
	public void surrender() {
		this.pj.setBoardEnable(false);
		this.pj.getNewGame().setEnabled(false);
		this.client.send(client.getOpponent(), Action.SURRENDER, new JsonObject());
		moveBack();
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
				// creates a new game.
				
				this.vj.dispose();
				this.setJC(starting, client,this.strict);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "The other player does't want to play again, Thanks for playing.");	
				FrameUserSelector fus= new FrameUserSelector();
				this.vj.dispose();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "You Win! The other player does't want to play again, sorry :c.");
			FrameUserSelector fus= new FrameUserSelector();
			this.vj.dispose();
		}
	}
	private void calculatePermitedBoard(int numeroReaded) {
		this.strictBoardEnable=(char)((numeroReaded%3)+(int)('A'));
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	public String getJ1() {
		return j1;
	}
	public void setJ1(String j1) {
		this.j1 = j1;
	}
}

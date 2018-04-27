import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelJuego extends JPanel implements MouseListener{
	private int margen; //ancho margen entre tableros
	private static final Color BKG=new Color(39,40,34);
	private GatoBoard gatoA,
					 gatoB,
					 gatoC;
	private String usuario,
				   oponente,
				   turno;
	private boolean boardEnable;
	private int xMo, yMo,	//coordenada de click
				xA, yTodos,//coordenadas de los boards en panel.
				xB,
				xC,
				lenghtG;
	private Font font,fontB;
	private Dimension dimen;
	private Button newGame;
	private static final Color OCOLOR= new Color(102, 217, 239);
	private JuegoCont jc;
	public PanelJuego() {
		super();
		

		boardEnable=true;
		this.setLayout(null);
		this.newGame= new Button("New game");
		this.dimen=new Dimension(870, 600);
		this.margen=(int)(dimen.getWidth()*3/87.0);
		this.lenghtG=(int)(dimen.getWidth()*25/87.0);
		this.xA=this.margen;
		this.xB=lenghtG+2*margen;
		this.xC=2*lenghtG+3*margen;
		yTodos=200;
		this.gatoA= new GatoBoard('A',xA, yTodos,lenghtG);
		this.gatoB= new GatoBoard('B',xB, yTodos,lenghtG);
		this.gatoC= new GatoBoard('C',xC, yTodos,lenghtG);
		this.addMouseListener(this);
		this.setPreferredSize(dimen);
		this.setBackground(BKG);
		
		fontB=new Font("Arial",Font.BOLD,24);
		this.newGame.setFont(fontB);
		this.newGame.setBounds(500, 100, 140, 40);
		this.newGame.setBackground(OCOLOR);
		font=new Font("Arial",Font.BOLD,40);
		this.newGame.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int surrender;
				if (boardEnable) {
					surrender=JOptionPane.showConfirmDialog(null, "Are you sure? This game gonna be considered as lost.", "", JOptionPane.YES_NO_OPTION);
					if(surrender==JOptionPane.YES_OPTION) {
						jc.surrender();
					}
				}

			}
		});

		this.add(newGame);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("3D Tic Tac Toe!", 50,50);
		g.drawString(this.usuario+" vs "+this.oponente, 80,100);
		g.drawString(this.turno+"'s turn", 500,50);		
		gatoA.paintComponet(g);
		gatoB.paintComponet(g);
		gatoC.paintComponet(g);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.xMo=e.getX();
		this.yMo=e.getY();
		enviarCordAGato(xMo,yMo);
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void enviarCordAGato(int x,int y) {
		int tablero, xG, yG;
		if(boardEnable) {
			tablero=x/(this.lenghtG+this.margen);
			xG=x%(this.lenghtG+this.margen);
			if((xG>this.margen && y>=this.yTodos) && (y<(this.yTodos+this.lenghtG))) {
				yG=y-200;
				xG-=this.margen;
				if(tablero==0) {
					this.gatoA.setClick(xG, yG);//xG y yG manda las coordenadas relativas del click al gato.
				}else if(tablero==1) {
					this.gatoB.setClick(xG, yG);				
				}else if(tablero==2) {
					this.gatoC.setClick(xG, yG);
				}else{
					
				}
			}
		}
	}
	public Dimension getDimen() {
		return dimen;
	}
	public void setDimen(Dimension dimen) {
		this.dimen = dimen;
	}	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public void setOponente(String oponente) {
		this.oponente = oponente;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}

	public boolean isBoardEnable() {
		return boardEnable;
	}
	public void setBoardEnable(boolean boardEnable) {
		this.boardEnable = boardEnable;
	}

	public GatoBoard getGato(char board) {
		if(board=='A') {
			return gatoA;
		}else if(board=='B') {
			return gatoB;
		}else if(board=='C') {
			return gatoC;
		}else {
			return null;
		}
	}

	
	public JuegoCont getJc() {
		return jc;
	}

	public void setJc(JuegoCont jc) {
		this.jc = jc;
	}
	public void setControllerOnBoards() {
		this.getGato('A').setJc(jc);
		this.getGato('B').setJc(jc);
		this.getGato('C').setJc(jc);
	}

	public Button getNewGame() {
		return newGame;
	}

	public void setNewGame(Button newGame) {
		this.newGame = newGame;
	}
}

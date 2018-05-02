//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelJuego extends JPanel implements MouseListener{
	private int margen; //ancho margen entre tableros
	private static final Color BKG=new Color(39,40,34);
	private static final Color XCOLORT= new Color(249, 38, 114,100);
	private static final Color OCOLORT= new Color(102, 217, 239,100);
	private static final Color ORCOLT= new Color(255,140,0,100);
	private GatoBoard gatoA,
					 gatoB,
					 gatoC;
	private String usuario,
				   oponente,
				   turno;
	private boolean boardEnable,
					someOneWin,
					strict;
	private int xMo, yMo,	//coordenada de click
				xA, yTodos,//coordenadas de los boards en panel.
				xB,
				xC,
				lenghtG,
				x1,x2,y1,y2;
	private Font font,fontB;
	private Dimension dimen;
	private Button newGame;
	private static final Color OCOLOR= new Color(102, 217, 239);
	private static final Color ORCOL= new Color(255,140,0);
	private JuegoCont jc;
	
	
	public PanelJuego(JuegoCont jc) {
		super();
		this.jc=jc;
		x1=x2=y1=y2=-1;//0ut of range
		strict=jc.isStrict();
		someOneWin=false;
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
		g.setColor(ORCOLT);
		g.fillRect(55, 465, 60, 60);
		g.setColor(XCOLORT);
		g.fillRect(40, 480, 60, 60);
		g.setColor(OCOLORT);
		g.fillRect(25, 495, 60, 60);
		g.setColor(Color.WHITE);
		g.drawLine(25, 495, 85, 495);
		g.drawLine(25, 515, 85, 515);
		g.drawLine(25, 535, 85, 535);
		g.drawLine(25, 555, 85, 555);
		
		g.drawLine(25, 495, 25, 555);
		g.drawLine(45, 495, 45, 555);
		g.drawLine(65, 495, 65, 555);
		g.drawLine(85, 495, 85, 555);
		
		g.drawLine(25, 495, 55, 465);
		g.drawLine(85, 495, 115, 465);
		g.drawLine(85, 555, 115, 525);
		gatoA.paintComponet(g);
		gatoB.paintComponet(g);
		gatoC.paintComponet(g);
		
		if(strict) {
			this.drawBoardEnable(g);
		}

		if(someOneWin) {
			drawingWinningLine(g);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		this.xMo=e.getX();
		this.yMo=e.getY();
		enviarCordAValidar(xMo,yMo);
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {	
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {		
	}
	private void enviarCordAValidar(int x,int y) {
		int tablero=x/(this.lenghtG+this.margen);
		char gatoClick,
			 gatoValido;
		gatoValido=jc.getStrictBoardEnable();
		gatoClick=(char)(tablero+(int)('A'));
		
		if(boardEnable) {
			if(strict) {
				if(gatoClick==gatoValido ||gatoValido=='N' )
					this.enviarCordAGato(tablero, x, y);	
			}else {
				this.enviarCordAGato(tablero, x, y);
			}
		}
	}
	public void enviarCordAGato(int tablero, int x, int y) {
		int xG, yG;
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
	private void drawingWinningLine(Graphics g) {
		Graphics2D g2= (Graphics2D)g;
		// draw line.
		g2.setColor(ORCOL);
		g2.setStroke(new BasicStroke(15));
		g2.drawLine(x1, y1, x2, y2);
	}
	private void drawBoardEnable(Graphics g) {
		char board;
		board=jc.getStrictBoardEnable();
		Graphics2D g3= (Graphics2D)g;
		g3.setColor(ORCOL);
		g3.setStroke(new BasicStroke(15));
		if(board=='A') {
			g3.drawRect(xA-18, yTodos-18, lenghtG+36, lenghtG+36);
		}else if(board=='B') {
			g3.drawRect(xB-18, yTodos-18, lenghtG+36, lenghtG+36);
		}else if(board=='C') {
			g3.drawRect(xC-18, yTodos-18, lenghtG+36, lenghtG+36);
		}
		g=(Graphics)g;
		
	}
	public void setSomeOneWin(boolean someOneWin) {
		this.someOneWin = someOneWin;
	}
	public void setLine(int x1,int y1 ,int x2,int y2) {
		//recibe coord. iniciales celda, se le agrega medio largo de celda pra centrar.
		this.x1=x1+(int)(gatoA.getLargoCell()*.5);
		this.x2=x2+(int)(gatoA.getLargoCell()*.5);
		this.y1=y1+(int)(gatoA.getLargoCell()*.5);
		this.y2=y2+(int)(gatoA.getLargoCell()*.5);
	}



	public boolean isStrict() {
		return strict;
	}



	public void setStrict(boolean strict) {
		this.strict = strict;
	}
}

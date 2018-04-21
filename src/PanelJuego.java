import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private int xMo, yMo,	//coordenada de click
				xA, yTodos,//coordenadas de los boards en panel.
				xB,
				xC,
				lenghtG;
	private Font font;
	private PanelJugadores pjs;
	private Dimension dimen;
	
	public PanelJuego() {
		super();
		this.dimen=new Dimension(870, 600);
		this.margen=(int)(dimen.getWidth()*3/87.0);
		this.lenghtG=(int)(dimen.getWidth()*25/87.0);
		this.xA=this.margen;
		this.xB=lenghtG+2*margen;
		this.xC=2*lenghtG+3*margen;
		yTodos=200;
		this.gatoA= new GatoBoard("A",xA, yTodos,lenghtG);
		this.gatoB= new GatoBoard("B",xB, yTodos,lenghtG);
		this.gatoC= new GatoBoard("C",xC, yTodos,lenghtG);
		this.addMouseListener(this);
		this.setPreferredSize(dimen);
		this.setBackground(BKG);
		font=new Font("Arial",Font.BOLD,40);
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
	public Dimension getDimen() {
		return dimen;
	}
	public void setDimen(Dimension dimen) {
		this.dimen = dimen;
	}	
}

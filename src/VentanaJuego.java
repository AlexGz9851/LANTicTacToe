//Luis Iván Morett Arévalo		   A01634417
//Jesús Alejandro González Sánchez A00820225 
//POO Gp2
//Profesor: Gerardo Salinas.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class VentanaJuego extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color BKG=new Color(39,40,34);
	private Dimension dim;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private PanelJuego pj;

	public VentanaJuego(JuegoCont jc) {
		super("3D Tic Tac Toe");
		this.setBackground(BKG);
		pj=new PanelJuego(jc);
		dim= new Dimension(870, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(this.dim);
		this.setResizable(false);
		//Colocar por default la ventana al centro.
	    this.setLocation(
	            (this.pantalla.width - this.dim.width) / 2,
	            (this.pantalla.height - this.dim.height) / 2);
	    
		//this.add(); add game and historial?? GESTOR DE PANELES.
	    this.add(pj);
	    
		this.pack();
		this.setVisible(true);
		
	}
	public PanelJuego getPj() {
		return pj;
	}
	public void setPj(PanelJuego pj) {
		this.pj = pj;
	}
	
	public static void main(String[] args) {
		//VentanaJuego vj= new VentanaJuego(jc);
	}

}

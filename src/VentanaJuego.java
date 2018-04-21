import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class VentanaJuego extends JFrame{
	private Dimension dim;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private PanelJuego pj;
	private PanelInicio pi;
	public VentanaJuego() {
		super("3D Tic Tac Toe");
		pj=new PanelJuego();
		pi=new PanelInicio(pj);
		dim= new Dimension(870, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(this.dim);
		//Colocar por default la ventana al centro.
	    this.setLocation(
	            (this.pantalla.width - this.dim.width) / 2,
	            (this.pantalla.height - this.dim.height) / 2);
	    
		//this.add(); add game and historial?? GESTOR DE PANELES.
	    this.add(pj);
	    this.add(pi);
	    pj.setVisible(false);
	    pi.setVisible(true);
	    
		this.pack();
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		VentanaJuego vj= new VentanaJuego();
	}

}
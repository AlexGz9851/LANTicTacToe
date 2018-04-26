import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
public class VentanaInicio  extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color BKG=new Color(39,40,34);
	private Dimension dim;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private PanelInicio pi;
	public VentanaInicio() {
		super("3D Tic Tac Toe");
		this.setBackground(BKG);
		pi=new PanelInicio();
		dim= new Dimension(870, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(this.dim);
		this.setResizable(false);
		//Colocar por default la ventana al centro.
	    this.setLocation(
	            (this.pantalla.width - this.dim.width) / 2,
	            (this.pantalla.height - this.dim.height) / 2);
	    
	    this.add(pi);
	    
		this.pack();
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		VentanaInicio vi= new VentanaInicio();
	}

}


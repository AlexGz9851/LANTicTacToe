import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FrameUserSelector  extends JFrame{
	private static final Color BKG=new Color(39,40,34);
	private Dimension dim;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private UserSelector us;
	public FrameUserSelector() {
		super("Choose an opponent:");
		this.setBackground(BKG);
		us=new UserSelector();
		dim= new Dimension(435, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(this.dim);
		this.setResizable(false);
		//Colocar por default la ventana al centro.
	    this.setLocation(
	            (this.pantalla.width - this.dim.width) / 2,
	            (this.pantalla.height - this.dim.height) / 2);
	    

	    this.add(us);
	    us.setVisible(true);
	    
		this.pack();
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		Client.getClient().tryLogin("test"); 
		FrameUserSelector fus= new FrameUserSelector();
	}

}


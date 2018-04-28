import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelInicio extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Colors and Fonts
	private static final Color XCOLOR= new Color(249, 38, 114);
	private static final Color OCOLOR= new Color(102, 217, 239);
	private static final Color ORCOL = new Color(255,140,0);
	private static final Color BKG=new Color(39,40,34);//POR QUE NO JALA EL BKG'??
	private static final Font FONT=new Font("Arial",Font.BOLD,150);
	private static final Font FONT2=new Font("Arial",Font.BOLD,30);
	private static final Font FONT3=new Font("Arial",Font.BOLD,50);
	
	private Dimension dimen;
	private JTextField tfEntradaNombre;
	private JLabel jLab,jLab2,jLab3;
	private Image imgGato;
	private Client client;
	private VentanaInicio vi;
	
	public PanelInicio(VentanaInicio vi) {

		//Constructor panel
		super();
		this.vi=vi;
		Client c = Client.getClient();
		this.setBackground(BKG);
		this.dimen=new Dimension(870, 600);
		this.setPreferredSize(dimen);
		
		this.tfEntradaNombre=new JTextField(15);	
		this.tfEntradaNombre.setFont(FONT2);
		
		//text field username. Here is where you insert that field.
		
		this.tfEntradaNombre.addActionListener(new ActionListener() {
				boolean validUsername;
					public void actionPerformed(ActionEvent e) {
						//Insert username
						String tryUser=tfEntradaNombre.getText();
							if(tryUser.equals("")) {
								JOptionPane.showMessageDialog(null, "The username field can't be void.");
							}
							else {
								//COMPARA CON BBD. SI NOMBRE VALIDO, CAMBIA DE VENTANA.######
								validUsername=c.tryLogin(tryUser);
								//validUsername=true;//testing
								if(validUsername) {
									setVisible(false);
									FrameUserSelector fus= new FrameUserSelector();
									vi.dispose();
									
									
								}else {
									JOptionPane.showMessageDialog(null, "Please Select another name, this is already taken. ");
								}
							}
					}
		});
		this.cheapTrick();
		this.add(tfEntradaNombre);
		this.imgGato=new ImageIcon("GatoImage.jpg").getImage();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(this.imgGato, 0, this.getHeight()-350, 350, 277, this);
		g.setFont(FONT);
		g.setColor(XCOLOR);
		g.drawString("T", 80, 200);
		g.setColor(Color.WHITE);
		g.drawString("ic", 170, 200);
		g.setColor(OCOLOR);
		g.drawString("T", 320, 200);
		g.setColor(Color.WHITE);
		g.drawString("ac", 390, 200);
		g.setColor(ORCOL);
		g.drawString("T", 560, 200);
		g.setColor(Color.WHITE);
		g.drawString("oe", 650, 200);
		g.drawString("3D", 560, 380);
		g.setFont(FONT2);
		g.drawString("Introduce your username", 250, 500);
		
		
		
	}
	
	public void cheapTrick() {
		jLab=new JLabel("             ");
		jLab2=new JLabel("                     ");
		jLab3=new JLabel("                                  ");
		jLab.setFont(FONT);
		jLab2.setFont(FONT);
		jLab3.setFont(FONT3);
		this.add(jLab);
		this.add(jLab2);
		this.add(jLab3);
	}
}

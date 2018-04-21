import java.awt.Color;
import java.awt.Graphics;

public class GatoBoard {
	private int largo,
				x0,y0,
				anchoBar,
				largoCell;
	private JuegoCont jc;
	private String nombre;
	private Celda[] celdas;
	public GatoBoard(String nombre,int x0, int y0, int largo) {
		celdas= new Celda[9];
		this.largo=largo;
		this.y0=y0;
		this.x0=x0;
		this.largoCell=(int)(largo*76/250.0);
		this.anchoBar=(int)(largo*11/250.0);

		this.nombre=nombre;
		
		for(int i=0;i<celdas.length;i++) {
				celdas[i]=new Celda(x0+((anchoBar+largoCell)*(i%3)), y0+(anchoBar+largoCell)*(i/3), largoCell);
			}


	}
	public void paintComponet(Graphics g) {
		g.setColor(Color.WHITE);
		// # grid in white.
		g.fillRect(x0+largoCell, this.y0, this.anchoBar, this.largo);
		g.fillRect(x0+2*largoCell+anchoBar, this.y0, this.anchoBar, this.largo);
		g.fillRect(x0, y0+largoCell, this.largo, this.anchoBar);
		g.fillRect(x0, y0+2*largoCell+anchoBar, this.largo, this.anchoBar);
		
		for(Celda c:celdas) {
			c.paintComponent(g);
		}
	}
	public void setClick(int x,int y) {
		int nCelda, xC, yC;
		
		nCelda=(x/(this.largoCell+this.anchoBar))%3+(y/(this.largoCell+this.anchoBar))*3;
		xC=x%(this.largoCell+this.anchoBar);
		yC=y%(this.largoCell+this.anchoBar);
		if(xC<this.largoCell && yC<this.largoCell) {
			jc.move(nCelda,this.nombre); //cambio de estado, aparecer figura.
		}
	}
	
}

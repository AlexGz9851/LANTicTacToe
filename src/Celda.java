import java.awt.Color;
import java.awt.Graphics;

public class Celda {
	private static final Color XCOLOR= new Color(249, 38, 114);
	private static final Color OCOLOR= new Color(102, 217, 239);
	private static final Color BKG=new Color(39,40,34);
	private int largo,
				x0,y0;
	private char estado;
	public Celda(int x0,int y0,int largo,char estado) {
		this.estado=estado;
		this.largo=largo;
		this.x0=x0;
		this.y0=y0;
	}
	public Celda(int x0,int y0,int largo) {
		this.estado='N';//Default state.
		this.largo=largo;
		this.x0=x0;
		this.y0=y0;
	}
	public void paintComponent(Graphics g) {

		if(estado=='O') {
			g.setColor(OCOLOR);
			g.fillOval(x0+(int)(0.15*largo), y0+(int)(0.15*largo), (int)(0.7*largo), (int)(0.7*largo));
			g.setColor(BKG);
			g.fillOval(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));
			
		}else if(estado=='X') {
			g.setColor(XCOLOR);
			g.fillRect(x0+(int)(0.15*largo), y0+(int)(0.15*largo), (int)(0.7*largo), (int)(0.7*largo));
			g.setColor(BKG);
			g.fillRect(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));	
			
		}
	}
	public char getEstado() {
		return estado;
	}
	public void setEstado(char estado) {
		this.estado = estado;
	}

}

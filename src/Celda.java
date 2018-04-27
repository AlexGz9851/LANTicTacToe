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
			int[] xPoints= {(int)0.1*largo,(int)(1/3.0)*largo,(int)0.1*largo,
							(int).27*largo,(int).5*largo,     (int).73*largo,
							(int).9*largo, (int)(2/3.0)*largo,(int).9*largo,
							(int).73*largo,(int).5*largo,     (int).27*largo};
			
			int[] yPoints= {(int).27*largo,(int).5*largo,(int).73*largo,
					(int).9*largo,(int)(2/3.0)*largo,     (int).9*largo,
					(int).73*largo, (int).5*largo,(int).27*largo,
					(int).1*largo,(int)(1/3.0)*largo,     (int).1*largo};
			g.setColor(XCOLOR);
			g.fillPolygon(xPoints, yPoints, 12);
			//g.fillRect(x0+(int)(0.15*largo), y0+(int)(0.15*largo), (int)(0.7*largo), (int)(0.7*largo));
			//g.setColor(BKG);
			//g.fillRect(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));	
			
		}
	}
	public char getEstado() {
		return estado;
	}
	public void setEstado(char estado) {
		this.estado = estado;
	}

}

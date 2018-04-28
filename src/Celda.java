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
			g.fillOval(x0+(int)(0.1*largo), y0+(int)(0.1*largo), (int)(0.8*largo), (int)(0.8*largo));
			g.setColor(BKG);
			g.fillOval(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));
			
		}else if(estado=='X') {
			int[] xPoints= {x0+(int)(0.1*largo),x0+(int)((1/3.0)*largo),x0+(int)(0.1*largo),
					x0+(int)(.27*largo),x0+(int)(0.5*largo),     x0+(int)(0.73*largo),
					x0+(int)(.9*largo), x0+(int)((2/3.0)*largo),x0+(int)(0.9*largo),
					x0+(int)(.73*largo),x0+(int)(0.5*largo),  x0+   (int)(.27*largo)};
			
			int[] yPoints= { y0+(int)(0.27*largo), y0+(int)(.5*largo), y0+(int)(0.73*largo),
					 y0+(int)(0.9*largo), y0+(int)((2/3.0)*largo),      y0+(int)(0.9*largo),
					 y0+(int)(0.73*largo),  y0+(int)(.5*largo), y0+(int)(0.27*largo),
					 y0+(int)(0.1*largo), y0+(int)((1/3.0)*largo),      y0+(int)(0.1*largo)};
			
			g.setColor(XCOLOR);
			g.fillPolygon(xPoints, yPoints, 12);
		}
	}
	public char getEstado() {
		return estado;
	}
	public void setEstado(char estado) {
		this.estado = estado;
	}
	public int getX0() {
		return x0;
	}
	public int getY0() {
		return y0;
	}

}

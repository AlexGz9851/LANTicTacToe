import java.awt.Color;
import java.awt.Graphics;

public class Celda {
	private static final Color XCOLOR= new Color(249, 38, 114);
	private static final Color OCOLOR= new Color(102, 217, 239);
	private static final Color BKG=new Color(39,40,34);
	private int largo,
				x0,y0;
	private String estado;
	public Celda(int x0,int y0,int largo,String estado) {
		this.estado=estado;
		this.largo=largo;
		this.x0=x0;
		this.y0=y0;
	}
	public Celda(int x0,int y0,int largo) {
		this.estado="";
		this.largo=largo;
		this.x0=x0;
		this.y0=y0;
	}
	public void paintComponent(Graphics g) {

		if(estado.equals("O")) {
			g.setColor(OCOLOR);
			g.fillOval(x0+(int)(0.15*largo), y0+(int)(0.15*largo), (int)(0.7*largo), (int)(0.7*largo));
			g.setColor(BKG);
			g.fillOval(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));
			
		}else if(estado.equals("X")) {
			g.setColor(XCOLOR);
			g.fillRect(x0+(int)(0.15*largo), y0+(int)(0.15*largo), (int)(0.7*largo), (int)(0.7*largo));
			g.setColor(BKG);
			g.fillRect(x0+(int)(0.25*largo), y0+(int)(0.25*largo), (int)(0.5*largo), (int)(0.5*largo));	
			
		}
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

}

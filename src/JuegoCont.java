import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JuegoCont {
	private PanelJuego pj;
	private boolean turno;
	VentanaJuego vj;
	Client client;
	String j1,j2;
	public JuegoCont(boolean whoStart,Client client) {
		this.client=client;
		j1=this.client.getUser();
		j2=this.client.getOpponent();
		this.turno=whoStart;
		
		vj= new VentanaJuego();
		this.pj=vj.getPj();
		
		this.pj.setUsuario((whoStart)?j1:j2);
		this.pj.setOponente((whoStart)?j2:j1);
		this.pj.setTurno((whoStart)?j1:j2);
		this.pj.setBoardEnable(whoStart);
		
		this.pj.getGatoA().setJc(this);
		this.pj.getGatoB().setJc(this);
		this.pj.getGatoC().setJc(this);
	}
	public void move(int numero, String nombre) {
		String celda=""+numero+nombre;
		//enviar como dato la celda.
		
	}
}

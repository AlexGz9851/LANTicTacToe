
public enum Action {
	SURRENDER("surrender"),USERLIST("userList"), LOGOUT("logOut"), NEWUSER("newUser"), ERROR("error"), INICIOJUEGO("inicioJuego"), FINJUEGO("finJuego"), TURNO("turno"), OK("ok"), GAMEREQUEST("gameRequest");
	private String value;
	
	private Action(String value) {
		this.value = value;
	}
	
	@Override
    public String toString(){
        return this.value;
    }
    
    public static Action getValue(String text) {
    	for (Action b : Action.values()) {
			if (b.toString().equalsIgnoreCase(text)) {
				return b;
			}
    	}
    	throw new IllegalArgumentException("No enum for \""+text+"\"");
    }
}

public class ClientePersonaJuridica extends Cliente{
	private String cuit;
	public ClientePersonaJuridica(String nombre, String tipo, String numeroTelefono, String cuit){
		super(nombre, tipo, numeroTelefono);
		this.cuit = cuit;
	}
}
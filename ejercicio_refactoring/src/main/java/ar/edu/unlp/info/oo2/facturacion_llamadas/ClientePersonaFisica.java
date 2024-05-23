public class ClientePersonaFisica extends Cliente{
	private String dni;
	public ClientePersonaFisica(String nombre, String tipo, String numeroTelefono, String dni){
		super(nombre, tipo, numeroTelefono);
		this.dni = dni;
	}
}
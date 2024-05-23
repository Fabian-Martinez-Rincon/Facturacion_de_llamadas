public class ClientFactory{
	public static Cliente crearCliente(String tipo, String nombre, String numeroTelefono, String data){
		switch(tipo){
			case "fisica":
				return new ClientePersonaFisica(nombre, tipo, numeroTelefono, data);
			case "juridica":
				return new ClientePersonaJuridica(nombre, tipo, numeroTelefono, data);			
		}		
	}
}
<div>
<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=30&duration=1200&pause=1000&color=FFFFFF&width=900&center=true&lines=Ejercicio 3 - Facturación de llamadas"/>
</div>

_Importante: Aprobando este ejercicio, no será necesario rendir el tema Refactoring en el parcial. Fecha límite de entrega: 24/05/2024 23:59 hs (máximo 2 integrantes por grupo)._

En el material adicional encontrará una aplicación que registra y factura llamadas telefónicas. Para lograr tal objetivo, la aplicación permite administrar números telefónicos, como así también clientes asociados a un número. Los clientes pueden ser personas físicas o jurídicas. Además, el sistema permite registrar las llamadas realizadas, las cuales pueden ser nacionales o internacionales. Luego, a partir de las llamadas, la aplicación realiza la facturación, es decir, calcula el monto que debe abonar cada cliente.

Importe el material adicional provisto por la cátedra y analícelo para identificar y corregir los malos olores que presenta. En forma iterativa, realice los siguientes pasos:

- (i) indique el mal olor,
- (ii) indique el refactoring que lo corrige,
- (iii) aplique el refactoring (modifique el código).
- (iv) asegúrese de que los tests provistos corran exitosamente.
  Si vuelve a encontrar un mal olor, retorne al paso (i).

**Ud debe entregar:**

- Un diagrama de clases UML con el diseño inicial de la solución provista
- La secuencia de refactorings aplicados, documentados cada uno de la siguiente manera:
  - Mal olor detectado en el código
  - Extracto del código que presenta el mal olor
  - Refactoring a aplicar que resuelve el mal olor
  - Código con el refactoring aplicado
- Un diagrama de clases UML con el diseño final
- El código java refactorizado

**Importante:** asegúrese de que no queden malos olores por identificar y refactorizar.

---

### Refactorings

- [Move Method]()


## Empresa

#### Code Smell: Envidia de atributos (Feature Envy)
#### Refactoring: Move Method

Se observa una mala asignación de responsabilidades en la clase **Empresa**, asociada a una evidente envidia de atributos
Encontramos tareas en esta clase que deberían ser responsabilidad de **GestorNumeroDisponibles**.

En primer lugar, realizamos un **Move method** del método **agregarNumeroTelefono(String str)** a la clase **GestorNumeroDisponibles**. 



```java
public class Empresa{
	public boolean agregarNumeroTelefono(String str) {
		boolean encontre = guia.getLineas().contains(str);
		if (!encontre) {
			guia.getLineas().add(str);
			encontre = true;
			return encontre;
		} else {
			encontre = false;
			return encontre;
		}
	}
}
```


```java	
public class GestorNumerosDisponibles{
	public boolean agregarNumeroTelefono(String str) {
		boolean encontre = this.getLineas().contains(str);
		if (!encontre) {
			this.getLineas().add(str);
			encontre = true;
			return encontre;
		} else {
			encontre = false;
			return encontre;
		}
	}
}
```


#### Code Smell: Intermediario (Middle Man)
#### Refactoring: 

Se observa que el método **obtenerNumeroLibre()** de la clase **Empresa** lo unico que hace es delegar la responsabilidad a la variable **guia** de la clase **GestorNumerosDisponibles**, por lo que se procede a aplicar el refactoring. 


```java
public String obtenerNumeroLibre() {
	return guia.obtenerNumeroLibre();
}
```
</td><td>
	
```java	

```

### registrarUsuario()


#### Malos Olores Detectados:

1. **Duplicate Code**: El código original tenía estructuras condicionales repetitivas para configurar objetos de `Cliente`, variando solo en la asignación de `dni` para clientes físicos y `cuit` para clientes jurídicos. Esto no solo duplica el código sino que también complica las modificaciones futuras.

2. **Switch Statements**: Aunque en tu refactoring final aún se utiliza un switch, este es movido a una fábrica, lo cual es un lugar más apropiado que dispersarlo por el código de negocio.

3. **Large Class**: La clase `Cliente` en el código original podría expandirse desproporcionadamente si se agregaran más tipos de clientes, con más condiciones y más campos.

#### Refactoring a aplicar

```java
public Cliente registrarUsuario(String data, String nombre, String tipo) {
		Cliente var = new Cliente();
		if (tipo.equals("fisica")) {
			var.setNombre(nombre);
			String tel = this.obtenerNumeroLibre();
			var.setTipo(tipo);
			var.setNumeroTelefono(tel);
			var.setDNI(data);
		} else if (tipo.equals("juridica")) {
			String tel = this.obtenerNumeroLibre();
			var.setNombre(nombre);
			var.setTipo(tipo);
			var.setNumeroTelefono(tel);
			var.setCuit(data);
		}
		clientes.add(var);
		return var;
	}
```


1. **Extract Class**: Creaste subclases específicas para diferentes tipos de clientes (`ClientePersonaJuridica` y `ClientePersonaFisica`), lo que permite manejar los datos específicos de cada tipo de cliente de manera más efectiva.

2. **Polymorphism**: Al definir una clase abstracta `Cliente` y tener subclases concretas para diferentes tipos de clientes, utilizas el polimorfismo para manejar diferentes comportamientos basados en el tipo de cliente. Esto te permite agregar nuevos tipos de clientes sin modificar las funciones existentes.

3. **Factory Method**: Implementaste un método de fábrica `crearCliente` en la clase `ClientFactory`, centralizando la creación de los diferentes tipos de clientes. Esto desacopla el código que crea clientes del código que los usa, haciendo que el sistema sea más fácil de extender y mantener.

4. **Encapsulation**: Mejoraste la encapsulación al asegurar que los detalles específicos de cada tipo de cliente estén ocultos dentro de sus respectivas subclases. Esto también ayuda a mantener el principio de responsabilidad única, donde cada clase gestiona sus propios datos de manera autónoma.
	
```java	

public abstract class Cliente{
	private String nombre;
	private String tipo;
	private String numeroTelefono;

	public Cliente(String nombre, String tipo, String numeroTelefono){
		this.nombre = nombre;
		this.tipo = tipo;
		this.numeroTelefono = numeroTelefono;
	}

	public void setNombre(String nombre){
		this.nombre = nombre;
	}

	public void setTipo(String tipo){
		this.tipo = tipo;
	}

	public void setNumeroTelefono(String numeroTelefono){
		this.numeroTelefono = numeroTelefono;
	}	

	public getNombre(){
		return this.nombre;
	}
}

public class ClientePersonaJuridica extends Cliente{
	private String cuit;
	public ClientePersonaJuridica(String nombre, String tipo, String numeroTelefono, String cuit){
		super(nombre, tipo, numeroTelefono);
		this.cuit = cuit;
	}
}

public class ClientePersonaFisica extends Cliente{
	private String dni;
	public ClientePersonaFisica(String nombre, String tipo, String numeroTelefono, String dni){
		super(nombre, tipo, numeroTelefono);
		this.dni = dni;
	}
}

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

public Cliente registrarUsuario(String data, String nombre, TipoCliente tipo) {
    String tel = this.obtenerNumeroLibre();
    Cliente cliente = ClienteFactory.crearCliente(tipo, nombre, tel, data);
    clientes.add(cliente);
    return cliente;
}

```


En el refactoring que has realizado, has abordado varios code smells y aplicado varias técnicas de refactoring para mejorar la estructura y mantenibilidad del código original. Aquí te explico cómo podrías explicar los cambios realizados, los malos olores detectados, y los refactorings aplicados:



### Refactorings Aplicados:



### Explicación de los Cambios:

"Para abordar los problemas de duplicación de código y manejo de múltiples tipos de clientes en un solo método, he refactorizado el código para utilizar el patrón Polimorfismo junto con un Factory Method. Cada tipo de cliente ahora tiene su propia subclase que extiende una clase base abstracta `Cliente`, permitiendo que cada subclase maneje los datos específicos relevantes para su contexto.

Además, la creación de objetos `Cliente` se ha delegado a una fábrica, `ClientFactory`, que encapsula la lógica de creación de clientes. Esto centraliza la creación de clientes y facilita la adición de nuevos tipos de clientes en el futuro, ya que solo se requiere modificar la fábrica y añadir una nueva subclase sin cambiar el código cliente.

Este enfoque no solo elimina la duplicación y mejora la organización del código, sino que también hace que el código sea más flexible y fácil de mantener, especialmente a medida que el sistema evoluciona y se agregan nuevos tipos de clientes."

Este tipo de explicación detalla tanto los problemas originales como las soluciones aplicadas, proporcionando un claro entendimiento del por qué y cómo de los cambios realizados.

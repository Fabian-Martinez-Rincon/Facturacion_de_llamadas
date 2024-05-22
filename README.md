- [Empresa](#empresa)


## Empresa

#### Code Smell: Envidia de atributos (Feature Envy)

> Refactoring: Move Method

Se observa una mala asignación de responsabilidades en la clase **Empresa**, asociada a una evidente envidia de atributos.

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

---

#### Code Smell: Intermediario (Middle Man)
> Refactoring: 

Se observa que el método **obtenerNumeroLibre()** de la clase **Empresa** lo unico que hace es delegar la responsabilidad a la variable **guia** de la clase **GestorNumerosDisponibles**, por lo que se procede a aplicar el refactoring. 


```java
public String obtenerNumeroLibre() {
	return guia.obtenerNumeroLibre();
}
```
</td><td>
	
```java	

```

---

#### Malos Olores Detectados:

1. **Duplicate Code**: El código original tenía estructuras condicionales repetitivas para configurar objetos de `Cliente`, variando solo en la asignación de `dni` para clientes físicos y `cuit` para clientes jurídicos. Esto no solo duplica el código sino que también complica las modificaciones futuras.

2. **Switch Statements**: Aunque en tu refactoring final aún se utiliza un switch, este es movido a una fábrica, lo cual es un lugar más apropiado que dispersarlo por el código de negocio.

3. **Large Class**: La clase `Cliente` en el código original podría expandirse desproporcionadamente si se agregaran más tipos de clientes, con más condiciones y más campos.


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


#### Refactorings Aplicados
- **Extract Class:** Creamos dos subclases, ClientePersonaFisica y ClientePersonaJuridica, desde la clase original Cliente. Esto no solo eliminó la duplicación de código sino que también aseguró que cada subclase maneje sus propios datos específicos de manera encapsulada.

- **Replace conditional with Polymorphism:** Implementamos el polimorfismo para tratar de forma uniforme los diferentes tipos de clientes mediante la superclase Cliente. Cada subclase tiene su propia implementación de métodos que dependen de su tipo específico, lo cual permite una mejor extensibilidad y mantenibilidad.

- **Factory Method:** Introdujimos un método de fábrica crearCliente en la clase ClientFactory. Este método centraliza la creación de los objetos Cliente, desacoplando la lógica de creación del código cliente y facilitando la extensión del sistema para incorporar nuevos tipos de clientes en el futuro.

> Creo que tambien tenemos redundancia de datos, o algo asi

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

public Cliente registrarUsuario(String data, String nombre, String tipo) {    
    Cliente cliente = ClienteFactory.crearCliente(tipo, nombre, this.obtenerNumeroLibre(), data);
    clientes.add(cliente);
    return cliente;
}

```

---

#### Malos Olores Detectados:

- **Feature Envy:** El método muestra envidia de atributos ya que accede directamente a la lista de llamadas del Cliente origen para agregar una nueva llamada. Esto indica una alta dependencia de la estructura interna de otra clase, lo cual va en contra del principio de encapsulación.
- **Inappropriate Intimacy:** Manipular directamente la lista de llamadas del Cliente desde otra clase podría ser considerado una intimidad inapropiada, pues el método no solo conoce detalles internos de otra clase, sino que también los modifica directamente.

```java
public Llamada registrarLlamada(Cliente origen, Cliente destino, String t, int duracion) {
		Llamada llamada = new Llamada(t, origen.getNumeroTelefono(), destino.getNumeroTelefono(), duracion);
		llamadas.add(llamada);
		origen.llamadas.add(llamada);
		return llamada;
	}
```


- **Encapsulate Collection:** Podríamos encapsular la gestión de la colección de llamadas dentro de la clase Cliente. Esto implica crear métodos en la clase Cliente para añadir llamadas, en lugar de modificar la lista directamente desde fuera.
- **Move Method**: Consideramos mover el método registrarLlamada o parte de su funcionalidad a la clase Cliente si tiene más sentido que esta clase gestione sus propias llamadas.
- **Hide Delegate**: En lugar de que otras clases accedan y modifiquen directamente las propiedades de Cliente, proporcionar métodos en Cliente que encapsulen estas acciones.
- **Cambiar Visibilidad** Cambiamos la visibilidad de la lista de llamadas de Cliente a privada para evitar que otras clases la manipulen directamente.

```java
public class Cliente {
    private List<Llamada> llamadas = new ArrayList<>();

    public void agregarLlamada(Llamada llamada) {
        this.llamadas.add(llamada);
    }

    // Otros métodos...
}

public class Empresa {
    private List<Llamada> llamadas = new ArrayList<>();

    public Llamada registrarLlamada(Cliente origen, Cliente destino, String t, int duracion) {
        Llamada llamada = new Llamada(t, origen.getNumeroTelefono(), destino.getNumeroTelefono(), duracion);
        this.llamadas.add(llamada);
        origen.agregarLlamada(llamada);
        return llamada;
    }
}
```

---

#### Malos Olores Detectados:

El método `calcularMontoTotalLlamadas` en el código que proporcionaste tiene varios problemas de diseño y prácticas que podrían mejorarse mediante refactoring. Aquí está el análisis de los problemas y cómo podrías refactorizarlo:

### Análisis de Problemas

1. **Feature Envy y Inappropriate Intimacy**: El método accede directamente a la lista interna de llamadas del `Cliente`. Esto es un ejemplo de "Feature Envy", ya que el método parece más interesado en las propiedades internas de `Cliente` que en sus propias responsabilidades. Además, manipular directamente esta lista constituye una "Inappropriate Intimacy".

2. **String Comparison**: Está utilizando `==` para comparar strings, lo cual es inapropiado en Java para comparaciones de contenido de strings. Debería usar `.equals()`.

3. **Duplicate Code**: Hay código duplicado en la manera de calcular el costo de las llamadas, especialmente en cómo se añade el IVA y se calculan los descuentos.

4. **Magic Numbers**: El código contiene numeros mágicos como `3`, `150`, `0.21`, `50`, que hacen el código difícil de entender y mantener.

### Sugerencias de Refactoring

1. **Move Method**: Mover este método a la clase `Cliente` podría ser una opción para considerar, donde cada cliente calcula su propio total de llamadas. Esto centraliza la lógica que manipula directamente las propiedades de `Cliente`.

2. **Replace Conditional with Polymorphism**: Considerando que el cálculo del costo varía significativamente entre llamadas nacionales e internacionales, podría ser útil aplicar polimorfismo. Crear clases para cada tipo de llamada que implementen una interfaz común para calcular su costo.

3. **Encapsulate Collection**: Introduce métodos en `Cliente` para gestionar la iteración sobre las llamadas, en lugar de exponer la lista directamente.

4. **Extract Method**: Extraer partes del código de cálculo a métodos separados para mejorar la claridad y reusabilidad.

### Código Refactorizado

Aquí hay una versión refactorizada del método, aplicando algunos de los cambios sugeridos:

```java
public class Cliente {
    private List<Llamada> llamadas = new ArrayList<>();
    private String tipo;

    public double calcularMontoTotalLlamadas() {
        double total = 0;
        for (Llamada llamada : this.llamadas) {
            total += llamada.calcularCosto(this.tipo);
        }
        return total;
    }

    // Getters y setters...
}

public abstract class Llamada {
    protected int duracion;

    public abstract double calcularCosto(String tipoCliente);

    // Getters y setters...
}

public class LlamadaNacional extends Llamada {
    @Override
    public double calcularCosto(String tipoCliente) {
        double costo = this.duracion * 3 * 1.21;
        return aplicarDescuento(costo, tipoCliente);
    }
}

public class LlamadaInternacional extends Llamada {
    @Override
    public double calcularCosto(String tipoCliente) {
        double costo = (this.duracion * 150 + 50) * 1.21;
        return aplicarDescuento(costo, tipoCliente);
    }

    private double aplicarDescuento(double costo, String tipoCliente) {
        double descuento = tipoCliente.equals("fisica") ? Cliente.descuentoFis : Cliente.descuentoJur;
        return costo * (1 - descuento);
    }
}
```

### Explicación de los Cambios

- **Encapsulación y Polimorfismo**: La lógica para calcular el costo de las llamadas se ha movido a clases específicas por tipo de llamada, lo cual permite una mayor flexibilidad y hace que el código sea más fácil de entender y mantener.
- **Evitar Magic Numbers y Strings**: Se han eliminado los números mágicos y las comparaciones de strings inapropiadas mediante el uso de constantes y el método `.equals()`.
- **Responsabilidad Delegada**: `Cliente` ahora solo delega la responsabilidad de calcular el costo a las instancias de `Llamada`, lo cual es un diseño más limpio y modular.

```java
public double calcularMontoTotalLlamadas(Cliente cliente) {
		double c = 0;
		for (Llamada l : cliente.llamadas) {
			double auxc = 0;
			if (l.getTipoDeLlamada() == "nacional") {
				// el precio es de 3 pesos por segundo más IVA sin adicional por establecer la
				// llamada
				auxc += l.getDuracion() * 3 + (l.getDuracion() * 3 * 0.21);
			} else if (l.getTipoDeLlamada() == "internacional") {
				// el precio es de 150 pesos por segundo más IVA más 50 pesos por establecer la
				// llamada
				auxc += l.getDuracion() * 150 + (l.getDuracion() * 150 * 0.21) + 50;
			}

			if (cliente.getTipo() == "fisica") {
				auxc -= auxc * descuentoFis;
			} else if (cliente.getTipo() == "juridica") {
				auxc -= auxc * descuentoJur;
			}
			c += auxc;
		}
		return c;
}
```
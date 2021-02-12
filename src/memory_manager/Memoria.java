package memory_manager;
import java.util.LinkedList;
import java.util.ListIterator;

public class Memoria {

	private Integer cantidadMemoria;
	private Integer tiempoSeleccion;
	private Integer tiempoCarga;
	private Integer tiempoLiberacion;
	private Integer tiempoTotalRetorno;
	private Integer FE; //Fragmentacion externa
	private LinkedList<Particion> tablaParticiones;
	private LinkedList<Proceso> procesosCargados;
	private SelectorStrategy selector;
	

	public Memoria(Integer cantidadMemoriaTotal, Integer tiempoSeleccion, Integer tiempoCarga,
				   Integer tiempoLiberacion, SelectorStrategy selector) {
		
		this.cantidadMemoria = cantidadMemoriaTotal;
		this.tiempoSeleccion = tiempoSeleccion;
		this.tiempoCarga = tiempoCarga;
		this.tiempoLiberacion = tiempoLiberacion;
		this.tiempoTotalRetorno = 0;
		this.FE = 0;
		this.tablaParticiones = new LinkedList<>();
		this.procesosCargados = new LinkedList<>();
		this.selector = selector;
		//Crear la primer particion (que es toda la memoria)
		Particion particion = new Particion();
		particion.setCantidadMemoria(cantidadMemoriaTotal);
		particion.setDireccionComienzo(0);
		particion.setEstadoLibre();
		this.tablaParticiones.add(particion);
	} 
	

	public Particion seleccionarParticion(Proceso proceso) {
		this.actualizarIndiceFragmentacionExterna();
		return this.selector.SeleccionarParticion(proceso, this.tablaParticiones);
	}
	

	public void cargarProceso(Particion particion, Proceso proceso) {

		/*--------------------------------------------------------------
			Si el proceso no llena por completo la particion 
			tengo que crear una nueva particion e insertarla en la tabla
		----------------------------------------------------------------*/
		if (particion.getCantidadMemoria() > proceso.getCantidadMemoria()) {	
			
			// System.out.println("El proceso no llena la particion por completo, creando particion");
			Particion particionNueva = new Particion();
			particionNueva.setDireccionComienzo(particion.getDireccionComienzo() 
												+ proceso.getCantidadMemoria());
			particionNueva.setCantidadMemoria(particion.getCantidadMemoria() 
												- proceso.getCantidadMemoria());	

			/*--------------------------------------------------- 
				Si la particion elegida es la ultima en la lista, 
				inserto la nueva particion al final
			-----------------------------------------------------*/
			if (this.tablaParticiones.peekLast() == particion) {
				this.tablaParticiones.add(particionNueva);
			}
			else {// sino la inserto seguido a la anterior para mantener el orden
				int index = this.tablaParticiones.indexOf(particion);
				this.tablaParticiones.add(index+1, particionNueva);
			}
		}
		particion.setIdProceso(proceso.getId());	
		particion.setEstadoOcupado();
		particion.setCantidadMemoria(proceso.getCantidadMemoria());
		this.actualizarIndiceFragmentacionExterna();
		this.procesosCargados.add(proceso);
	}
	

	public void liberarParticion(Proceso proceso) {
		boolean encontrada = false;
		ListIterator<Particion> iterador = this.tablaParticiones.listIterator();
		
		int particionAnteriorIndex = -1;
		int particionSiguienteIndex = -1;
		Particion particion = null;
		Particion particionAnterior;
		Particion particionSiguiente;
		
		//mientras no encuentre la particion que tiene el proceso, sigo buscando
		while (!encontrada && iterador.hasNext()) {
			particion = iterador.next();
			if(particion.getIdProceso() == proceso.getId()) {
				encontrada = true;
				//guardo los indices de la sig y anterior para compactar si es posible
				particionAnteriorIndex = iterador.previousIndex()-1;
				particionSiguienteIndex = iterador.nextIndex();				
			}
		}
		
		if (encontrada) {
			particion.setEstadoLibre();
			particion.setIdProceso(-1);			
			// System.out.println("Particion liberada Intentando compactar");
			//Si existe particion siguiente y no esta ocupada, las compacto
			if(particionSiguienteIndex != this.tablaParticiones.size()) {
				particionSiguiente = this.tablaParticiones.get(particionSiguienteIndex);
				if(!particionSiguiente.isOcupado()) {
					particion.setCantidadMemoria(particion.getCantidadMemoria() + 
							particionSiguiente.getCantidadMemoria());
					this.tablaParticiones.remove(particionSiguienteIndex);
				}	
			}
			//Si existe particion anterior y no esta ocupada, las compacto
			if(particionAnteriorIndex != -1) {
				particionAnterior = this.tablaParticiones.get(particionAnteriorIndex);
				if(!particionAnterior.isOcupado()) {
					particion.setCantidadMemoria(particion.getCantidadMemoria() + 
							particionAnterior.getCantidadMemoria());
					particion.setDireccionComienzo(particionAnterior.getDireccionComienzo());
					this.tablaParticiones.remove(particionAnteriorIndex);
				}
			}
			this.procesosCargados.remove(proceso);
			this.actualizarIndiceFragmentacionExterna();
		}
	}


	public void imprimirTablaParticiones() {
		System.out.println("Particion ID Proceso ID Direc Tamanio Estado");
		System.out.println("----------------------------------------------");
		for (Particion particion : tablaParticiones) {
			System.out.println(particion.toString());
		}
		System.out.println("----------------------------------------------");
	}

	private void actualizarIndiceFragmentacionExterna() {
		Integer FE = 0;
		for(Particion particion : this.tablaParticiones) {
			if(!particion.isOcupado()) {
				FE += particion.getCantidadMemoria();
			}
		}
		this.FE = FE;
	}

	public Integer getFE(){
		return this.FE;
	}

	public LinkedList<Proceso> getProcesosCargados() {
		return this.procesosCargados;
	}

	public Integer getTiempoSeleccion() {
		return tiempoSeleccion;
	}

	public void setTiempoSeleccion(Integer tiempoSeleccion) {
		this.tiempoSeleccion = tiempoSeleccion;
	}

	public Integer getTiempoCarga() {
		return tiempoCarga;
	}

	public void setTiempoCarga(Integer tiempoCarga) {
		this.tiempoCarga = tiempoCarga;
	}

	public Integer getTiempoLiberacion() {
		return tiempoLiberacion;
	}

	public void setTiempoLiberacion(Integer tiempoLiberacion) {
		this.tiempoLiberacion = tiempoLiberacion;
	}

	public Integer getTiempoTotalRetorno() {
		return tiempoTotalRetorno;
	}

	public void setTiempoTotalRetorno(Integer tiempoTotalRetorno) {
		this.tiempoTotalRetorno = tiempoTotalRetorno;
	}
}

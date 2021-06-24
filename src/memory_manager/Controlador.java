package memory_manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Controlador {
    
    private Memoria memoria;
    //private Vista vista;

    public Controlador(Memoria memoria/*, Vista vista*/) {
        this.memoria = memoria;
        //this.vista = vista;
    }

    public boolean cargarProceso(Proceso proceso) {
		Particion particion = this.memoria.seleccionarParticion(proceso);
		this.memoria.setTiempoTotalRetorno(this.memoria.getTiempoTotalRetorno() + this.memoria.getTiempoSeleccion());									
		boolean exito;
		if (particion != null) {
			this.actualizarVista("Se selecciona particion " + particion.getID() + "para proceso " + proceso.getId());
			exito = true;	
			this.memoria.cargarProceso(particion, proceso);	
			this.memoria.setTiempoTotalRetorno(this.memoria.getTiempoTotalRetorno() + this.memoria.getTiempoCarga());	
			proceso.setTiempoArriboEfectivo(this.memoria.getTiempoTotalRetorno());
			this.actualizarVista("se carga proceso " + proceso.getId());
		}
		else{
			this.actualizarVista("No se pudo cargar el proceso "+proceso.getId()+" en memoria no hay espacio");
			exito = false;
		}
		return exito;
	}


	public LinkedList<Proceso> liberarProcesos() {
		LinkedList<Proceso> procesosCargados = new LinkedList<>();
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();

		procesosCargados.addAll(this.memoria.getProcesosCargados());
		for (Proceso proceso : procesosCargados) {
			if(this.memoria.getTiempoTotalRetorno() >= proceso.getTiempoRetorno()){
				this.memoria.liberarParticion(proceso);
				this.memoria.setTiempoTotalRetorno(this.memoria.getTiempoTotalRetorno() + this.memoria.getTiempoLiberacion());
				procesosLiberados.add(proceso);
				this.actualizarVista("se libera proceso " + proceso.getId());
			}
		}
		return procesosLiberados;
	}


	public LinkedList<Proceso> liberarTodosProcesos() {
		LinkedList<Proceso> procesosCargados = new LinkedList<>();
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();				
		procesosCargados = this.memoria.getProcesosCargados(); 

		Collections.sort(procesosCargados, new Comparator<Proceso>() {
			@Override
			public int compare(Proceso p1, Proceso p2) {
				return p1.getTiempoRetorno() - p2.getTiempoRetorno();
			}
		});
		for (Proceso proceso : procesosCargados){				
			this.memoria.liberarParticion(proceso);
			this.memoria.setTiempoTotalRetorno(proceso.getTiempoRetorno() + this.memoria.getTiempoLiberacion());
			procesosLiberados.add(proceso);
			this.actualizarVista("se libera proceso " + proceso.getId());
		}						
		return procesosLiberados;
	}

	public Integer actualizarFE(Integer tiempo) {
		return this.memoria.getFE() * tiempo;
	}

	public void actualizarTiempoRetornoTotal(){
		this.memoria.setTiempoTotalRetorno(memoria.getTiempoTotalRetorno() + 1);
		this.actualizarVista("se espera un tic porque no hay espacio");
	}

	public void actualizarVista(String accion){
		System.out.println("************************************************");
		System.out.println(accion);
		this.memoria.imprimirTablaParticiones();
		System.out.println("tiempo de retorno total parcial "+ this.memoria.getTiempoTotalRetorno());
		System.out.println("particion de la memoria en este momento "+ this.memoria.getFE());
		System.out.println("************************************************");
	}
    
}

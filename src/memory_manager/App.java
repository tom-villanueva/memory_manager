package memory_manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeMap;

public class App {

	public static TreeMap<Integer, Proceso> cargarProcesosDeArchivo(){
		return null;
	}
	
	public static void main(String[] args) {
		
		boolean exito = false;
		Integer FETotal = 0;
		Memoria memoria = new Memoria(150, 1, 1, 1, new NextFit());
		TreeMap<Integer, Proceso> procesosEnEspera 
					= new TreeMap<>();
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();
		LinkedList<Proceso> procesosFinalizados = new LinkedList<>();

		Proceso p1 = new Proceso(1, 0, 6, 20);
		Proceso p2 = new Proceso(2, 1, 10, 10);
		Proceso p3 = new Proceso(3, 2, 4, 40);
		Proceso p4 = new Proceso(4, 3, 11, 20);
		Proceso p5 = new Proceso(5, 4, 2, 30);
		Proceso p6 = new Proceso(6, 5, 9, 20);
		Proceso p7 = new Proceso(7, 6, 3, 10);
		Proceso p8 = new Proceso(8, 7, 2, 30);
		Proceso p9 = new Proceso(9, 8, 3, 50);
		Proceso p10 = new Proceso(10, 9, 2, 10);

		procesosEnEspera.put(p1.getTiempoArribo(), p1);
		procesosEnEspera.put(p2.getTiempoArribo(), p2);
		procesosEnEspera.put(p3.getTiempoArribo(), p3);
		procesosEnEspera.put(p4.getTiempoArribo(), p4);
		procesosEnEspera.put(p5.getTiempoArribo(), p5);
		procesosEnEspera.put(p6.getTiempoArribo(), p6);
		procesosEnEspera.put(p7.getTiempoArribo(), p7);
		procesosEnEspera.put(p8.getTiempoArribo(), p8);
		procesosEnEspera.put(p9.getTiempoArribo(), p9);
		procesosEnEspera.put(p10.getTiempoArribo(), p10);
		
		for (Proceso procesoNuevo : procesosEnEspera.values()) {
			System.out.println("tiempo total "+memoria.getTiempoTotalRetorno());
			exito = false;
			memoria.imprimirTablaParticiones();
			do {	
				/*---------------------------------------------------------
					Si el proceso a cargar no esta en su tiempo de arribo
					sigo contando un tic, sino lo intento cargar en memoria
				-----------------------------------------------------------*/
				if(memoria.getTiempoTotalRetorno() < procesoNuevo.getTiempoArribo()) {
					memoria.setTiempoTotalRetorno(memoria.getTiempoTotalRetorno() + 1);
					FETotal += memoria.getFE();
				}
				else {
					FETotal += (memoria.getFE()*memoria.getTiempoSeleccion());
					exito = cargarProceso(procesoNuevo, memoria);
					procesoNuevo.setTiempoRetorno(memoria.getTiempoTotalRetorno() + procesoNuevo.getTiempoDuracion());
					if(exito){						
						FETotal += (memoria.getFE()*memoria.getTiempoCarga());
					}
					memoria.imprimirTablaParticiones();
				}
				procesosLiberados.addAll(liberarProcesos(memoria));
				procesosFinalizados.addAll(procesosLiberados);

				for(int i = 1; i<= procesosLiberados.size(); i++) {
					FETotal += (memoria.getFE()*memoria.getTiempoLiberacion());
				}
				procesosLiberados.clear();
			}
			while (!exito);	
		}
		System.out.println("tiempo total "+memoria.getTiempoTotalRetorno());
		memoria.imprimirTablaParticiones();
		procesosFinalizados.addAll(liberarTodosProcesos(memoria));
		ImprimirResultados(procesosFinalizados, memoria, FETotal);
	} /* FIN DE MAIN*/


	public static boolean cargarProceso(Proceso proceso, Memoria memoria) {
		Particion particion = memoria.seleccionarParticion(proceso);
		memoria.setTiempoTotalRetorno(memoria.getTiempoTotalRetorno() + memoria.getTiempoSeleccion());									
		boolean exito;
		if (particion != null) {
			System.out.println("cargando proceso "+proceso.getId()+" en memoria");	
			exito = true;	
			memoria.cargarProceso(particion, proceso);	
			memoria.setTiempoTotalRetorno(memoria.getTiempoTotalRetorno() + memoria.getTiempoCarga());	
			proceso.setTiempoArriboEfectivo(memoria.getTiempoTotalRetorno());
		}
		else{
			System.out.println("No se pudo cargar el proceso "+proceso.getId()+" en memoria no hay espacio");
			exito = false;
		}
		return exito;
	}


	public static LinkedList<Proceso> liberarProcesos(Memoria memoria) {
		LinkedList<Proceso> procesosCargados = new LinkedList<>();
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();

		procesosCargados.addAll(memoria.getProcesosCargados());
		for (Proceso proceso : procesosCargados) {
			if(memoria.getTiempoTotalRetorno() >= proceso.getTiempoRetorno()){
				System.out.println("Liberando proceso "+proceso.getId());
				memoria.liberarParticion(proceso);
				memoria.setTiempoTotalRetorno(memoria.getTiempoTotalRetorno() + memoria.getTiempoLiberacion());
				procesosLiberados.add(proceso);
				memoria.imprimirTablaParticiones();
			}
		}
		return procesosLiberados;
	}

	public static LinkedList<Proceso> liberarTodosProcesos(Memoria memoria) {
		LinkedList<Proceso> procesosCargados = new LinkedList<>();
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();				
		procesosCargados = memoria.getProcesosCargados(); 

		Collections.sort(procesosCargados, new Comparator<Proceso>() {
			@Override
			public int compare(Proceso p1, Proceso p2) {
				return p1.getTiempoRetorno() - p2.getTiempoRetorno();
			}
		});
		for (Proceso proceso : procesosCargados){				
			memoria.liberarParticion(proceso);
			memoria.setTiempoTotalRetorno(proceso.getTiempoRetorno() + memoria.getTiempoLiberacion());
			procesosLiberados.add(proceso);
			memoria.imprimirTablaParticiones();
		}						
		return procesosLiberados;
	}

	public static void ImprimirResultados (LinkedList<Proceso> procesosFinalizados, Memoria memoria, Integer FETotal) {
		Integer total = 0;
		Integer cantidad = 0;
		for (Proceso proceso : procesosFinalizados) {
			cantidad += 1;
			total += proceso.getTiempoRetorno();
			System.out.println("Tiempo retorno proceso "+proceso.getId()+" "+proceso.getTiempoRetorno());
		}
		System.out.println("Tiempo retorno promedio: "+total/cantidad);
		System.out.println("Tiempo de retorno total: "+memoria.getTiempoTotalRetorno());
		System.out.println("FETotal: "+FETotal);
	}
}



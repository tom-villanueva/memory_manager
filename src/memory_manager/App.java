package memory_manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class App {

	public static LinkedList<Proceso> cargarProcesosDeArchivo(String fileName){
		LinkedList<Proceso> procesos = new LinkedList<>();
		Proceso proceso;
		System.out.println("entre");
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				proceso = new Proceso(Integer.parseInt(values[0]), 
									  Integer.parseInt(values[1]), 
									  Integer.parseInt(values[2]), 
									  Integer.parseInt(values[3]));
				System.out.println(proceso.toString());
				procesos.add(proceso);			
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		Collections.sort(procesos, new Comparator<Proceso>() {
			@Override
			public int compare(Proceso p1, Proceso p2) {
				return p1.getTiempoArribo() - p2.getTiempoArribo();
			}
		});
		return procesos;
	}
	
	public static void main(String[] args) {
		
		boolean exito = false;
		Integer FETotal = 0;
		Memoria memoria;
		Controlador controlador;
		
		System.out.println ("Empezamos el simulador");

        Integer tamanoMemoria = 0;
		String estrategia = "";
		SelectorStrategy estrategiaElegida; 
		Integer tiempoSeleccion = 0;
		Integer tiempoCarga = 0;
		Integer tiempoLiberacion = 0;

        Scanner entradaEscaner = new Scanner (System.in);
		System.out.println ("Ingrese tamanio de la memoria");
        tamanoMemoria = Integer.parseInt(entradaEscaner.nextLine());
		System.out.println ("Ingrese estrategia first-fit, best-fit, next-fit o worst-fit");
		estrategia = entradaEscaner.nextLine();
		System.out.println ("Ingrese tiempo de seleccion de la particion");
		tiempoSeleccion = Integer.parseInt(entradaEscaner.nextLine());
		System.out.println ("Ingrese tiempo de carga promedio");
		tiempoCarga = Integer.parseInt(entradaEscaner.nextLine());
		System.out.println ("Ingrese tiempo de liberacion");
		tiempoLiberacion = Integer.parseInt(entradaEscaner.nextLine());
		entradaEscaner.close();
		switch (estrategia) {
			case "first-fit":
				estrategiaElegida = new FirstFit();
				break;
			case "best-fit":
				estrategiaElegida = new BestFit();
				break;
			case "next-fit":
				estrategiaElegida = new NextFit();
				break;
			case "worst-fit":
				estrategiaElegida = new WorstFit();
				break;
			default:
				estrategiaElegida = new FirstFit();
				break;
		}

		memoria = new Memoria(tamanoMemoria, 
							  tiempoSeleccion, 
							  tiempoCarga, 
							  tiempoLiberacion, 
							  estrategiaElegida);

		controlador = new Controlador(memoria);
		
		LinkedList<Proceso> procesosLiberados = new LinkedList<>();
		LinkedList<Proceso> procesosFinalizados = new LinkedList<>();
		System.out.println("por entrar a cargar archivo");
		LinkedList<Proceso> procesosEnEspera = cargarProcesosDeArchivo("datos.csv");
		System.out.println("saliendo de cargar archivo");
		for (Proceso procesoNuevo : procesosEnEspera) {
			exito = false;
			do {	
				/*---------------------------------------------------------
					Si el proceso a cargar no esta en su tiempo de arribo
					sigo contando un tic, sino lo intento cargar en memoria
				-----------------------------------------------------------*/
				if(memoria.getTiempoTotalRetorno() < procesoNuevo.getTiempoArribo()) {
					controlador.actualizarTiempoRetornoTotal();
					FETotal += controlador.actualizarFE(1);
				}
				else {
					FETotal += controlador.actualizarFE(tiempoSeleccion);
					exito = controlador.cargarProceso(procesoNuevo);
					procesoNuevo.setTiempoRetorno(memoria.getTiempoTotalRetorno() + procesoNuevo.getTiempoDuracion());
					if(exito){						
						FETotal += controlador.actualizarFE(tiempoCarga);
					}
					memoria.imprimirTablaParticiones();
				}
				procesosLiberados.addAll(controlador.liberarProcesos());
				procesosFinalizados.addAll(procesosLiberados);

				for(int i = 1; i<= procesosLiberados.size(); i++) {
					FETotal += controlador.actualizarFE(tiempoLiberacion);
				}
				procesosLiberados.clear();
			}
			while (!exito);	
		}
		procesosFinalizados.addAll(controlador.liberarTodosProcesos());
		ImprimirResultados(procesosFinalizados, memoria, FETotal);
	} /* FIN DE MAIN*/

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



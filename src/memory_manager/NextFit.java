package memory_manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class NextFit implements SelectorStrategy {
    public Particion SeleccionarParticion(Proceso proceso, LinkedList<Particion> tablaParticiones) {

        //El ID mas grande es la ultima particion
        Particion particionElegida;
        Particion ultimaParticion = Collections.max(tablaParticiones, new Comparator<Particion>() {
			@Override
			public int compare(Particion p1, Particion p2) {
				return p1.getID() - p2.getID();
			}
		});
        System.out.println("particion elegida "+ultimaParticion.getID());
        int indexUltimaParticion = tablaParticiones.indexOf(ultimaParticion);

        // Primer recorrida hasta el final
        for(int i = indexUltimaParticion; i < tablaParticiones.size(); i++) {
            particionElegida = tablaParticiones.get(i);
            if(!particionElegida.isOcupado() && 
                particionElegida.getCantidadMemoria() >= proceso.getCantidadMemoria()) {
                return particionElegida;
            }
        }

        //SIno encuentro particion, recorrida desde el principio
        for(int i = 0; i < indexUltimaParticion; i++) {
            particionElegida = tablaParticiones.get(i);
            if(!particionElegida.isOcupado() && 
                particionElegida.getCantidadMemoria() >= proceso.getCantidadMemoria()) {
                return particionElegida;
            } 
        }

        return null;
    }
    
}

package memory_manager;

import java.util.LinkedList;
import java.util.ListIterator;

public class WorstFit implements SelectorStrategy {
    public Particion SeleccionarParticion(Proceso proceso, LinkedList<Particion> tablaParticiones) {
        
        ListIterator<Particion> iteradorParticiones = tablaParticiones.listIterator();
        Particion particion; // = iteradorParticiones.next();
        int diferencia;
        Particion mejorParticion = null;
        int mejorDiferencia = -1;

        //encuentro la primer particion libre
        while(iteradorParticiones.hasNext()) {
            particion = iteradorParticiones.next();
            if(!particion.isOcupado() &&
                particion.getCantidadMemoria() >= proceso.getCantidadMemoria()){
                diferencia = particion.getCantidadMemoria() - proceso.getCantidadMemoria();
                if(mejorDiferencia == -1 || diferencia > mejorDiferencia){
                    mejorDiferencia = diferencia;
                    mejorParticion = particion;
                }
            }
            
        }

        return mejorParticion;

    }
}

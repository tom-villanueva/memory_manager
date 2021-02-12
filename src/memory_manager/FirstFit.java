package memory_manager;

import java.util.LinkedList;
import java.util.ListIterator;

public class FirstFit implements SelectorStrategy{
    
    @Override
    public Particion SeleccionarParticion(Proceso proceso, LinkedList<Particion> tablaParticiones){
        
        ListIterator<Particion> iteradorParticiones = tablaParticiones.listIterator();
        Particion particionElegida; 
        System.out.println("eligiendo particion para proceso "+proceso.getId());
        while(iteradorParticiones.hasNext()) {

            particionElegida = iteradorParticiones.next();
            if (!particionElegida.isOcupado() && 
                 particionElegida.getCantidadMemoria() >= proceso.getCantidadMemoria()) {
                    return particionElegida;  
            }

        }
        return null;
    }
}

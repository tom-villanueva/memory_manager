package memory_manager;

import java.util.LinkedList;
import java.util.ListIterator;

public interface SelectorStrategy {

	public Particion SeleccionarParticion(Proceso proceso, LinkedList<Particion> tablaParticiones);
}

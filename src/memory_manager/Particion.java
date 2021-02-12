package memory_manager;

public class Particion {
	public enum estadosPosibles {OCUPADO, LIBRE};
	public static Integer siguienteID = 0;
	
	private Integer idProceso;
	private Integer ID;
	private Integer direccionComienzo;
	private Integer cantidadMemoria;	
	private estadosPosibles estado;
	
	public Particion() {
		this.ID = siguienteID++;
		this.idProceso = -1; //No tiene proceso asignado
		this.estado = estadosPosibles.LIBRE;
	}

	public String toString() {
		return  this.ID + "          |" + this.idProceso + "         |" + this.direccionComienzo 
				+ "      " + this.cantidadMemoria + "      " + this.estado;  
	}

	public Integer getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Integer idProceso) {
		this.idProceso = idProceso;
	}

	public Integer getID() {
		return ID;
	}

	public Integer getDireccionComienzo() {
		return direccionComienzo;
	}

	public void setDireccionComienzo(Integer direccionComienzo) {
		this.direccionComienzo = direccionComienzo;
	}

	public Integer getCantidadMemoria() {
		return cantidadMemoria;
	}

	public void setCantidadMemoria(Integer cantidadMemoria) {
		this.cantidadMemoria = cantidadMemoria;
	}

	public boolean isOcupado() {
		if (this.estado == estadosPosibles.OCUPADO) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setEstadoOcupado() {
		this.estado = estadosPosibles.OCUPADO;
	}
	
	public void setEstadoLibre() {
		this.estado = estadosPosibles.LIBRE;
	}
}

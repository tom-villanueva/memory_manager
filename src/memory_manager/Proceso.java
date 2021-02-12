package memory_manager;

public class Proceso {
	private Integer id;
	private Integer tiempoArribo;
	private Integer tiempoDuracion;
	private Integer cantidadMemoria;
	private Integer tiempoArriboEfectivo;
	private Integer tiempoRetorno;
	
	public Proceso(Integer id, Integer tiempoArribo, Integer tiempoDuracion, Integer cantidadMemoria) {
		this.id = id;
		this.tiempoArribo = tiempoArribo;
		this.tiempoDuracion = tiempoDuracion;
		this.cantidadMemoria = cantidadMemoria;
	}

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getTiempoArribo() {
		return tiempoArribo;
	}


	public void setTiempoArribo(Integer tiempoArribo) {
		this.tiempoArribo = tiempoArribo;
	}


	public Integer getTiempoDuracion() {
		return tiempoDuracion;
	}


	public void setTiempoDuracion(Integer tiempoDuracion) {
		this.tiempoDuracion = tiempoDuracion;
	}


	public Integer getCantidadMemoria() {
		return cantidadMemoria;
	}


	public void setCantidadMemoria(Integer cantidadMemoria) {
		this.cantidadMemoria = cantidadMemoria;
	}


	public Integer getTiempoArriboEfectivo() {
		return tiempoArriboEfectivo;
	}


	public void setTiempoArriboEfectivo(Integer tiempoArriboEfectivo) {
		this.tiempoArriboEfectivo = tiempoArriboEfectivo;
	}
	
	public Integer getTiempoRetorno() {
		return tiempoRetorno;
	}


	public void setTiempoRetorno(Integer tiempoRetorno) {
		this.tiempoRetorno = tiempoRetorno;
	}
	
}

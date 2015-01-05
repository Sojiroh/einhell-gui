package cl.facele.einhell.html;

import java.io.File;

public class DTEBean {

	private String rutEmisorDTE;
	private String tipoDTE;
	private String folioDTE;
	private String contenido;
	private File fileXML;
	private String contenidoTXT;
	private String sucursalEmisorDTE;
	private String pathtxt;
        private String codigoKaeser;
        
        public String getCodigokaeser() {
		return codigoKaeser;
	}

	public void setCodigokaeser(String codigoKaeser) {
		this.codigoKaeser = codigoKaeser;
	}

	public String getPathtxt() {
		return pathtxt;
	}

	public void setPathtxt(String pathtxt) {
		this.pathtxt = pathtxt;
	}

	public void setRutEmisor(String rut) {
		rutEmisorDTE = rut;
	}

	public String getRutEmisor() {
		return this.rutEmisorDTE;
	}

	public void setTipoDTE(String TipoDTE) {
		tipoDTE = TipoDTE;
	}

	public String getTipoDTE() {
		return this.tipoDTE;
	}

	public void setFolioDTE(String folio) {
		folioDTE = folio;
	}

	public String getFolioDTE() {
		return this.folioDTE;
	}

	public void setContendioFile(String Contenido) {
		contenido = Contenido;
	}

	public String getContenido() {
		return this.contenido;
	}

	public void setFile(File file) {
		fileXML = file;
	}

	public File getFile() {
		return this.fileXML;
	}

	public void setTXT(String txt) {
		contenidoTXT = txt;
	}

	public String getTXT() {
		return this.contenidoTXT;
	}

	public void setSucursalEmisorDTE(String sucursal) {
		sucursalEmisorDTE = sucursal;
	}

	public String getSucursalEmisorDTE() {
		return this.sucursalEmisorDTE;
	}

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author eugeniavogt
 */
public class DomicilioFiscal extends Base {

    private String calle;
    private int numero;
    private String ciudad;
    private Provincia provincia;
    private String codigoPostal;
    private Pais pais;

    public DomicilioFiscal(int id, String calle, int numero, String ciudad, Provincia provincia, String codigoPostal, Pais pais) {
        super(id, false);
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
    }

    public DomicilioFiscal() {
    }

    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public Pais getPais() {
        return pais;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "domic_id:" + getId()
                + ", calle:" + calle
                + ", numero:" + numero
                + ", ciudad:" + ciudad
                + ", provincia:" + provincia
                + ", codigoPostal:" + codigoPostal
                + ", pais:" + pais
                + ", domic_eliminado:" + isEliminado();
    }

}

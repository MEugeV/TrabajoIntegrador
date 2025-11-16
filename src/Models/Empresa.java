/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author eugeniavogt
 */
public class Empresa extends Base {

    private String razonSocial;
    private String cuit;
    private ActividadPrincipal actividadPrincipal;
    private String email;
    private DomicilioFiscal domicilioFiscal;

    public Empresa(int id, String razonSocial, String cuit, ActividadPrincipal actividadPrincipal, String email) {
        super(id, false);
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
    }

    public Empresa() {
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public ActividadPrincipal getActividadPrincipal() {
        return actividadPrincipal;
    }

    public String getEmail() {
        return email;
    }

    public DomicilioFiscal getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public void setActividadPrincipal(ActividadPrincipal actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    @Override
    public String toString() {
        return "\n emp_id:" + getId()
                + ", razonSocial:" + razonSocial
                + ", cuit:" + cuit
                + ", actividadPrincipal:" + actividadPrincipal
                + ", email:" + email
                + ", emp_eliminado:" + isEliminado()
                + ", \n Domicilio Fiscal: " + domicilioFiscal;
    }

}

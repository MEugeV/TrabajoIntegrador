/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Models;

import static Models.Provincia.values;

/**
 *
 * @author eugeniavogt
 */
public enum ActividadPrincipal implements Labelled {
    ACTIVIDADES_ADMINISTRATIVAS("Actividades administrativas"),
    ADMINISTRACION_PUBLICA("Administracion publica"),
    AGRICULTURA("Agricultura"),
    GANADERIA("Ganadería"),
    SERVICIOS_DEPORTIVOS("Servicios deportivos"),
    COMERCIO("Comercio"),
    CONSTRUCCION("Construcción"),
    ENSENANZA("Enseñanza"),
    EXPLOTACION_MINAS("Explotación de minas"),
    INDUSTRIA_MANUFACTURERA("Industria manufacturera"),
    INFORMACION_COMUNICACIONES("Información y comunicaciones"),
    INTERMEDIACION_FINANCIERA("Intermediación financiera"),
    SALUD("Salud"),
    SERVICIOS_ARTISTICOS("Servicios artísticos"),
    SERVICIOS_ALOJAMIENTO("Servicios de alojamiento"),
    SERVICIOS_COMIDA("Servicios de comida"),
    SERVICIOS_TRANSPORTE("Servicios de transporte"),
    SERVICIOS_INMOBILIARIOS("Servicios inmobiliarios");

    private final String label;

    ActividadPrincipal(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ActividadPrincipal fromLabel(String label) {
        for (ActividadPrincipal a : values()) {
            if (a.label.equalsIgnoreCase(label)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Actividad principal inválida: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}

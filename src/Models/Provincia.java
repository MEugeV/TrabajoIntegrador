/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Models;

/**
 *
 * @author eugeniavogt
 */
public enum Provincia implements Labelled {
    BUENOS_AIRES("Buenos Aires"),
    CATAMARCA("Catamarca"),
    CHACO("Chaco"),
    CHUBUT("Chubut"),
    CORDOBA("Córdoba"),
    CORRIENTES("Corrientes"),
    ENTRE_RIOS("Entre Ríos"),
    FORMOSA("Formosa"),
    JUJUY("Jujuy"),
    LA_PAMPA("La Pampa"),
    LA_RIOJA("La Rioja"),
    MENDOZA("Mendoza"),
    MISIONES("Misiones"),
    NEUQUEN("Neuquén"),
    RIO_NEGRO("Río Negro"),
    SALTA("Salta"),
    SAN_JUAN("San Juan"),
    SAN_LUIS("San Luis"),
    SANTA_CRUZ("Santa Cruz"),
    SANTA_FE("Santa Fe"),
    SANTIAGO_DEL_ESTERO("Santiago del Estero"),
    TIERRA_DEL_FUEGO("Tierra del Fuego"),
    TUCUMAN("Tucumán"),
    CABA("Ciudad Autonoma de Buenos Aires");

    private final String label;

    Provincia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Provincia fromLabel(String label) {
        for (Provincia p : values()) {
            if (p.label.equalsIgnoreCase(label)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Provincia inválida: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}

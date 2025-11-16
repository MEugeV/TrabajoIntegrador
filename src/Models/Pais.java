/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Models;

/**
 *
 * @author eugeniavogt
 */
public enum Pais implements Labelled {
    ARGENTINA("Argentina");

    private final String label;

    Pais(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Pais fromLabel(String label) {
        for (Pais p : values()) {
            if (p.label.equalsIgnoreCase(label)) {
                return p;
            }
        }
        throw new IllegalArgumentException("País inválido: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}

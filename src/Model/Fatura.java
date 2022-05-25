package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Fatura implements Serializable {
    private final float valor;
    private final String nif;
    private final String companhia;
    private final LocalDate dia;

    public Fatura(float valor, String nif, String companhia, LocalDate dia) {
        this.valor = valor;
        this.nif = nif;
        this.dia = dia;
        this.companhia = companhia;
    }

    public String getNif() {
        return nif;
    }

    public float getValor() {
        return valor;
    }

    public LocalDate getDia() {
        return dia;
    }

    public String getCompanhia() {
        return companhia;
    }

    @Override
    public String toString() {
        return "Fatura:\n" +
                "Valor:" + valor +
                "\nNif:" + nif +
                "\nDistribuidora:" + companhia +
                "\nDia de emissão:" + dia;
    }
}

package Model;

import Controller.EqTree;

import java.util.Random;

public class EletricalCompany {

    private final String nome;
    private String equation;
    private float tax;
    private float discount;
    private float fixValue;

    public EletricalCompany(String nome) {
        Random r = new Random();
        this.nome = nome;
        this.equation = "num > 10 ? (val*com* (1 + tax)) * 0.9 : (val * com * (1 + tax)) * 0.75";
        this.tax = r.nextFloat(0f,0.50f);
        this.fixValue = r.nextFloat(1f,23f);
        this.tax = r.nextFloat(0f,0.50f);
        this.discount = r.nextFloat(0f,1f);
    }

    public EletricalCompany(String nome, String equation, float tax, float discount, float fixValue) {
        this.nome = nome;
        this.equation = equation;
        this.tax = tax;
        this.discount = discount;
        this.fixValue = fixValue;
    }

    public String getNome() {
        return nome;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getFixValue() {
        return fixValue;
    }

    public void setFixValue(float fixValue) {
        this.fixValue = fixValue;
    }

    public boolean check(String cond) {
        cond = cond.replaceAll("\\s","");
        var num = cond.split("[<>]|==|!=");
        var sym = cond.split("-?\\d+");

        return switch (sym[0]) {
            case "<" -> Float.parseFloat(num[0]) < Float.parseFloat(num[1]);
            case ">" -> Float.parseFloat(num[0]) > Float.parseFloat(num[1]);
            case "==" -> Float.parseFloat(num[0]) == Float.parseFloat(num[1]);
            default -> Float.parseFloat(num[0]) != Float.parseFloat(num[1]);
        };

    }

    public float calculateAmount(int devices, float res) {
        equation = equation.replaceAll("tax","" + tax);
        equation = equation.replaceAll("val","" + fixValue);
        equation = equation.replaceAll("com","" + res);

        var sp = equation.split(":");

        if (sp.length > 1) {
            sp[0] = sp[0].replaceAll("num",""+ devices);
            var ss = sp[0].split("\\?");
            if(check(ss[0]))
                return new EqTree(ss[1]).result() *discount;
            else
                return new EqTree(sp[1]).result() * discount;
        }

        return new EqTree(equation).result() * discount;
    }

    @Override
    public String toString() {
        return "EletricalCompany{" +
                "nome='" + nome + '\'' +
                ", equation='" + equation + '\'' +
                ", tax=" + tax +
                ", discount=" + discount +
                ", fixValue=" + fixValue +
                '}';
    }
}

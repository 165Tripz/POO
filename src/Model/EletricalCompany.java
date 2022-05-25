package Model;

import Controller.EqTree;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class EletricalCompany implements OrderManager, Serializable {

    private final String nome;
    private String equation;
    private static float tax;

    private final ArrayList<Fatura> faturasEmitidas = new ArrayList<>();

    static {
        tax = new Random().nextFloat(0.12f,0.50f);
    }

    private float discount;
    private float fixValue;

    public EletricalCompany(String nome) {
        Random r = new Random();
        this.nome = nome;
        this.equation = "num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375";
        this.fixValue = r.nextFloat(1f,2.3f);
        this.discount = r.nextFloat(0.5f,1f);
    }

    public EletricalCompany(String nome, Float fixValue, Float discount, String equation) {
        this.nome = nome;
        this.discount = discount;
        this.fixValue = fixValue;
        this.equation = equation;
    }

    public EletricalCompany(String[] obj) {
        this.nome = obj[0];
        this.discount = Float.parseFloat(obj[1]);
        tax = Float.parseFloat(obj[2]);
        this.fixValue = Float.parseFloat(obj[3]);
        this.equation = obj[4];
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
        EletricalCompany.tax = tax;
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

    public Fatura calculateAmount(String nif, int devices, float res, LocalDate time) {
        equation = equation.replaceAll("tax","" + tax);
        equation = equation.replaceAll("val","" + fixValue);
        equation = equation.replaceAll("com","" + res);

        var sp = equation.split(":");
        if (sp.length > 1) {
            sp[0] = sp[0].replaceAll("num",""+ devices);
            var ss = sp[0].split("\\?");
            if(check(ss[0])) {
                Fatura e = new Fatura(new EqTree(ss[1]).result() * discount, nif, nome, time);
                faturasEmitidas.add(e);
                return e;
            }else {
                Fatura e = new Fatura(new EqTree(sp[1]).result() * discount, nif, nome, time);
                faturasEmitidas.add(e);
                return e;
            }
        }
        Fatura e = new Fatura(new EqTree(equation).result() * discount,nif,nome,time);
        faturasEmitidas.add(e);
        return e;
    }

    public ArrayList<Fatura> getFaturasEmitidas() {
        return faturasEmitidas;
    }

    @Override
    public String toString() {
        return "EletricalCompany{" +
                "\nnome='" + nome + '\'' +
                "\n, equation='" + equation + '\'' +
                "\n, tax=" + tax +
                "\n, discount=" + discount +
                "\n, fixValue=" + fixValue +
                "\n, valorFaturado=" + faturasEmitidas.stream().mapToDouble(Fatura::getValor).sum() +
                '}';
    }

    @Override
    public void execute(Orders order) throws Exception {
        switch (order.getDetails()[1]) {
            case "Tax" -> tax = Float.parseFloat(order.getDetails()[2]);
            case "Value" -> fixValue = Float.parseFloat(order.getDetails()[2]);
            case "Discount" -> discount = Float.parseFloat(order.getDetails()[2]);
            case "Equation" -> equation = order.getDetails()[2];
            default -> throw new Exception("No such option.");
        }
        System.out.println(order.getDayOfUse() + ":Order Company " + nome + " " + order.getDetails()[1] + " " + order.getDetails()[2] + " successful.");
    }
}

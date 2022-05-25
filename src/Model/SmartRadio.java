package Model;

import Controller.EqTree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class SmartRadio extends SmartDevices implements OrderManager , Serializable {

    private final String brand;
    private int volume;
    private String radio;

    private static final HashMap<String,Float> brands = new HashMap<>();
    static {
        brands.put("Logitech",0.85f);
        brands.put("Pioneer",0.47f);
        brands.put("Pyle",0.65f);
        brands.put("JBL",1.5f);
        brands.put("Bose",0.8f);
        brands.put("Alpine",1.2f);
        brands.put("Xiaomi",1.8f);
        brands.put("Samsung",2.2f);
        brands.put("Sony",3f);
    }

    private static final HashMap<String,Float> brandPrices = new HashMap<>();
    static {
        brandPrices.put("Logitech",300f);
        brandPrices.put("Pioneer",600f);
        brandPrices.put("Pyle",450f);
        brandPrices.put("JBL",120f);
        brandPrices.put("Bose",270f);
        brandPrices.put("Alpine",370f);
        brandPrices.put("Xiaomi",150f);
        brandPrices.put("Samsung",100f);
        brandPrices.put("Sony",80f);
    }


    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getRadio() {
        return radio;
    }

    public SmartRadio(String brand) {
        this.brand = brand;
        this.radio = "";
        this.volume = 50;
        if (!brandPrices.containsKey(brand)) {
            brandPrices.put(brand,new Random().nextFloat(80f,500f));
            brands.put(brand,new Random().nextFloat(0.40f,5f));
        }
        super.price = brandPrices.get(brand);
        this.setConsumo(new Random().nextFloat(1.0f,10.0f));
        super.manufacturerId = id;
        id++;
        super.changeState(false);
    }

    public SmartRadio(int volume,String brand, String radio,float consumo) {
        this.brand = brand;
        this.radio = radio;
        this.volume = volume;
        if (!brandPrices.containsKey(brand)) {
            brandPrices.put(brand,new Random().nextFloat(80f,500f));
            brands.put(brand,new Random().nextFloat(0.40f,5f));
        }
        super.price = brandPrices.get(brand);
        this.setConsumo(consumo);
        super.manufacturerId = id;
        id++;
        super.changeState(false);
    }

    public SmartRadio(String[] data) {
        this.brand = data[2];
        this.radio = data[1];
        this.volume = Integer.parseInt(data[0]);
        if (!brandPrices.containsKey(brand)) {
            brandPrices.put(brand,new Random().nextFloat(80f,500f));
            brands.put(brand,new Random().nextFloat(0.40f,5f));
        }
        super.price = brandPrices.get(brand);
        this.setConsumo(Float.parseFloat(data[3]));
        super.manufacturerId = id;
        id++;
    }

    @Override
    public float getDailyComsumption() {

        return brands.get(brand) * volume * super.getConsumo()*0.01f;
    }

    @Override
    public void execute(Orders order) throws Exception {
        switch (order.getDetails()[3]) {
            case "Signal" -> super.changeState(Boolean.getBoolean(order.getDetails()[4]));
            case "Volume" -> setVolume(Integer.parseInt(order.getDetails()[4]));
            case "Channel" -> setRadio(order.getDetails()[4]);
            default -> throw new Exception("No such option.");
        }
        System.out.println(order.getDayOfUse() + ":Order SmartRadio " + manufacturerId + " " + order.getDetails()[3] + " " + order.getDetails()[4] + " successful.");
    }
}

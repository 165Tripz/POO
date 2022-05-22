package Model;

import java.util.HashMap;

public class SmartRadio extends SmartDevices {

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
        super.price = brandPrices.get(brand);
        super.manufacturerId = id;
        id++;
        super.changeState(false);
    }

    @Override
    public float getDailyComsumption() {
        return brands.get(brand) * volume * 0.5f;
    }
}

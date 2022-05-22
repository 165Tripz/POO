package Model;

import java.util.HashMap;
import java.util.Random;

public class SmartLamp extends SmartDevices {

    private String state = "Neutral";

    private static final HashMap<String,Float> factors = new HashMap<>();
    static {
        factors.put("Cold",50f);
        factors.put("Neutral",100f);
        factors.put("Warm",150f);
    }
    private final float dimension;

    public SmartLamp(float dimension) {
        super.price = 5f;
        super.manufacturerId = id;
        id++;
        this.state = "Neutral";
        super.changeState(false);
        this.dimension = dimension;
        super.setConsumo(new Random().nextFloat(1,dimension));
    }

    public SmartLamp(String state,float dimension,float consumo) {
        super.price = 5f;
        super.manufacturerId = id;
        id++;
        if (factors.containsKey(state))
            this.state = state;
        else this.state = "Neutral";
        super.changeState(false);
        this.dimension = dimension;
        super.setConsumo(consumo);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static HashMap<String, Float> getFactors() {
        return factors;
    }

    @Override
    public float getDailyComsumption() {
        return super.getConsumo()*factors.get(state);
    }

    @Override
    public String toString() {
        return "SmartLamp{" +
                "id=" + super.getManufacturerId() + '\'' +
                ", turned='" + super.isStatus() + '\'' +
                ", state='" + state + '\'' +
                ", dimension=" + dimension +
                '}';
    }
}

package Model;

import java.util.HashMap;

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
        return dimension + factors.get(state);
    }
}

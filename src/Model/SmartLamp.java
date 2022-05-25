package Model;

import Controller.Auxiliary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class SmartLamp extends SmartDevices implements OrderManager , Serializable {

    private String state = "Neutral";

    private static final HashMap<String,Float> factors = new HashMap<>();
    static {
        factors.put("Cold",0.5f);
        factors.put("Neutral",1f);
        factors.put("Warm",1.5f);
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

    public SmartLamp(String[] data) {
        super.price = 5f;
        super.manufacturerId = id;
        id++;
        if (factors.containsKey(data[0]))
            this.state = data[0];
        else this.state = "Neutral";
        this.dimension = Float.parseFloat(data[1]);
        super.setConsumo(Float.parseFloat(data[2]));
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
        if (factors.containsKey(state))
            this.state = state;
        else this.state = "Neutral";
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

    @Override
    public void execute(Orders order) throws Exception {
        switch (order.getDetails()[3]) {
            case "Signal" -> super.changeState(Boolean.getBoolean(order.getDetails()[4]));
            case "State" -> setState(order.getDetails()[4]);
            default -> throw new Exception("No such option.");
        }
        System.out.println(this);
        System.out.println(order.getDayOfUse() + ":Order SmartLamp " + manufacturerId + " " + order.getDetails()[3] + " " + order.getDetails()[4] + " successful.");
    }
}

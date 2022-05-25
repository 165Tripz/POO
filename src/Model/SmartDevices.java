package Model;

import java.io.Serializable;
import java.util.Random;
import java.util.Stack;

public abstract class SmartDevices implements OrderManager , Serializable {
    private final Stack<Float> history = new Stack<>();
    protected static int id = 0;
    protected int manufacturerId;
    private boolean status = new Random().nextBoolean();
    private float consumo;
    float price;

    public boolean isStatus() {
        return status;
    }

    public void changeState(boolean command) {
        status = command;
    }

    public abstract float getDailyComsumption();

    public float getConsumo() {
        return consumo;
    }

    public void setConsumo(float consumo) {
        this.consumo = consumo;
    }

    public Stack<Float> getHistory() {
        return history;
    }

    public float getPrice() {
        return price;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void saveComsumption() {
        history.add(getDailyComsumption());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
package Model;

import java.util.Stack;

public abstract class SmartDevices {
    private final Stack<Float> history = new Stack<>();
    protected static int id = 0;
    protected int manufacturerId;
    private boolean status = false;
    float price;

    public boolean isStatus() {
        return status;
    }

    public void changeState(boolean command) {
        status = command;
    }

    public abstract float getDailyComsumption();

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

}
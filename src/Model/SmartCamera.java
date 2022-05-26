package Model;

import Controller.EqTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SmartCamera extends SmartDevices implements OrderManager , Serializable {

    private String resolution;
    private int sizeOfFile;

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    public void setSizeOfFile(int sizeOfFile) {
        this.sizeOfFile = sizeOfFile;
    }

    public int getSizeOfFile() {
        return sizeOfFile;
    }

    public SmartCamera(String resolution) {
        this.sizeOfFile = 20;
        this.resolution = resolution;
        super.price = 50f;
        super.manufacturerId = id;
        super.setConsumo(new Random().nextFloat(1,10));
        id++;
        super.changeState(false);
    }

    public SmartCamera(String resolution, int sizeOfFile, float consumo) {
        this.sizeOfFile = sizeOfFile;
        this.resolution = resolution;
        super.price = 50f;
        super.manufacturerId = id;
        super.setConsumo(consumo);
        id++;
        super.changeState(false);
    }

    public SmartCamera(String[] data) {
        this.sizeOfFile = Integer.parseInt(data[1]);
        this.resolution = data[0];
        super.price = 50f;
        super.manufacturerId = id;
        super.setConsumo(Float.parseFloat(data[2]));
        id++;
    }

    @Override
    public float getDailyComsumption() {
        String res = resolution.replaceAll("x","*");
        return sizeOfFile * new EqTree(res).result()*getConsumo()*0.00000001f;
    }

    @Override
    public void execute(Orders order) throws Exception {
        switch (order.getDetails()[3]) {
            case "Signal" -> super.changeState(Boolean.getBoolean(order.getDetails()[4]));
            case "Size" -> setSizeOfFile(Integer.parseInt(order.getDetails()[4]));
            case "Resolution" -> setResolution(order.getDetails()[4]);
            default -> throw new Exception("No such option.");
        }
        System.out.println(order.getDayOfUse() + ":Order SmartLamp " + manufacturerId + " " + order.getDetails()[3] + " " + order.getDetails()[4] + " successful.");
    }
}

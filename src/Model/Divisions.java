package Model;

import Controller.Auxiliary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Divisions implements OrderManager, Serializable {

    private String name;

    private final HashMap<Integer,SmartDevices> devices = new HashMap<>();

    public HashMap<Integer, SmartDevices> getDevices() {
        return devices;
    }

    public void addDevices(ArrayList<SmartDevices> devices) {
        for (SmartDevices t: devices) {
            this.devices.put(t.getManufacturerId(), t);
        }
    }

    public Divisions(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendGeneralSignal(boolean command) {
        devices.values().forEach(e -> e.changeState(command));
    }

    @Override
    public String toString() {
        return "Divisions{" +
                "devices=" + devices +
                '}';
    }

    @Override
    public void execute(Orders order) throws Exception {
        switch(order.getDetails()[2]) {
            case "Signal" -> sendGeneralSignal(Boolean.getBoolean(order.getDetails()[3]));
            case "Buy" -> {
                switch (order.getDetails()[3]) {
                    case "Camera" -> {
                        var e = new SmartCamera((String[]) Auxiliary.trim(order.getDetails(), 4, 20));
                        devices.put(e.getManufacturerId(), e);
                    }
                    case "Radio" -> {
                        var e = new SmartRadio((String[]) Auxiliary.trim(order.getDetails(), 4, 20));
                        devices.put(e.getManufacturerId(), e);
                    }
                    case "Lamp" -> {
                        var e = new SmartLamp((String[]) Auxiliary.trim(order.getDetails(), 4, 20));
                        devices.put(e.getManufacturerId(), e);
                    }
                    default -> throw new Exception("No such Device.");
                }
            }
            case "Sell" -> devices.remove(Integer.parseInt(order.getDetails()[4]));
        }
        System.out.println(order.getDayOfUse() + ":Order Division " + name + " " + order.getDetails()[2] + " " + order.getDetails()[3] + " successful.");
    }

    public Float getConsumo() {
        float x = 0;
        for (SmartDevices r: devices.values()) {
            if (r.isStatus())
                x += r.getDailyComsumption();
        }
        return x;
    }
}
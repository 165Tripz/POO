package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Divisions {

    private final HashMap<Integer,SmartDevices> devices = new HashMap<>();

    public HashMap<Integer, SmartDevices> getDevices() {
        return devices;
    }

    public void addDevices(ArrayList<SmartDevices> devices) {
        for (SmartDevices t: devices) {
            this.devices.put(t.getManufacturerId(), t);
        }
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
}

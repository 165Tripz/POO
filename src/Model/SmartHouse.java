package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SmartHouse {

    private float valueSpent = 0;
    private final ArrayList<Float> values = new ArrayList<>();
    private final String nif;
    private final String owner;
    private final int divisions;
    private EletricalCompany company;
    private HashMap<String,Divisions> houseMap = new HashMap<String,Divisions>();

    private final HashMap<Integer,SmartDevices> storage = new HashMap<>();

    public SmartHouse(String nif, String owner,int divisions,EletricalCompany company) {
        this.divisions = divisions;
        this.nif = nif;
        this.owner = owner;
        this.company = company;
        for (int i = 0; i < divisions; i++) {
            houseMap.put(""+i,new Divisions());
        }
    }

    public void addStorage(SmartDevices dev) {
        valueSpent += dev.getPrice();
        storage.put(dev.getManufacturerId(), dev);
    }

    public void sendDevices(String divisions, int[] dev) {
        ArrayList<SmartDevices> aux  = new ArrayList<>();
        for (int j : dev) {
            aux.add(storage.remove(j));
        }
        houseMap.get(divisions).addDevices(aux);
    }

    public void renameDivisions(String old,String n) {
        houseMap.put(n,houseMap.remove(old));
    }

    public int getDivisions() {
        return divisions;
    }

    public String nextDay() {
        float res = 0;
        for (Divisions j: houseMap.values()) {
            for (SmartDevices i : j.getDevices().values()) {
                res += i.getDailyComsumption();
            }
        }
        res = company.calculateAmount(divisions,res);
        values.add(res);
        valueSpent += res;
        return "Result: " + res;
    }

    public HashMap<String, Divisions> getHouseMap() {
        return houseMap;
    }

    public void setHouseMap(HashMap<String, Divisions> houseMap) {
        this.houseMap = houseMap;
    }

    public Divisions getDivision(String id) {
        return houseMap.get(id);
    }

    public void setCommand(String id, boolean command) {
        houseMap.get(id).sendGeneralSignal(command);
    }

    public String getNif() {
        return nif;
    }

    public String getOwner() {
        return owner;
    }

    public EletricalCompany getCompany() {
        return company;
    }

    public void setCompany(EletricalCompany company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "SmartHouse{" +
                "valueSpent=" + valueSpent +
                ", values=" + values +
                ", nif='" + nif + '\'' +
                ", owner='" + owner + '\'' +
                ", divisions=" + divisions +
                ", company=" + company +
                ", houseMap=" + houseMap +
                ", storage=" + storage +
                '}';
    }
}

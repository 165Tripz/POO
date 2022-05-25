package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SmartHouse implements Serializable {

    private float valueSpent = 0;
    private final ArrayList<Fatura> values = new ArrayList<>();
    private final String nif;
    private final String owner;
    private int divisions;
    private String company;
    private final HashMap<String,Divisions> houseMap = new HashMap<String,Divisions>();

    public SmartHouse(String[] data) {
        if (data.length == 4)
            this.divisions = Integer.parseInt(data[3]);
        else
            this.divisions = 0;
        this.nif = data[1];
        this.owner = data[0];
        this.company = data[2];
        for (int i = 0; i < divisions; i++) {
            houseMap.put(""+i,new Divisions("" + i));
        }
    }


    public void renameDivisions(String old,String n) {
        houseMap.put(n,houseMap.remove(old));
    }

    public int getDivisions() {
        return divisions;
    }

    public int getNumberDevices() {
        int devices = 0;
        for (Divisions e : houseMap.values()) {
            for (SmartDevices x : e.getDevices().values()) {
                if (x.isStatus())
                    devices++;
            }
        }
        return devices;
    }

    public void addFatura(Fatura x) {
        values.add(x);
    }

    public HashMap<String, Divisions> getHouseMap() {
        return houseMap;
    }

    public Divisions getDivision(String id) {
        return houseMap.get(id);
    }


    public String getNif() {
        return nif;
    }

    public String getOwner() {
        return owner;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setValueSpent(float valueSpent) {
        this.valueSpent = valueSpent;
    }

    public float getValueSpent() {
        return valueSpent;
    }

    public ArrayList<Fatura> getValues() {
        return new ArrayList<>(values);
    }

    public void addDivision(Divisions x) {
        this.divisions++;
        houseMap.put(x.getName(),x);
    }

    @Override
    public String toString() {
        return "\nSmartHouse{" +
                "valueSpent=" + valueSpent +
                ", values=" + values +
                ", nif='" + nif + '\'' +
                ", owner='" + owner + '\'' +
                ", divisions=" + divisions +
                ", company=" + company +
                ", houseMap=" + houseMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartHouse that = (SmartHouse) o;
        return Objects.equals(nif, that.nif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif);
    }

}

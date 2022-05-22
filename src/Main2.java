import Model.*;

import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) {
        EletricalCompany e = new EletricalCompany("Gertrude");

        System.out.println(e);

        System.out.println(e.calculateAmount(11,33));

        SmartHouse x = new SmartHouse("365597405","Mendigo",6,e);


        x.renameDivisions("0","Sala de Jantar");
        SmartLamp e1 = new SmartLamp("Warm",11,4.57f);
        SmartLamp e3 = new SmartLamp("Neutral",12,4.73f);
        ArrayList<SmartDevices> devices = new ArrayList<>();
        devices.add(e1);devices.add(e3);
        Divisions e2 = x.getDivision("Sala de Jantar");
        e2.addDevices(devices);


        System.out.println(x);

    }

}

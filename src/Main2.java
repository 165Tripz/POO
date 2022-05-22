import Model.EletricalCompany;
import Model.SmartHouse;

public class Main2 {
    public static void main(String[] args) {
        EletricalCompany e = new EletricalCompany("Gertrude");

        System.out.println(e);

        System.out.println(e.calculateAmount(11,33));

        SmartHouse x = new SmartHouse("365597405","Mendigo",6,e);

        System.out.println(x);

    }

}

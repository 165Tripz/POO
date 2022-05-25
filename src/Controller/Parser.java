package Controller;

import Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class Parser implements Serializable {
    private Controller x;
    private final HashMap<String,EletricalCompany> companies = new HashMap<>();
    private final ArrayList<SmartHouse> houses = new ArrayList<>();

    public void parse(String filename) {

        boolean isText = false;

        File file = new File(filename);
        if (filename.matches(".*\\.txt")) isText = true;

        try {
            if (isText) {
                Scanner scanner = new Scanner(file);
                boolean flag = true;
                String line = "";
                while (scanner.hasNextLine()) {
                    if (flag) line = scanner.nextLine();
                    else flag = true;

                    String[] s;
                    s = line.split(":");

                    switch (s[0]) {
                        case "Fornecedor" -> {companies.put(s[1], new EletricalCompany(s[1]));}
                        case "Casa" -> {
                            SmartHouse house = new SmartHouse(s[1].split(","));
                            while (scanner.hasNextLine()) {
                                if (flag) line = scanner.nextLine();
                                else flag = true;
                                if (line.contains("Divisao")) {
                                    Divisions n = new Divisions(line.split(":")[1]);
                                    ArrayList<SmartDevices> e = new ArrayList<>();
                                    while (scanner.hasNextLine()) {
                                        line = scanner.nextLine();
                                        var r = line.split(":");
                                        switch (r[0]) {
                                            case "SmartBulb" -> e.add(new SmartLamp(r[1].split(",")));
                                            case "SmartCamera" -> e.add(new SmartCamera(r[1].split(",")));
                                            case "SmartSpeaker" -> e.add(new SmartRadio(r[1].split(",")));
                                            default -> {
                                                n.addDevices(e);
                                                flag = false;
                                                house.addDivision(n);
                                            }
                                        }
                                        if (!flag) break;
                                    }
                                } else {
                                    houses.add(house);
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream obj = new ObjectInputStream(fis);

                x = (Controller) obj.readObject();

                obj.close();
                fis.close();

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Controller x){
        String filename = "file.ser";
        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(x);

            out.close();
            file.close();
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public ArrayList<SmartHouse> getHouses() {
        return houses;
    }

    public HashMap<String, EletricalCompany> getCompanies() {
        return companies;
    }

    public Controller getController() {
        return x;
    }
}

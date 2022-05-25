package Controller;

import Model.*;
import View.View;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Serializable {
    private static final long serialversionUID = 129348938L;
    private final View view;
    private LocalDate time;
    private HashMap<String,EletricalCompany> companies = new HashMap<>();
    private HashSet<SmartHouse> houses = new HashSet<>();

    private HashMap<LocalDate,ArrayList<Orders>> orders = new HashMap<>();

    public Controller(Controller c) {
        this.houses = c.houses;
        this.companies = c.companies;
        this.orders = c.orders;
        this.time = c.time;
        this.view = c.view;
        this.view.reconnect();
        run();
    }

    public Controller() {

        time = LocalDate.of(2022,4,17);
        this.view = new View();

        String option = view.initialize();

        if ("load".equals(option)) {
            option = view.load(false);
            File file = new File(option);
            while (!file.exists()) {
                file = new File(option = view.load(true));
            }
            Parser x = new Parser();
            x.parse(option);
            if (option.matches(".*\\.txt")) {
                this.companies.putAll(x.getCompanies());
                this.houses.addAll(x.getHouses());
            }
            else new Controller(x.getController());
        }

        run();
    }


    public void run () {
        boolean error = false;
        String check;
        while (!(check = view.menu(error,time)).equals("10")) {
            error = false;
            switch (check) {
                case "1" -> createHouse();
                case "2" -> createCompany();
                case "3" -> manageHouses();
                case "4" -> createHouse();
                case "5" -> createHouse();
                case "6" -> createHouse();
                case "7" -> createHouse();
                case "8" -> createHouse();
                case "9" -> new Parser().save(this);
                default -> error = true;
            }
        }
    }

    public void createHouse() {
        boolean[] errors = new boolean[4];
        String[] data = new String[4];
        boolean check = false;
        try {
            if (companies.keySet().size() == 0) throw new Exception("Não existe companhias.");
            do {
                data = view.criarCasas(companies.keySet(), data,check, errors);
                errors[0] = data[0].matches("\\d+");
                errors[1] = !Auxiliary.PT(data[1]);
                errors[2] = !companies.containsKey(data[2]);
                errors[3] = data[3].matches("\\D+");
                check = (errors[0] || errors[1] || errors[2] || errors[3]);
            } while (check);
            SmartHouse x = new SmartHouse(data);
            if (houses.contains(x)) throw new Exception("NIF já utilizado.");
            houses.add(x);
            view.sucess(true);
        } catch (Exception e) {
            view.errorHandler(e);
        }
    }

    public void createCompany() {
        boolean[] errors = new boolean[4];
        String[] data = new String[4];
        boolean check = false;
        float val = 0f;
        float dis = 0f;
        do {
            data = view.criarCompanhias(data,check, errors);
            errors[0] = companies.containsKey(data[0]);
            try {
                val = Float.parseFloat(data[1]);
                errors[1] = false;
            } catch (Exception e) {errors[1] = true;}
            try {
                dis = Float.parseFloat(data[2]);
                if (dis > 1 || dis < 0.5f) throw new Exception("Illegal Interval");
                errors[2] = false;
            } catch (Exception e) {errors[2] = true;}
            if (data[3] == null) data[3] = "num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375";
            else try {
                if (!data[3].contains("tax") || !data[3].contains("val") || !data[3].contains("com")) throw new Exception("");
                new EqTree(data[3].replaceAll("\\s*num\\s*(>=|<=|!=)\\s*\\d+\\s*\\?","").replaceAll("tax","1").replaceAll("val","1").replaceAll("com","1")).result();
                errors[3] = false;
            } catch (Exception e) {
                errors[3] = true;
            }
            check = (errors[0] || errors[1] || errors[2] || errors[3]);
        } while (check);
        EletricalCompany x = new EletricalCompany(data[0],val,dis,data[3]);
        companies.put(data[0],x);
        view.sucess(true);
    }

    public void manageHouses() {
        ArrayList<String> data1 = new ArrayList<>();
        for (SmartHouse house: houses) data1.add(house.getOwner() + " (" + house.getNif() + ")");
        String[] data = data1.toArray(new String[0]);
        String x = view.selectHouse(data,false);
        boolean check;
        while (x.matches("\\D+") || (check = Integer.parseInt(x) >= houses.size()) && !houses.contains(new SmartHouse(new String[]{"",x,""}))) {x = view.selectHouse(data,true);}

        try {
            Optional<SmartHouse> receptacle;

            if (check) {
                String finalX = x;
                receptacle = houses.stream().filter(e -> e.getNif().equals(finalX)).findFirst();
            } else receptacle = Optional.ofNullable(houses.stream().toList().get(Integer.parseInt(x)));

            SmartHouse res;
            if (receptacle.isPresent()) {
                res = receptacle.get();
            } else throw new Exception("House doesnt exist!");

            houseManager(res);

        } catch (Exception e) {
            view.errorHandler(e);
        }
    }

    private void houseManager(SmartHouse res) {
        String[] send = new String[3];
        send[0] = "Dono da casa: " + res.getOwner() + '\n';
        send[1] = "NIF: " + res.getNif() + '\n';
        send[2] = "Companhia ELetrica: " + res.getCompany() + '\n';
        view.display(send);

        String x;
        do {
            x = view.houseMenu(false);
            while (x.matches("\\D+") || !Auxiliary.between(1,x,6)) x = view.houseMenu(true);
            switch (x) {
                case "1" -> divisionManager(res.getHouseMap());
                case "2" -> renameDivisions(res.getHouseMap());
                case "3" -> verifyFaturas(res.getValues());
                case "4" -> valuesByTime(res.getValues());
                case "5" -> eletricalSwitch(res.getCompany());
            }
        } while (!x.equals("6"));


    }

    private void eletricalSwitch(String company) {
        String[] send = companies.keySet().toArray(new String[0]);
        System.out.println(Arrays.toString(send));
    }

    private void valuesByTime(ArrayList<Fatura> values) {
    }

    private void verifyFaturas(ArrayList<Fatura> values) {

    }

    private void renameDivisions(HashMap<String, Divisions> houseMap) {
    }

    private void divisionManager(HashMap<String, Divisions> houseMap) {

    }

    public void nextDay() {
        for (SmartHouse e : houses) {
            float res = 0;
            for (Divisions j : e.getHouseMap().values()) {
                for (SmartDevices i : j.getDevices().values()) {
                    if (i.isStatus()) {
                        res += i.getDailyComsumption();
                    }
                }
            }
            Fatura r = companies.get(e.getCompany()).calculateAmount(e.getNif(),e.getNumberDevices(), res, time);
            e.addFatura(r);
            e.setValueSpent(e.getValueSpent() + r.getValor());
        }
        time = time.plusDays(1);
    }

    public SmartHouse casaComMaisGastos(LocalDate inicio, LocalDate fim)  {
        return  ranking(inicio,fim).get(0);
    }

    public EletricalCompany maiorFaturacao() {
        return companies.values().stream().sorted((v1, v2) -> (int) (v2.getFaturasEmitidas().stream().mapToDouble(Fatura::getValor).sum() - v1.getFaturasEmitidas().stream().mapToDouble(Fatura::getValor).sum())).toList().get(0);
    }

    public ArrayList<Fatura> faturasEmitidasCompany(String companhia) {
        return companies.get(companhia).getFaturasEmitidas();
    }

    public List<SmartHouse> ranking(LocalDate inicio, LocalDate fim) {
        return houses.stream().sorted((v1, v2) -> (int) (v2.getValues().stream().filter(f -> f.getDia().isAfter(inicio) && f.getDia().isBefore(fim) || f.getDia().equals(inicio) || f.getDia().equals(fim)).mapToDouble(Fatura::getValor).sum() - v1.getValues().stream().filter(f -> f.getDia().isAfter(inicio) && f.getDia().isBefore(fim) || f.getDia().equals(inicio) || f.getDia().equals(fim)).mapToDouble(Fatura::getValor).sum())).toList();
    }

    public void addOrdem(Orders ordem) {
        if (!orders.containsKey(ordem.getDayOfUse()))
            orders.put(ordem.getDayOfUse(),new ArrayList<>());
        orders.get(ordem.getDayOfUse()).add(ordem);
    }

    public void executeOrders(LocalDate dia) {
        var s = orders.remove(dia);
        for (Orders x : s) {
            try {
                switch (x.getTarget()) {
                    case Company -> companies.get(x.getDetails()[0]).execute(x);
                    case Division -> houses.stream().filter(e -> e.getNif().equals(x.getDetails()[0])).toList().get(0).getDivision(x.getDetails()[1]).execute(x);
                    case SmartDevice -> houses.stream().filter(e -> e.getNif().equals(x.getDetails()[0])).toList().get(0).getDivision(x.getDetails()[1]).getDevices().get(Integer.parseInt(x.getDetails()[2])).execute(x);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void determinadaData(LocalDate date) {
        if (date.isAfter(time)) {
            while (!time.equals(date)) {
                nextDay();
                if (orders.containsKey(time))
                    executeOrders(time);
            }
        }

    }

    @Override
    public String toString() {
        return "Controller{" +
                "t\nime=" + time +
                ", \ncompanies=" + companies +
                ", \nhouses=" + houses +
                '}';
    }
}

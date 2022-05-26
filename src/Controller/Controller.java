package Controller;

import Model.*;
import View.View;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static Model.Orders.OrderType.Company;
import static Model.Orders.OrderType.Division;

public class Controller implements Serializable {
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

        String option;

        try {
            option = view.initialize();
            if ("load".equals(option)) {
                option = view.load(false);
                File file = new File(option);
                while (!file.exists()) {
                    file = new File(option = view.load(true));
                }
                Parser x = new Parser();
                try {
                    x.parse(option);
                }catch (Exception e) {
                    System.out.println("File not valid.");
                }
                if (option.matches(".*\\.txt")) {
                    this.companies.putAll(x.getCompanies());
                    this.houses.addAll(x.getHouses());
                }
                else new Controller(x.getController());
            }
        } catch(Exception e) {return;}


        run();
    }


    public void run () {
        boolean error = false;
        String check;
        while (!(check = view.menu(error,time.toString())).equals("11")) {
            error = false;
            switch (check) {
                case "1" -> createHouse();
                case "2" -> createCompany();
                case "3" -> manageHouses();
                case "4" -> manageCompanies();
                case "5" -> determinadaData();
                case "6" -> casaComMaisGastos();
                case "7" -> maiorFaturacao();
                case "8" -> faturasEmitidasCompany();
                case "9" -> ranking();
                case "10" -> save();
                default -> error = true;
            }
        }
    }

    private void manageCompanies() {
        view.pagedView(companies.keySet().toArray(new String[0]), 20);
        String data;
        boolean check = false;
        do {
            try {
                data = view.getNumber(check);
            } catch (Exception e) {return;}
            check = !companies.containsKey(data);
        } while (check);
        EletricalCompany r = companies.get(data);
        while (!(data = view.compMenu(check)).equals("5")) {
            view.display(new String[]{"Nome: " + r.getNome(), "Equacao: " + r.getEquation()});
            check = false;
            if ("1".equals(data)) {
                changeEquacao(r);
                //case "2" -> changeDiscount(r);
                //case "3" -> changeValue(r);
                //case "4" -> casasAssociadas(r);
            } else {
                check = true;
            }
        }
    }

    private void changeEquacao(EletricalCompany r) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        try {
            do {
                data = view.getEquacao(error);
                if (data == null) data = "num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375";
                else {
                    if (!data.contains("tax") || !data.contains("val") || !data.contains("com")) {
                        error = true;
                    }
                    else
                        error = !new EqTree(data.replaceAll("\\s*num\\s*(<=|>=|<|>|==|!=)\\s*\\d+\\s*\\?", "").replaceAll("tax", "1").replaceAll("val", "1").replaceAll("com", "1")).check();
                }
            } while (error);
        } catch (Exception ignored) {}

        if (!orders.containsKey(dia)) orders.put(dia,new ArrayList<>());
        orders.get(dia).add(new Orders(dia,Company,new String[]{r.getNome(),"Equation",data}));

    }

    public void save () {
        String filename;
        filename = view.save(true);
        while (filename.matches(".*\\.txt")) filename = view.save(false);
        try {
            new Parser().save(this,filename);
            view.display(new String[]{"Projeto gravado com sucesso"});
        } catch (Exception e) {
            view.errorHandler(e);
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
            try {
                data = view.criarCompanhias(data, check, errors);
            } catch(Exception e) {return;}
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
            else {
                if (!data[3].contains("tax") || !data[3].contains("val") || !data[3].contains("com")) errors[3] = true;
                else
                    errors[3] = !new EqTree(data[3].replaceAll("\\s*num\\s*(<=|>=|<|>|==|!=)\\s*\\d+\\s*\\?", "").replaceAll("tax", "1").replaceAll("val", "1").replaceAll("com", "1")).check();
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
        String x;
        boolean check;
        try {
            x = view.selectHouse(data, false);
            while (x.matches("\\D+") || (check = Integer.parseInt(x) >= houses.size()) && !houses.contains(new SmartHouse(new String[]{"", x, ""})))
                x = view.selectHouse(data, true);
        }catch(Exception e) {return;}
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
        String x;
        do {
            String[] send = new String[3];
            send[0] = "Dono da casa: " + res.getOwner();
            send[1] = "NIF: " + res.getNif();
            send[2] = "Companhia ELetrica: " + res.getCompany();
            view.display(send);
            x = view.houseMenu(false);
            while (x.matches("\\D+") || !Auxiliary.between(1,x,6)) x = view.houseMenu(true);
            switch (x) {
                case "1" -> divisionManager(res.getHouseMap(),res);
                case "2" -> renameDivisions(res.getHouseMap());
                case "3" -> verifyFaturas(res.getValues());
                case "4" -> verificarConsumo(res);
                case "5" -> eletricalSwitch(res);
                default -> View.clearScreen();
            }
        } while (!x.equals("6"));


    }

    private void verificarConsumo(SmartHouse res) {
        view.display(new String[]{"Consumo da Casa: " + res.getHouseMap().values().stream().mapToDouble(e -> e.getDevices().values().stream().map(SmartDevices::getDailyComsumption).mapToDouble(Float::doubleValue).sum()).sum() + " kW por dia"});
    }

    private void eletricalSwitch(SmartHouse r) {
        String[] send;
        String[] send2;
        var comp = companies.values().stream().map(EletricalCompany::getNome).toList();
        var compa = comp.toArray(new String[0]);
        String x;
        EletricalCompany res;
        try {
            do {
                x = view.companies( false,compa);
                while (!companies.containsKey(x))
                    x = view.companies(true, compa);
                res = companies.get(x);
                send2 = new String[]{"Companhia: " + companies.get(r.getCompany()).getNome(), "Equação de consumo: " + companies.get(r.getCompany()).getEquation()};
                send = new String[]{"Companhia: " + res.getNome(), "Equação de consumo: " + res.getEquation()};
            } while (!(view.companyDisplay(send2, send)).toLowerCase(Locale.ROOT).equals("y"));
        }catch(Exception e) {return;}
        r.setCompany(res.getNome());
    }

    private void verifyFaturas(ArrayList<Fatura> values) {
        ArrayList<String> strings = new ArrayList<>();
        for (Fatura r : values)
            strings.add("Dia: " + r.getDia().toString() + "\nCompanhia: " + r.getCompanhia() + "\nNIF: " + r.getNif() + "\nValor: "+ r.getValor());

        view.pagedView(strings.toArray(new String[0]),5);
    }

    private void renameDivisions(HashMap<String, Divisions> houseMap) {
        var s = houseMap.keySet();
        ArrayList<String> strings = new ArrayList<>(s);
        view.pagedView(strings.toArray(new String[0]),20);
        String data;
        boolean check = false;
        do {
            try {
                data = view.getNumber(check);
            } catch (Exception e) {return;}
            check = !houseMap.containsKey(data);
        } while (check);
        String nome;
        try {nome = view.getNome();} catch(Exception e) {return;}
        Divisions temp;
        (temp = houseMap.remove(data)).setName(nome);
        houseMap.put(nome, temp);

        String finalData = data;
        orders.values().stream().map(e -> e.stream().filter(i -> !i.getTarget().equals(Company)).map(i -> i.getDetails()[1] = finalData));
    }

    private void divisionManager(HashMap<String, Divisions> houseMap,SmartHouse ee) {
        view.pagedView(houseMap.keySet().toArray(new String[0]), 20);
        String data;
        boolean check = false;
        do {
            try {
                data = view.getNumber(check);
            } catch (Exception e) {return;}
            check = !houseMap.containsKey(data);
        } while (check);
        Divisions r = houseMap.get(data);
        while (!(data = view.divMenu(check)).equals("5")) {
            view.display(new String[]{"Nome: " + r.getName() , "Consumo: " + r.getConsumo()});
            check = false;
            switch (data) {
                case "1" -> sendGeneralSignal(r,ee);
                case "2" -> acess(r,ee.getNif());
                case "3" -> buy(r,ee.getNif());
                default -> check = true;
            }
        }
    }

    private void acess(Divisions r, String nif) {
        boolean error = false;
        String data;
        do {
            try {
                data = view.getCreateSmartDevice(error);
                error = !r.getDevices().containsKey(Integer.parseInt(data));
                SmartDevices x = r.getDevices().get(Integer.parseInt(data));
                switch (x.getClass().getName()) {
                    case "SmartCamera" -> acessCamera(x,r,nif);
                    case "SmartLamp" -> acessLamp(x,r,nif);
                    case "SmartRadio" -> acessRadio(x,r,nif);
                    default -> error = true;
                }
            } catch (Exception e) {error = true;}
        } while (error);
    }

    private void acessRadio(SmartDevices x, Divisions r, String nif) {
        SmartRadio camera = (SmartRadio) x;

        String data;
        boolean check = false;
        while (!(data = view.radioMenu(check)).equals("5")) {
            view.display(new String[]{"Dispositivo " + camera.getManufacturerId() , "Consumo: " + camera.getDailyComsumption() , "Ligado: " + camera.isStatus() , "Canal: " + camera.getRadio() , "Volume: " + camera.getVolume()});
            check = false;
            switch (data) {
                case "1" -> sendSignal("Radio",camera.getManufacturerId(),r.getName(),nif);
                case "2" -> changeChannel(camera.getManufacturerId(),r.getName(),nif);
                case "3" -> changeVolume(camera.getManufacturerId(),r.getName(),nif);
                default -> check = true;
            }
        }
    }

    private void changeVolume(int manufacturerId, String name, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            try {
                error = !Auxiliary.between(0,Integer.parseInt(data = view.volume(error)),100);
            } catch (Exception e) {error = true;}
        }while (error);
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,"Radio","Volume",data}));
    }

    private void changeChannel(int manufacturerId, String name, String nif) {
        boolean error = false;
        String data;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        data = view.channel();
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,"Radio","Channel",data}));
    }

    private void acessLamp(SmartDevices x, Divisions r, String nif) {
        SmartLamp camera = (SmartLamp) x;

        String data;
        boolean check = false;
        while (!(data = view.lampMenu(check)).equals("5")) {
            view.display(new String[]{"Dispositivo " + camera.getManufacturerId() , "Consumo: " + camera.getDailyComsumption() , "Ligado: " + camera.isStatus() , "Estado: " + camera.getState()});
            check = false;
            switch (data) {
                case "1" -> sendSignal("Lamp",camera.getManufacturerId(),r.getName(),nif);
                case "2" -> changeState(camera.getManufacturerId(),r.getName(),nif);
                default -> check = true;
            }
        }
    }

    private void sendSignal(String type,int manufacturerId, String name, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            try {
                Boolean.getBoolean(data = view.estado(error));
            } catch (Exception e) {error = true;}
        } while (error);
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,type,"Signal",data}));
    }

    private void changeState(int manufacturerId, String name, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            try {
                error = !(data = view.estado(error)).matches("(Warm|Neutral|Cold)");
            } catch (Exception e) {error = true;}
        } while (error);
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,"Lamp","State",data}));
    }

    private void acessCamera(SmartDevices x, Divisions r, String nif) {
        SmartCamera camera = (SmartCamera) x;

        String data;
        boolean check = false;
        while (!(data = view.cameraMenu(check)).equals("5")) {
            view.display(new String[]{"Dispositivo " + camera.getManufacturerId() , "Consumo: " + camera.getDailyComsumption() , "Ligado: " + camera.isStatus() , "Tamanho do file: " + camera.getSizeOfFile() , "Resolução: " + camera.getResolution()});
            check = false;
            switch (data) {
                case "1" -> sendSignal("Camera",camera.getManufacturerId(),r.getName(),nif);
                case "2" -> changeSize(camera.getManufacturerId(),r.getName(),nif);
                case "3" -> changeResolution(camera.getManufacturerId(),r.getName(),nif);
                default -> check = true;
            }
        }

    }

    private void changeResolution(int manufacturerId, String name, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            try {
                error = !(data = view.resolution(error)).matches("\\(\\d+x\\d+\\)");
            } catch (Exception e) {error = true;}
        } while (error);
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,"Camera","Resolution",data}));
    }

    private void changeSize(int manufacturerId, String name, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            try {
                error = Integer.parseInt(data = view.size(error)) <= 0;
            } catch (Exception e) {error = true;}
        } while (error);
        orders.get(dia).add(new Orders(dia,Division,new String[]{nif,name,"" + manufacturerId,"Camera","Size",data}));
    }

    private void buy(Divisions r, String nif) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            data = view.getCreateSmartDevice(error);
            switch (data.toLowerCase(Locale.ROOT)) {
                case "camera" -> createCamera(dia,r.getName(),nif);
                case "lamp" -> createLamp(dia,r.getName(),nif);
                case "radio" -> createRadio(dia,r.getName(),nif);
                default -> error = true;
            }
        } while (error);
    }

    public void createCamera(LocalDate dia,String divisao, String casa) {
        boolean[] errors = new boolean[2];
        String[] data = new String[2];
        boolean check = false;
        int size = 0;
        do {
            try {
                data = view.criarCameras(data, check, errors);
            } catch(Exception e) {return;}
            try {
                size = Integer.parseInt(data[0]);
                errors[1] = size <= 0;
            } catch (Exception e) {errors[0] = true;}
            errors[1] = data[1].matches("\\(\\d+x\\d+\\)");
            check = (errors[0] || errors[1]);
        } while (check);
        orders.get(dia).add(new Orders(dia,Division,new String[]{casa,divisao,"Buy","Camera",data[0],data[1],"" + new Random().nextFloat(0.8f,10.0f)}));
        view.sucess(true);
    }

    public void createLamp(LocalDate dia,String divisao, String casa) {
        String[] data = new String[2];
        boolean check = false;
        int size = 0;
        do {
            try {
                data = view.criarLamp(data, check);
            } catch(Exception e) {return;}
            try {
                size = Integer.parseInt(data[0]);
                check = size <= 0;
            } catch (Exception e) {check = true;}
        } while (check);
        orders.get(dia).add(new Orders(dia,Division,new String[]{casa,divisao,"Buy","Lamp",data[0],data[1],"" + new Random().nextFloat(0.8f,10.0f)}));
        view.sucess(true);
    }

    public void createRadio(LocalDate dia,String divisao, String casa) {
        String[] data = new String[3];
        boolean check = false;
        int size = 0;
        do {
            try {
                data = view.criarRadio(data, check);
            } catch(Exception e) {return;}
            try {
                size = Integer.parseInt(data[0]);
                check = !Auxiliary.between(0,size,100);
            } catch (Exception e) {check = true;}
        } while (check);
        orders.get(dia).add(new Orders(dia,Division,new String[]{casa,divisao,"Buy","Radio",data[0],data[1],data[2],"" + new Random().nextFloat(0.8f,10.0f)}));
        view.sucess(true);
    }

    private void sendGeneralSignal(Divisions r,SmartHouse ee) {
        boolean error = false;
        String data = null;

        LocalDate dia = null;
        do {
            try {
                error = !(dia = LocalDate.parse(view.getDia(error))).isAfter(time);
            } catch (Exception e) {
                if (e.getMessage().equals("Quit")) return;
                error = true;
            }
        } while (error);
        if (!orders.containsKey(dia)) {
            orders.put(dia, new ArrayList<>());
        }
        do {
            data = view.getConfirmation(error);
            if (data.toLowerCase(Locale.ROOT).equals("y")) orders.get(dia).add(new Orders(dia,Division,new String[]{ee.getNif(),r.getName(),"Signal","true"}));
            else if (data.toLowerCase(Locale.ROOT).equals("n")) orders.get(dia).add(new Orders(dia,Division,new String[]{ee.getNif(),r.getName(),"Signal","false"}));
            else error = true;
        } while (error);
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

    public void casaComMaisGastos()  {
        LocalDate inicio = time;
        LocalDate fim = time;
        String[] data = new String[2];
        boolean error = false;
        boolean[] errors = new boolean[2];
        do {
            try {
                data = view.datas(error, errors, data);
            } catch(Exception e) {return;}
            try {
                errors[0] = !data[0].matches("\\d{4}-\\d+-\\d+") || !(inicio = LocalDate.parse(data[0])).isAfter(LocalDate.of(2022, 4, 16));
            } catch (Exception e) {errors[0] = true;}
            try {
                errors[1] = !data[1].matches("\\d{4}-\\d+-\\d+") || !(fim = LocalDate.parse(data[1])).isBefore(time.plusDays(1));
            } catch (Exception e) {errors[1] = true;}
            error = errors[0] || errors[1];
        } while(error);
        LocalDate finalInicio = inicio;
        LocalDate finalFim = fim;
        List<SmartHouse> rrr = houses.stream().sorted((v1, v2) -> (int) (v2.getValues().stream().filter(f -> Auxiliary.betweenDay(finalInicio,f.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum() - v1.getValues().stream().filter(f -> Auxiliary.betweenDay(finalInicio,f.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum())).toList();
        SmartHouse r = rrr.get(0);
        String[] ret = new String[]{"Dono: "+r.getOwner(),"NIF: "+r.getNif(),"Companhia: " + r.getCompany(),"Valores Gastos:" + r.getValues().stream().filter(e -> Auxiliary.betweenDay(finalInicio,e.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum()};
        view.display(ret);
    }

    public void maiorFaturacao() {
        EletricalCompany e = companies.values().stream().sorted((v1, v2) -> (int) (v2.getFaturasEmitidas().stream().mapToDouble(Fatura::getValor).sum() - v1.getFaturasEmitidas().stream().mapToDouble(Fatura::getValor).sum())).toList().get(0);
        view.display(new String[]{"Nome: " + e.getNome(),"Valor faturado: " + e.getFaturasEmitidas().stream().mapToDouble(Fatura::getValor).sum()});
    }

    public void faturasEmitidasCompany() {
        var comp = companies.values().stream().map(EletricalCompany::getNome).toList();
        var compa = comp.toArray(new String[0]);
        String x;
        try {
            x = view.companies( false,compa);
            while (!companies.containsKey(x))
                x = view.companies( true,compa);
        }catch(Exception e) {return;}
        var res = companies.get(x).getFaturasEmitidas();
        ArrayList<String> ss = new ArrayList<>();
        for (Fatura r : res) {
            ss.add("Companhia:" + r.getCompanhia() + "\nUtente: " + r.getNif() + "\nValor: " + r.getValor() + "\nData de emissao: " + r.getDia());
        }
        view.pagedView(ss.toArray(new String[0]),5);
    }

    public void ranking() {
        LocalDate inicio = time;
        LocalDate fim = time;
        String[] data = new String[2];
        boolean error = false;
        boolean[] errors = new boolean[2];
        do {
            try {
                data = view.datas(error, errors, data);
            } catch(Exception e) {return;}
            try {
                errors[0] = !data[0].matches("\\d{4}-\\d+-\\d+") || !(inicio = LocalDate.parse(data[0])).isAfter(LocalDate.of(2022, 4, 16));
            } catch (Exception e) {errors[0] = true;}
            try {
                errors[1] = !data[1].matches("\\d{4}-\\d+-\\d+") || !(fim = LocalDate.parse(data[1])).isBefore(time.plusDays(1));
            } catch (Exception e) {errors[1] = true;}
            error = errors[0] || errors[1];
        } while(error);
        LocalDate finalInicio = inicio;
        LocalDate finalFim = fim;
        List<SmartHouse> rrr = houses.stream().sorted((v1, v2) -> (int) (v2.getValues().stream().filter(f -> Auxiliary.betweenDay(finalInicio,f.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum() - v1.getValues().stream().filter(f -> Auxiliary.betweenDay(finalInicio,f.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum())).toList();
        String[] strings = new String[rrr.size()];
        for(int i = 0; i < rrr.size(); i++) {
            SmartHouse temp = rrr.get(i);
            strings[i] = temp.getOwner() + " (" + temp.getNif() + ") \nValor gasto no periodo (" + finalInicio.toString() + "-" + finalFim + "): "+temp.getValues().stream().filter(f -> Auxiliary.betweenDay(finalInicio,f.getDia(),finalFim)).mapToDouble(Fatura::getValor).sum() ;
        }
        view.pagedView(strings,10);

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

    public void determinadaData() {
        int days = 0;
        boolean error = false;
        do {
            try {
                days = Integer.parseInt(view.avancar(error));
                error = !(days > 0);
            } catch (Exception e) {error = true;}
        } while (error);
        while (days != 0) {
            nextDay();
            days--;
            if (orders.containsKey(time))
                executeOrders(time);
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

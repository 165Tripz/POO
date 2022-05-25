package View;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class View implements Serializable {

    transient Scanner in = new Scanner(System.in);

    public void reconnect() {
        in = new Scanner(System.in);
    }


    public String initialize() {
        System.out.println("Bem vindo ao sistema SmartOrg.");

        System.out.print("Deseja criar um sistema em branco, ou carregar um ficheiro?\n(new\\load):");
        String option = in.nextLine();
        while (!option.equals("new") && !option.equals("load")) {
            System.out.print("Opção não existente.\n(new\\load):");
            option = in.nextLine();
        }

        return option;
    }

    public String load(boolean error) {
        if (error) System.out.println("Ficheiro não existe.");
        System.out.print("Nome do ficheiro:");
        return in.nextLine();
    }

    public String[] criarCasas(Set<String> strings, String[] data, boolean error, boolean[] errorQ) throws Exception {
        if (!error) {
            System.out.print("Nome do dono:");
            data[0] = in.nextLine();
            System.out.print("NIF:");
            data[1] = in.nextLine();
            System.out.println("Companhias:");
            int i = 0;
            for (String s : strings) {
                System.out.println(i++ +". " + s);
            }
            System.out.print("Companhia distribuidora:");
            data[2] = in.nextLine();
            System.out.print("Numero de divisoes:");
            data[3] = in.nextLine();
        } else {
            if (errorQ[0]) {
                System.out.println("Nome não disponivel.");
                System.out.print("Nome do dono:");
                data[0] = in.nextLine();
            }
            if (errorQ[1]) {
                System.out.println("NIF Invalido");
                System.out.print("NIF:");
                data[1] = in.nextLine();
            }
            if (errorQ[2]) {
                System.out.println("Companhia não existe.");
                System.out.println("Companhias:");
                int i = 0;
                for (String s : strings) {
                    System.out.println(i++ +". " + s);
                }
                System.out.print("Companhia distribuidora:");
                data[2] = in.nextLine();
            }
            if (errorQ[3]) {
                System.out.println("Formato numerico errado.");
                System.out.print("Numero de divisoes:");
                data[3] = in.nextLine();
            }
        }
        return data;
    }


    public String menu(boolean error, LocalDate time) {
        if(!error) {
            clearScreen();
            System.out.println("\nMenu (Dia:" + time.toString() + "):");
            String helpMenu = """
                    1. Criar casas;
                    2. Criar companhias;
                    3. Gerir Casas;
                    4. Avançar dias;
                    5. Casa com mais gastos;
                    6. Comercializador com mais faturação;
                    7. Listar faturas(companhias);
                    8. Ranking de consumidores de energia(casas);
                    9. Gravar programa;
                    10. Sair do programa;""";
            System.out.println(helpMenu);
        } else {
            System.out.println("Opção não existente.");
        }
        System.out.print("Opção:");
        return in.nextLine();
    }

    public void sucess(boolean success) {
        if (success) System.out.println("Ação sucedida.");
        else System.out.println("Ação não sucedida.");
    }

    public void errorHandler(Exception e) {
        System.out.println(e.getMessage());
    }

    public String selectHouse(String[] houses, boolean error) {
        if (!error) {
            System.out.println("Lista de casas:");
            pagedView(houses);
        } else System.out.println("Opção não existente;");
        System.out.print("Escolha uma casa (por NIF ou numero da lista):");
        return in.nextLine();
    }

    public String[] criarCompanhias(String[] data, boolean error, boolean[] errorQ) {
        String x;
        if (!error) {
            System.out.print("Nome da empresa:");
            data[0] = in.nextLine();
            do {
                System.out.println("Equação opcional;");
                System.out.println("Equação normal = \"num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375\"");
                System.out.print("(y/n):");
                x = in.nextLine();
            } while (!(x.toLowerCase(Locale.ROOT).equals("y") || x.toLowerCase(Locale.ROOT).equals("n")));
            if (x.equals("y")) {
                System.out.println("Tem de conter as variaveis (\"tax\",\"val\",\"com\").");
                System.out.print("Equaçao:");
                data[3] = in.nextLine();
            } else data[3] = null;
            System.out.print("Valor Fixo:");
            data[1] = in.nextLine();
            System.out.print("Desconto (aplicado depois da equação)[0,5:1.0]:");
            data[2] = in.nextLine();
        } else {
            if (errorQ[0]) {
                System.out.println("Nome de empresa indisponivel.");
                System.out.print("Nome da empresa:");
                data[0] = in.nextLine();
            }
            if (errorQ[3]) {
                do {
                    System.out.println("Equação não possivel;");
                    System.out.println("Equação opcional;");
                    System.out.println("Equação normal = \"num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375\"");
                    System.out.print("(y/n):");
                    x = in.nextLine();
                } while (!(x.toLowerCase(Locale.ROOT).equals("y") || x.toLowerCase(Locale.ROOT).equals("n")));
                if (x.equals("y")) {
                    System.out.println("Tem de conter as variaveis (\"tax\",\"val\",\"com\").");
                    System.out.print("Equaçao:");
                    data[3] = in.nextLine();
                } else data[3] = null;
            }
            if (errorQ[1]) {
                System.out.println("Formato incorreto.");
                System.out.print("Valor Fixo:");
                data[1] = in.nextLine();
            }
            if (errorQ[2]) {
                System.out.println("Formato incorreto.");
                System.out.print("Desconto (aplicado depois da equação):");
                data[2] = in.nextLine();
            }
        }
        return data;
    }

    public void display(String[] res) {
        clearScreen();
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("----------------------------------------------");
        for (String re : res) System.out.print(re);
        System.out.println("----------------------------------------------");
        System.out.println("----------------------------------------------");
    }

    public String houseMenu(boolean error) {
        if (!error) {
            System.out.println("Gestor de casa:");
            System.out.println("""
                    1. Monitorizar divisões;
                    2. Renomear divisões;
                    3. Verificar faturas;
                    4. Verificar valor gasto num periodo de tempo;
                    5. Trocar companhia eletrica;
                    6. Voltar;
                    """);
        } else {
            System.out.println("Opção nao disponivel;");
        }
        System.out.print("Opção:");
        return in.nextLine();
    }

    public void pagedView(String[] strings) {
        int i = 0;
        String r;
        boolean error = false;
        do {
            if (!error) {
                for (int x = i * 20; x < strings.length && x < (i + 1) * 20; x++)
                    System.out.println(x + ". " + strings[x] + ";");
                System.out.println("Pagina " + i + " de " + (strings.length / 20) + ".");
            }
            else System.out.println("Comando errado.");
            System.out.print("Mova com A para a anterior, com D para a seguinte, S para concluir:");
            r = in.nextLine();
            if (r.toLowerCase(Locale.ROOT).equals("d")) {
                error = false;
                i = min(strings.length/20,i+1);
            }
            else if (r.toLowerCase(Locale.ROOT).equals("a")) {
                error = false;
                i = max(0,i-1);
            }
            else error = true;
        } while (!r.toLowerCase(Locale.ROOT).equals("s"));
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}

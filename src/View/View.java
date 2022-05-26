package View;

import java.io.Serializable;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class View implements Serializable {

    transient Scanner in = new Scanner(System.in);

    public void reconnect() {
        in = new Scanner(System.in);
    }

    public String safeQuit() throws Exception {
        String safe = in.nextLine();
        if (safe.toLowerCase(Locale.ROOT).equals("exit")) throw new Exception("Quit");
        return safe;
    }

    public String initialize() throws Exception {
        System.out.println("Bem vindo ao sistema SmartOrg.");

        System.out.print("Deseja criar um sistema em branco, ou carregar um ficheiro?\n(new\\load):");
        String option = safeQuit();
        while (!option.equals("new") && !option.equals("load")) {
            System.out.print("Opção não existente.\n(new\\load):");
            option = safeQuit();
        }

        return option;
    }

    public String load(boolean error) throws Exception {
        if (error) System.out.println("Ficheiro não existe.");
        System.out.print("Nome do ficheiro:");
        return safeQuit();
    }

    public String[] criarCasas(Set<String> strings, String[] data, boolean error, boolean[] errorQ) throws Exception {
        if (!error) {
            System.out.print("Nome do dono:");
            data[0] = safeQuit();
            System.out.print("NIF:");
            data[1] = safeQuit();
            System.out.println("Companhias:");
            int i = 0;
            for (String s : strings) {
                System.out.println(i++ +". " + s);
            }
            System.out.print("Companhia distribuidora:");
            data[2] = safeQuit();
            System.out.print("Numero de divisoes:");
            data[3] = safeQuit();
        } else {
            if (errorQ[0]) {
                System.out.println("Nome não disponivel.");
                System.out.print("Nome do dono:");
                data[0] = safeQuit();
            }
            if (errorQ[1]) {
                System.out.println("NIF Invalido");
                System.out.print("NIF:");
                data[1] = safeQuit();
            }
            if (errorQ[2]) {
                System.out.println("Companhia não existe.");
                System.out.println("Companhias:");
                int i = 0;
                for (String s : strings) {
                    System.out.println(i++ +". " + s);
                }
                System.out.print("Companhia distribuidora:");
                data[2] = safeQuit();
            }
            if (errorQ[3]) {
                System.out.println("Formato numerico errado.");
                System.out.print("Numero de divisoes:");
                data[3] = safeQuit();
            }
        }
        return data;
    }


    public String menu(boolean error, String time) {
        if(!error) {
            clearScreen();
            System.out.println("\nMenu (Dia:" + time + "):");
            String helpMenu = """
                    1. Criar casas;
                    2. Criar companhias;
                    3. Gerir Casas;
                    4. Gerir Companhias;
                    5. Avançar dias;
                    6. Casa com mais gastos;
                    7. Comercializador com mais faturação;
                    8. Listar faturas(companhias);
                    9. Ranking de consumidores de energia(casas);
                    10. Gravar programa;
                    11. Sair do programa;""";
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

    public String selectHouse(String[] houses, boolean error) throws Exception {
        if (!error) {
            System.out.println("Lista de casas:");
            pagedView(houses,20);
        } else System.out.println("Opção não existente;");
        System.out.print("Escolha uma casa (por NIF ou numero da lista):");
        return safeQuit();
    }

    public String[] criarCompanhias(String[] data, boolean error, boolean[] errorQ) throws Exception {
        String x;
        if (!error) {
            System.out.print("Nome da empresa:");
            data[0] = safeQuit();
            do {
                System.out.println("Equação opcional;");
                System.out.println("Equação normal = \"num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375\"");
                System.out.print("(y/n):");
                x = safeQuit();
            } while (!(x.toLowerCase(Locale.ROOT).equals("y") || x.toLowerCase(Locale.ROOT).equals("n")));
            if (x.equals("y")) {
                System.out.println("Tem de conter as variaveis (\"tax\",\"val\",\"com\").");
                System.out.print("Equaçao:");
                data[3] = safeQuit();
            } else data[3] = null;
            System.out.print("Valor Fixo:");
            data[1] = safeQuit();
            System.out.print("Desconto (aplicado depois da equação)[0,5:1.0]:");
            data[2] = safeQuit();
        } else {
            if (errorQ[0]) {
                System.out.println("Nome de empresa indisponivel.");
                System.out.print("Nome da empresa:");
                data[0] = safeQuit();
            }
            if (errorQ[3]) {
                data[3] = getEquacao(true);
            }
            if (errorQ[1]) {
                System.out.println("Formato incorreto.");
                System.out.print("Valor Fixo:");
                data[1] = safeQuit();
            }
            if (errorQ[2]) {
                System.out.println("Formato incorreto.");
                System.out.print("Desconto (aplicado depois da equação):");
                data[2] = safeQuit();
            }
        }
        return data;
    }

    public void display(String[] res) {
        clearScreen();
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("----------------------------------------------");
        for (String re : res) System.out.println(re);
        System.out.println("----------------------------------------------");
        System.out.println("----------------------------------------------");
    }

    public String houseMenu(boolean error){
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

    public void pagedView(String[] strings,int linhas) {
        int i = 0;
        String r;
        boolean error = false;
        do {
            if (!error) {
                for (int x = i * linhas; x < strings.length && x < (i + 1) * linhas; x++)
                    System.out.println(x + ". " + strings[x] + ";");
                System.out.println("Pagina " + i + " de " + (strings.length / linhas) + ".");
            }
            else System.out.println("Comando errado.");
            System.out.print("Mova com A para a anterior, com D para a seguinte, S para concluir:");
            r = in.nextLine();
            if (r.toLowerCase(Locale.ROOT).equals("d")) {
                error = false;
                i = min(strings.length/linhas,i+1);
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

    public String save(boolean error) {
        if (error) System.out.println("Ficheiro inválido;");
        System.out.print("Nome do ficheiro para gravar:");
        return in.nextLine();
    }

    public String[] datas(boolean error,boolean[] errors, String[] data) throws Exception {
        if (error) {
            System.out.println("Formato de data errado;");
            if (errors[0]) {
                System.out.print("Introduza o intervalo inicial (aaaa-mm-dd):");
                data[0] = safeQuit();
            }
            if (errors[1]) {
                System.out.print("Introduza o intervalo final (aaaa-mm-dd):");
                data[1] = safeQuit();
            }
        } else {
            System.out.print("Introduza o intervalo inicial (aaaa-mm-dd):");
            data[0] = safeQuit();
            System.out.print("Introduza o intervalo final (aaaa-mm-dd):");
            data[1] = safeQuit();
        }
        return data;
    }

    public String avancar(boolean error) throws Exception {
        if (error) System.out.println("Input não permitido");
        System.out.print("Numero de dias a avançar:");
        return safeQuit();
    }


    public String companies(boolean error,String[] companies) throws Exception {
        if (!error) {
            System.out.println("Lista de companhias:");
            pagedView(companies,20);
        } else System.out.println("Opção não existente;");
        System.out.print("Escolha uma companhia (Nome):");
        return safeQuit();
    }

    public String companyDisplay(String[] company,String[] company2) {
        System.out.println("Companhia atual:");
        display(company);
        System.out.println("Companhia proposta:");
        display(company2);
        System.out.print("Deseja esta companhia (y/*):");
        return in.nextLine();
    }

    public String getNumber(boolean check) throws Exception {
        if (check) System.out.println("Opção indisponivel;");
        System.out.print("Opção (Nome na lista): ");
        return safeQuit();
    }

    public String getNome() throws Exception {
        System.out.print("Novo nome: ");
        return safeQuit();
    }

    public String compMenu(boolean check) {
        if(!check) {
            clearScreen();
            String helpMenu = """
                    1. Mudar Equação;
                    2. Mudar Disconto;
                    3. Mudar Valor Fixo;
                    4. Casas associadas;""";
            System.out.println(helpMenu);
        } else {
            System.out.println("Opção não existente.");
        }
        System.out.print("Opção:");
        return in.nextLine();
    }

    public String getEquacao(boolean error) {
        String x;
        try {
            if(error)System.out.println("Equação não possivel;");
            do {
                System.out.println("Equação opcional;");
                System.out.println("Equação normal = \"num > 10 ? (val*com* (1 + tax)) * 0.45 : (val * com * (1 + tax)) * 0.375\"");
                System.out.print("(y/n):");
                x = safeQuit();
            } while (!(x.toLowerCase(Locale.ROOT).equals("y") || x.toLowerCase(Locale.ROOT).equals("n")));
            if (x.equals("y")) {
                System.out.println("Tem de conter as variaveis (\"tax\",\"val\",\"com\").");
                System.out.print("Equaçao:");
                return safeQuit();
            } else return null;
        } catch (Exception e) {return null;}
    }

    public String getDia(boolean error) throws Exception {
        if (error) System.out.println("Formato indisponivel;");
        System.out.print("Introduza uma data (xxxx-xx-xx): ");
        return safeQuit();
    }

    public String getConfirmation(boolean error) {
        if (error) System.out.println("Opcao indisponivel:");
        System.out.println("Prima y para ligar todos os dispositivos, n para desligar: ");
        return in.nextLine();
    }

    public String getCreateSmartDevice(boolean error) {
        if (error) System.out.println("Opcao indisponivel:");
        System.out.println("Que tipo de dispositivo (Camera/Radio/Lamp): ");
        return in.nextLine();
    }

    public String[] criarCameras(String[] data, boolean check, boolean[] errors) throws Exception{
        if (!check) {
            System.out.print("Tamanho do ficheiro: ");
            data[0] = safeQuit();
            System.out.print("Resolução: ");
            data[1] = safeQuit();
        } else {
            if (errors[0]) {
                System.out.println("Tamanho do ficheiro invalido;");
                System.out.print("Tamanho do ficheiro: ");
                data[0] = safeQuit();
            }
            if (errors[1]) {
                System.out.println("Resolução Invalida");
                System.out.print("Resolucao \"(NNxNN)\":");
                data[1] = safeQuit();
            }
        }
        return data;
    }

    public String[] criarRadio(String[] data, boolean check) throws Exception {
        if (!check) {
            System.out.print("Volume: ");
            data[0] = safeQuit();
            System.out.print("Canal de Radio: ");
            data[1] = safeQuit();
            System.out.print("Marca de Radio: ");
            data[2] = safeQuit();
        } else {
                System.out.println("Volume invalido;");
                System.out.print("Volume: ");
                data[0] = safeQuit();
        }
        return data;
    }

    public String[] criarLamp(String[] data, boolean check) throws Exception {
        if (!check) {
            System.out.print("Dimensao: ");
            data[0] = safeQuit();
            System.out.print("Estado (Cold/Neutral*/Warm): ");
            data[1] = safeQuit();
        } else {
            System.out.println("Dimensao invalida;");
            System.out.print("Dimensao: ");
            data[0] = safeQuit();
        }
        return data;
    }

    public String radioMenu(boolean check) {
        if (!check) {
            System.out.println("""
                    1. (Des)Ligar;
                    2. Mudar Volume;
                    3. Mudar Canal;""");
        }
        else System.out.println("Opção indisponivel");
        System.out.print("Opçao: ");
        return in.nextLine();
    }

    public String lampMenu(boolean check) {
        if (!check) {
            System.out.println("""
                    1. (Des)Ligar;
                    2. Mudar Estado;""");
        }
        else System.out.println("Opção indisponivel");
        System.out.print("Opçao: ");
        return in.nextLine();
    }
    public String cameraMenu(boolean check) {
        if (!check) {
            System.out.println("""
                    1. (Des)Ligar;
                    2. Mudar Tamanho;
                    3. Mudar Resolucao;""");
        }
        else System.out.println("Opção indisponivel");
        System.out.print("Opçao: ");
        return in.nextLine();
    }

    public String channel() {
        System.out.print("Canal Para mudar:");
        return in.nextLine();
    }

    public String volume(boolean error) {
        if (error) System.out.println("Invalido;");
        System.out.print("Volume: ");
        return in.nextLine();
    }

    public String estado(boolean error) {
        if (error) System.out.println("Invalido;");
        System.out.print("Estado (Neutral/Warm/Cold): ");
        return in.nextLine();
    }

    public String size(boolean error) {
        if (error) System.out.println("Invalido;");
        System.out.print("Tamanho do ficheiro: ");
        return in.nextLine();
    }

    public String resolution(boolean error) {
        if (error) System.out.println("Invalido;");
        System.out.print("Resolucao (DDDxDDD): ");
        return in.nextLine();
    }

    public String divMenu(boolean check) {
        if(!check) {
            clearScreen();
            String helpMenu = """
                    1. Sinal Geral;
                    2. Aceder a dispositivo;
                    3. Comprar dispositivo;""";
            System.out.println(helpMenu);
        } else {
            System.out.println("Opção não existente.");
        }
        System.out.print("Opção:");
        return in.nextLine();
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import java.util.List;


class Main {
    public static void main(String[] args) {
        new Test();
    }
}

class JP extends JPanel{
    final int height = 30;//30 pixels high.
    final int width = 30;//30 pixels wide.
    final boolean resize = true;

    private int divs = 1;

    public void setDivs(int divs) {
        this.divs = divs;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int verticalCenter = this.getHeight()/2;
        int horizontalCenter = this.getWidth()/2;

        if(!resize){
            int topLeftSquareCornerY = verticalCenter - (height/2);
            int topLeftSquareCornerX = horizontalCenter - (width/2);

            g.setColor(Color.BLUE);
            g.drawRect(topLeftSquareCornerX, topLeftSquareCornerY, width, height);
        }else{
                g.setColor(Color.BLACK);

                int gridX = divs/5;
                int gridX2 = divs%5;

                for (int j = 0; j < gridX; j ++) {
                    for (int i = 0; i < 5; i++) {
                        if (gridX2 != 0)
                            g.drawRect((this.getWidth() / 5) * i + 15, (this.getHeight() / (gridX+1))*(j) + 15, (this.getWidth() / 5) - 30, (this.getHeight() / (gridX+1)) - 30);
                        else
                            g.drawRect((this.getWidth() / 5) * i + 15, (this.getHeight() / (gridX))*(j) + 15, (this.getWidth() / 5) - 30, (this.getHeight() / (gridX)) - 30);
//                    g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
                    }
                }
                for (int i = 0; i < gridX2; i++) {
                    g.drawRect((this.getWidth() / gridX2) * i + 15, (this.getHeight() / (gridX+1))*gridX + 15, (this.getWidth() / gridX2) - 30, (this.getHeight() / (gridX+1)) - 30);
                }
        }
    }
};

public class Test extends JPanel {
    private final JFrame frame;
    private int current;
    private final JP[] menu = new JP[20];
    private final Stack<Integer> lastPanels = new Stack<>();
    private String utilizador;
    private String file = "";


    public Test() {
        File file = new File("output/");
        if (file.mkdirs()) System.out.println("Pasta de output criada.");

        sistemaPrincipal();
        current = 7;

        //Creating the Frame
        frame = new JFrame("SmartOrg");
        frame.setResizable(false);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(menu[7]);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void sistemaPrincipal(){
        menu[7] = new JP();
        // Controller.Main menu text
        JLabel menuText = new JLabel("SmartOrg", SwingConstants.CENTER);
        menuText.setFont(new Font(menuText.getName(), Font.BOLD, 30));
        JLabel subText = new JLabel("Selecione um sistema", SwingConstants.CENTER);
        subText.setFont(new Font(subText.getName(), Font.PLAIN, 20));
        JLabel nome = new JLabel("Nome do Ficheiro:");
        JLabel out = new JLabel("",SwingConstants.CENTER);

        File file = new File("output");
        File[] files = file.listFiles();

        List<String> strings = new ArrayList<>();

        if (files != null) {
            for (File f : files) strings.add(f.getName());
        }
        DefaultComboBoxModel<String> p = new DefaultComboBoxModel<>();
        p.addAll(strings);

        JComboBox<String> combo = new JComboBox<>();
        combo.setModel(p);
        try {
            combo.setSelectedIndex(0);
        } catch (IllegalArgumentException ignored) {}

        // New game and load game buttons
        JButton pre = new JButton("Sistema em branco");
        JButton fich = new JButton("Carregar Sistema");
        JButton sair = new JButton("Sair");

        GroupLayout mainMenuL = new GroupLayout(menu[7]);
        mainMenuL.setAutoCreateGaps(true);
        mainMenuL.setAutoCreateContainerGaps(true);
        menu[7].setLayout(mainMenuL);
        mainMenuL.setHorizontalGroup(
                mainMenuL.createParallelGroup(GroupLayout.Alignment.CENTER).
                        addComponent(menuText, 0,0, Short.MAX_VALUE).
                        addComponent(subText,0,0,Short.MAX_VALUE).
                        addComponent(pre,0,0,200).
                        addGroup(mainMenuL.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nome).addComponent(out, GroupLayout.Alignment.CENTER).
                                addComponent(combo,0,0,300)).
                        addComponent(fich,0,0,200).
                        addComponent(sair,0,0,200));

        mainMenuL.setVerticalGroup(
                mainMenuL.createSequentialGroup().
                        addGap(25).
                        addComponent(menuText).
                        addComponent(subText).
                        addGap(25).
                        addComponent(nome,0,0,30).
                        addComponent(combo,0,0,30).
                        addComponent(out,30,30,30).
                        addGap(20).
                        addComponent(fich,0,0,30).
                        addComponent(pre,0,0,30).
                        addComponent(sair,0,0,30));

        pre.addActionListener(e -> {
//            this.model = SistemaLNFacade.sistemaInicial();
            next(0);
        });

        fich.addActionListener(e -> {
            out.setForeground(Color.red);
            if (combo.getSelectedIndex() == -1) {
                out.setText("No file selected.");
            } else {
                try {
//                    this.model = SistemaDAO.load((String) combo.getSelectedItem());
                    this.file = (String) combo.getSelectedItem();
                    next(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    out.setText("Can't connect to file.");
                }
            }
        });

        sair.addActionListener(e-> System.exit(0));
    }

    private void menuPrincipal(){
        menu[0] = new JP();
        // Controller.Main menu text
        JLabel menuText = new JLabel("SmartOrg", SwingConstants.CENTER);
        menuText.setFont(new Font(menuText.getName(), Font.BOLD, 30));
        JLabel subText = new JLabel("Welcome", SwingConstants.CENTER);
        subText.setFont(new Font(subText.getName(), Font.PLAIN, 20));
        JLabel nome = new JLabel("Ficheiro:",SwingConstants.LEFT);
        JLabel out = new JLabel("",SwingConstants.CENTER);

        JTextField tf = new JTextField(10);
        tf.setText(this.file);

        // New game and load game buttons
        JButton login = new JButton("Login");
        JButton gravar = new JButton("Gravar");
        JButton sair = new JButton("Voltar");

        GroupLayout mainMenuL = new GroupLayout(menu[0]);
        mainMenuL.setAutoCreateGaps(true);
        mainMenuL.setAutoCreateContainerGaps(true);
        menu[0].setLayout(mainMenuL);
        mainMenuL.setHorizontalGroup(
                mainMenuL.createParallelGroup(GroupLayout.Alignment.CENTER).
                        addComponent(menuText, 0,0, Short.MAX_VALUE).
                        addComponent(subText,0,0,Short.MAX_VALUE).
                        addGroup(mainMenuL.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nome).addComponent(tf,0,0,300)).
                        addComponent(gravar,0,0,150).
                        addComponent(out).
                        addGroup(mainMenuL.createSequentialGroup().
                                addComponent(login,0,0,150).
                                addComponent(sair,0,0,150)));

        mainMenuL.setVerticalGroup(
                mainMenuL.createSequentialGroup().
                        addGap(25).
                        addComponent(menuText).
                        addComponent(subText).
                        addGap(25).
                        addComponent(nome).
                        addComponent(tf,0,0,30).
                        addComponent(gravar,0,0,50).
                        addComponent(out,0,0,30).
                        addGap(0,0,25).
                        addGroup(mainMenuL.createParallelGroup(GroupLayout.Alignment.CENTER).
                                addComponent(login,0,0,50).
                                addComponent(sair,0,0,50)));

        login.addActionListener(e -> next(2));

        gravar.addActionListener(e -> {
            try {
                //SistemaDAO.save(model,tf.getText());
                out.setText("File saved.");
                out.setForeground(Color.black);
                file = tf.getText();
            } finally {
                out.setForeground(Color.red);
                out.setText("Couldn't save file.");
            }
        });

        sair.addActionListener(this::retAddActionListener);
    }
/*
    private void login(){
        menu[1] = new JPanel();
        menu[1].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel title = new JLabel("Login do Utilizador", SwingConstants.CENTER);
        title.setFont(new Font(title.getName(), Font.BOLD, 25));
        JLabel name = new JLabel("Nome:", SwingConstants.CENTER);
        JLabel pass = new JLabel("Password:", SwingConstants.CENTER);
        JLabel out = new JLabel("");

        // text box for team name
        JTextField tf = new JTextField(10);
        JPasswordField td = new JPasswordField(10);

        // Button to receive input
        JButton choose = new JButton("Confirmar");
        choose.addActionListener(e -> {
            out.setForeground(Color.red);
            /*if (tf.getText().isEmpty()) out.setText("Campo utilizador vazio.");
            else if (model.fazerLogin(tf.getText(), TextAPI.toText(td.getPassword()))) {
                utilizador = tf.getText().substring(0,2).toUpperCase() + tf.getText().substring(2);
                if (tf.getText().toUpperCase().charAt(0) == 'F') next(2);
                else if (tf.getText().toUpperCase().charAt(0) == 'G') next(2);
                else if (tf.getText().toUpperCase().charAt(0) == 'T') next(8);
            } else out.setText("Utilizador Invalido.");
        });


        //Button to return to Controller.Main Menu
        JButton ret = new JButton("Voltar");
        ret.addActionListener(this::retAddActionListener);

        JButton reg = new JButton("Registar");
        reg.addActionListener(e -> next(6));

        //GroupLayout
        GroupLayout newGameL = new GroupLayout(menu[1]);
        menu[1].setLayout(newGameL);
        newGameL.setAutoCreateGaps(true);
        newGameL.setAutoCreateContainerGaps(true);
        newGameL.setHorizontalGroup(newGameL.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(title, 0, 0, Short.MAX_VALUE)
                .addGroup(newGameL.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(name)
                        .addComponent(tf)
                        .addComponent(pass))
                .addComponent(td).addComponent(out)
                .addComponent(choose,0,0,120)
                .addComponent(reg,0,0,120)
                .addComponent(ret,0,0,120));

        newGameL.setVerticalGroup(newGameL.createSequentialGroup()
                .addGap(40)
                .addComponent(title)
                .addGap(20)
                .addComponent(name, 0, 0 , 25)
                .addComponent(tf, 0, 0 , 25)
                .addComponent(pass,0,0,25)
                .addComponent(td,0,0,25)
                .addComponent(out,25,25,25)
                .addGap(20)
                .addComponent(choose, 0, 0, 50)
                .addComponent(reg,0,0,50)
                .addComponent(ret, 0,0, 50));


    }

    private void registar() {
        menu[6] = new JPanel();
        menu[6].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel title = new JLabel("Registro do Utilizador", SwingConstants.CENTER);
        title.setFont(new Font(title.getName(), Font.BOLD, 25));
        JLabel name = new JLabel("Nome:", SwingConstants.CENTER);
        JLabel pass = new JLabel("Password:", SwingConstants.CENTER);
        JLabel checkPass = new JLabel("Verifica a password:");
        JLabel out = new JLabel("");

        // text box for team name
        JTextField tf = new JTextField(10);
        JPasswordField td = new JPasswordField(10);
        JPasswordField tv = new JPasswordField(10);


        // Button to receive input
        JButton choose = new JButton("Confirmar");
       /* choose.addActionListener(e -> {
            out.setForeground(Color.red);
            if (tf.getText().isEmpty()) out.setText("Campo de utilizador vazio.");
            else if (tf.getText().length() > 10) out.setText("Campo de utilzador maior que 10 caracteres.");
            else if (!Arrays.equals(td.getPassword(), tv.getPassword())) out.setText("Palavra-passes não correspondem.");
            else {
                out.setForeground(Color.black);
                if ((tf.getText().toUpperCase().charAt(0) == 'F' && model.addFuncionario(tf.getText(),TextAPI.toText(td.getPassword())))
                        || (tf.getText().toUpperCase().charAt(0) == 'T' && model.addTecnico(tf.getText(),TextAPI.toText(td.getPassword())))) {
                    out.setText("Utilizador " + tf.getText().substring(0, 1).toUpperCase() + tf.getText().substring(1) + " foi registado/a com sucesso");
                    tf.setText(""); td.setText(""); tv.setText("");
                }
                else {
                    out.setForeground(Color.red);
                    out.setText("Não é possivel criar este utilizador");
                }
            }
        });

        //Button to return to Controller.Main Menu
        JButton ret = new JButton("Voltar");
        ret.addActionListener(this::retAddActionListener);

        //GroupLayout
        GroupLayout newGameL = new GroupLayout(menu[6]);
        menu[6].setLayout(newGameL);
        newGameL.setAutoCreateGaps(true);
        newGameL.setAutoCreateContainerGaps(true);
        newGameL.setHorizontalGroup(newGameL.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(title, 0, 0, Short.MAX_VALUE)
                .addGroup(newGameL.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(name)
                        .addComponent(tf)
                        .addComponent(pass)
                        .addComponent(td)
                        .addComponent(checkPass)
                        .addComponent(tv))
                .addComponent(out)
                .addComponent(choose,0,0,120)
                .addComponent(ret,0,0,120));

        newGameL.setVerticalGroup(newGameL.createSequentialGroup()
                .addGap(40)
                .addComponent(title)
                .addGap(20)
                .addComponent(name, 0, 0 , 25)
                .addComponent(tf, 0, 0 , 25)
                .addComponent(pass,0,0,25)
                .addComponent(td,0,0,25)
                .addComponent(checkPass,0,0,25)
                .addComponent(tv,0,0,25)
                .addGap(20)
                .addComponent(out,20,20,20)
                .addGap(20)
                .addComponent(choose, 0, 0, 25)
                .addComponent(ret, 0,0, 25));


    }
*/
    private void funcMenu() {
        menu[2] = new JP();
        menu[2].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel funcMenu = new JLabel("<html><center>Bem-vindo</center></html>",SwingConstants.CENTER);
        funcMenu.setFont(new Font(funcMenu.getName(), Font.BOLD, 25));

        JButton back = new JButton("LogOut");
        JButton normal = new JButton("Nova Casa");
        JButton expresso = new JButton("Nova companhia");
        JButton verificar = new JButton("Calendario de dias");
        JButton orca = new JButton("Monitorizar casas");

        normal.addActionListener(e -> next(3));
        expresso.addActionListener(e -> next(4));
        orca.addActionListener(e -> next(5));
        back.addActionListener(this::retAddActionListener);

        GroupLayout funcLayout = new GroupLayout(menu[2]);
        menu[2].setLayout(funcLayout);
        funcLayout.setAutoCreateGaps(true);
        funcLayout.setAutoCreateContainerGaps(true);

        funcLayout.setHorizontalGroup(
                funcLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(funcMenu,0,0,350)
                        .addGap(0,0,Short.MAX_VALUE)
                        .addComponent(normal,0,0,200)
                        .addComponent(expresso,0,0,200)
                        .addComponent(back,0,0,200)
                        .addComponent(orca,0,0,200)
                        .addComponent(verificar,0,0,200)
                        .addGap(0,0,Short.MAX_VALUE));

        funcLayout.setVerticalGroup(
                funcLayout.createSequentialGroup()
                        .addGap(35)
                        .addComponent(funcMenu)
                        .addGap(0,0,Short.MAX_VALUE)
                        .addComponent(normal,0,0,40)
                        .addComponent(expresso,0,0,40)
                        .addComponent(orca,0,0,40)
                        .addComponent(verificar,0,0,40)
                        .addGap(30)
                        .addComponent(back,0,0,40)
                        .addGap(0,0,Short.MAX_VALUE));

    }

    private void houseCreate() {


        this.menu[3] = new JP();

        this.menu[3].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel out = new JLabel("",SwingConstants.CENTER);

        JSlider options = new JSlider(1,20,1);

        JLabel description = new JLabel("Numero de divisões: " + options.getValue());

        options.addChangeListener(e -> {
            this.menu[3].setDivs(options.getValue());
            this.menu[3].repaint();
            description.setText("Numero de divisões: " + options.getValue());
        });

        JButton confirm = new JButton("Confirmar");
        /*confirm.addActionListener(e -> {
            out.setForeground(Color.red);
            boolean isNumber = false;
            try {Integer.parseInt(nifArea.getText());} catch (NumberFormatException ex) {isNumber = true;}

            if (nifArea.getText().equals("") || emailArea.getText().equals("") || descriptionArea.getText().equals("")) out.setText("Campos Vazios.");
            else if (!emailArea.getText().contains("@")) out.setText("Email inválido.");
            else if (isNumber){out.setText("NIF Invalido");}
            else {
                model.addPedidoOrcamento(utilizador.substring(1));
                int cod = model.registaEquipamento(nifArea.getText(), emailArea.getText(), descriptionArea.getText());
                out.setForeground(Color.black);
                out.setText("Pedido de codigo " + cod + " registado!");
                nifArea.setText("");
                emailArea.setText("");
                descriptionArea.setText("");
            }
        });*/

        JButton ret = new JButton("Voltar");
        ret.addActionListener(this::retAddActionListener);

        GroupLayout pd = new GroupLayout(this.menu[3]);
        this.menu[3].setLayout(pd);
        pd.setAutoCreateGaps(true);
        pd.setAutoCreateContainerGaps(true);

        pd.setHorizontalGroup(
                pd.createSequentialGroup()
                        .addGap(0,0,300)
                        .addGroup(
                                pd.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(description)
                                        .addComponent(options,200,200,400)
                        )
                        .addGap(0,0,300)
                        .addGroup(
                                pd.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pd.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(out,0,0,300))
                                        .addGroup(
                                                pd.createSequentialGroup()
                                                        .addComponent(confirm,0,0,150)
                                                        .addComponent(ret,0,0,150)
                                        )
                        )
                        .addGap(0,0,300)
        );

        pd.setVerticalGroup(
                pd.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(
                                pd.createSequentialGroup()
                                        .addGap(0,0,30)
                                        .addComponent(description)
                                        .addComponent(options,0,0,20)
                                        .addGap(0,0,30)
                        )
                        .addGroup(
                                pd.createSequentialGroup()
                                        .addGap(0,0,30)
                                        .addComponent(out,30,30,30)
                                        .addGroup(pd.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(confirm,0,0,50)
                                                .addComponent(ret,0,0,50)
                                        )
                                        .addGap(0,0,300)
                        )
        );

    }

    private void companyCreate() {
        this.menu[4] = new JP();
        this.menu[4].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel tax = new JLabel("Tax Value:",SwingConstants.CENTER);
        JLabel name = new JLabel("Nome:",SwingConstants.CENTER);
        JLabel baseValue = new JLabel("Base Value:",SwingConstants.CENTER);
        JLabel out = new JLabel("");

        JTextField nameArea = new JTextField(10);
        JTextField baseArea = new JTextField(10);
        JTextField taxArea = new JTextField(10);

        JButton confirm = new JButton("Confirmar");
        confirm.addActionListener(e -> {
            out.setForeground(Color.red);
            boolean isNumber = false;
            boolean isPhone = false;
            //try {Integer.parseInt(nifArea.getText());} catch (NumberFormatException ex) {isNumber = true;}
            //try {Integer.parseInt(emailArea.getText());} catch (NumberFormatException ex) {isNumber = true;}

            /*if (nifArea.getText().equals("") || emailArea.getText().equals("") || opt.isSelectionEmpty()) out.setText("Campos Vazios.");
            else if (isPhone || emailArea.getText().length() != 9) out.setText("Numero de telefone inválido.");
            else if (isNumber || nifArea.getText().length() != 9){out.setText("NIF Invalido");}
            else {
                //model.addPedidoExpresso(utilizador , opt.getSelectedValue());
                //int cod = model.registaEquipamento(nifArea.getText(), emailArea.getText(),"Expresso");
                out.setForeground(Color.black);
                //out.setText("Pedido de codigo " + cod + " registado!");
                nifArea.setText("");
                emailArea.setText("");
            }*/
        });

        JButton ret = new JButton("Voltar");
        ret.addActionListener(this::retAddActionListener);

        GroupLayout pe = new GroupLayout(this.menu[4]);
        this.menu[4].setLayout(pe);
        pe.setAutoCreateGaps(true);
        pe.setAutoCreateContainerGaps(true);

        pe.setHorizontalGroup(
                pe.createSequentialGroup()
                        .addGap(0,0,300)
                        .addGroup(
                            pe.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addGap(0,0,300)
                                    .addComponent(name)
                                    .addComponent(nameArea,100,150,300)
                                    .addComponent(baseValue)
                                    .addComponent(baseArea,100,150,300)
                                    .addComponent(tax)
                                    .addComponent(taxArea,100,150,300)
                                    .addGroup(pe.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(out,240,240,240))
                                    .addGroup(pe.createSequentialGroup()
                                            .addComponent(confirm,0,0,150)
                                            .addComponent(ret,0,0,150)
                                    )
                        )
                        .addGap(0,0,300)
        );

        pe.setVerticalGroup(
                                pe.createSequentialGroup()
                                        .addGap(0,0,30)
                                        .addComponent(name)
                                        .addComponent(nameArea,0,0,30)
                                        .addComponent(baseValue)
                                        .addComponent(baseArea,0,0,30)
                                        .addComponent(tax)
                                        .addComponent(taxArea, 0,0,30)
                                        .addComponent(out,30,30,30)
                                        .addGap(0,0,30)
                                        .addGroup(pe.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(confirm,0,0,50)
                                                .addComponent(ret,0,0,50)
                                        )
                                        .addGap(0,0,300)
        );

    }

    private void budgets() {
        this.menu[5] = new JP();
        this.menu[5].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel options = new JLabel("Orçamentos:");
        JLabel status = new JLabel("");

        //List<String> strings = Arrays.asList(model.opcoesPorAprovar());
        DefaultListModel<String> p = new DefaultListModel<>();
        //p.addAll(strings);

        JList<String> opt = new JList<>();
        opt.setFixedCellWidth(100);
        opt.setFixedCellHeight(20);
        opt.setModel(p);
        JScrollPane r = new JScrollPane(opt);

        JButton reject = new JButton("Rejeitar");
        JButton confirm = new JButton("Confirmar");
        JButton ret = new JButton("Voltar");

        confirm.addActionListener(e -> {
            if (!opt.isSelectionEmpty()) {
                p.remove(opt.getSelectedIndex());
                //model.aprovarOrcamento(opt.getSelectedIndex());
                status.setText("Aprovado");
            } else {status.setText("Nada selecionado");}
        });

        reject.addActionListener(e -> {
            if (!opt.isSelectionEmpty()){
                p.remove(opt.getSelectedIndex());
                //model.rejeitarOrcamento(opt.getSelectedIndex());
                status.setText("Rejeitado");
            } else {status.setText("Nada selecionado");}
        });

        ret.addActionListener(this::retAddActionListener);

        GroupLayout pe = new GroupLayout(this.menu[5]);
        this.menu[5].setLayout(pe);
        pe.setAutoCreateGaps(true);
        pe.setAutoCreateContainerGaps(true);

        pe.setHorizontalGroup(pe.createSequentialGroup()
                .addGap(0,0,300)
                .addGroup(pe.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(options)
                        .addComponent(r,200,200,400))
                .addGap(0,0,300)
                .addGroup(pe.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(status)
                        .addGroup(pe.createSequentialGroup()
                                .addComponent(confirm,150,150,150)
                                .addComponent(reject,150,150,150))
                        .addComponent(ret,0,0,150))
                .addGap(0,0,300));

        pe.setVerticalGroup(pe.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(pe.createSequentialGroup()
                        .addGap(0,0,30)
                        .addComponent(options)
                        .addComponent(r,0,0,400)
                        .addGap(0,0,30))
                .addGroup(pe.createSequentialGroup()
                        .addGap(0,0,50)
                        .addComponent(status)
                        .addGap(0,0,80)
                        .addGroup(pe.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(confirm,0,0,50)
                                .addComponent(reject,0,0,50))
                        .addComponent(ret,0,0,150)
                        .addGap(0,0,300)));
    }

    private void tecMenu () {
        menu[8] = new JP();
        menu[8].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel funcMenu = new JLabel("<html><center>Bem-vindo " + utilizador + "</center></html>",SwingConstants.CENTER);
        funcMenu.setFont(new Font(funcMenu.getName(), Font.BOLD, 25));

        JButton back = new JButton("LogOut");
        JButton calculaOrcamento = new JButton("Calcular Orçamento");
        JButton reparar = new JButton("Reparar equipamento");

        calculaOrcamento.addActionListener(e -> next(9));
        back.addActionListener(this::retAddActionListener);

        GroupLayout funcLayout = new GroupLayout(menu[8]);
        menu[8].setLayout(funcLayout);
        funcLayout.setAutoCreateGaps(true);
        funcLayout.setAutoCreateContainerGaps(true);

        funcLayout.setHorizontalGroup(
                funcLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(funcMenu,0,0,350)
                        .addGap(0,0,Short.MAX_VALUE)
                        .addComponent(back,0,0,200)
                        .addComponent(calculaOrcamento,0,0,200)
                        .addComponent(reparar,0,0,200)
                        .addGap(0,0,Short.MAX_VALUE));

        funcLayout.setVerticalGroup(
                funcLayout.createSequentialGroup()
                        .addGap(35)
                        .addComponent(funcMenu)
                        .addGap(0,0,Short.MAX_VALUE)
                        .addComponent(calculaOrcamento,0,0,40)
                        .addComponent(reparar,0,0,40)
                        .addGap(30)
                        .addComponent(back,0,0,40)
                        .addGap(0,0,Short.MAX_VALUE));
    }

    private void plano () {
        this.menu[9] = new JP();
        this.menu[9].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel description = new JLabel("Descrição do equipamento:");
        JLabel nif = new JLabel("Custo:",SwingConstants.LEFT);
        JLabel email = new JLabel("Dureção:",SwingConstants.LEFT);
        JLabel out = new JLabel("",SwingConstants.CENTER);
        JLabel plan = new JLabel("Plano de trablaho:");

        //ArrayList<Passo> umPasso = new ArrayList<>();
        //List<List<Passo>> plano = new ArrayList<>();
        //Pedido i = model.getPedidoOrcamento();
        //Equipamento n = i == null ? null:model.getArmazem().get(i.codigo_registo);

        JTextArea descriptionArea = new JTextArea(/*n == null ? "Sem pedidos de orçamento.":"Pedido numero:" + n.getCodigo_registo() + "\n" + n.getDescricao_problema() + ".",5,20*/);
        descriptionArea.setEditable(false);
        JScrollPane dA = new JScrollPane(descriptionArea);

        JTextArea passos = new JTextArea();
        passos.setEditable(false);
        JScrollPane dE = new JScrollPane(passos);

        JTextField nifArea = new JTextField(10);
        JTextField emailArea = new JTextField(10);

        JButton confirm = new JButton("Criar Passo");
        /*confirm.addActionListener(e -> {
            //List<Passo> unpasso = new ArrayList<>();
            out.setForeground(Color.red);
            int custo = 0,duracao = 0;
            boolean isbothNumber = false;
            try {custo = Integer.parseInt(nifArea.getText());} catch (NumberFormatException ex) {isbothNumber = true;}
            try {duracao = Integer.parseInt(emailArea.getText());} catch (NumberFormatException ex) {isbothNumber = true;}

            if (nifArea.getText().equals("") || emailArea.getText().equals("")) out.setText("Campos Vazios.");
            else if (isbothNumber){out.setText("Valores Invalidos");}
            else {
                if (!umPasso.isEmpty()) {
                    plano.add(umPasso);
                }
                unpasso.add(new Passo(custo, duracao));
                umPasso.addAll(unpasso);
                out.setForeground(Color.black);
                out.setText("Passo criado!");
                nifArea.setText("");
                emailArea.setText("");
                passos.setText(passos.getText() + "\nCusto: " + custo + "\nDuração: " + duracao);
            }
        });*/

        JButton confirm1 = new JButton("Criar Sub-passo");
        /*confirm1.addActionListener(e -> {
            out.setForeground(Color.red);
            int custo = 0,duracao = 0;
            boolean isbothNumber = false;
            try {custo = Integer.parseInt(nifArea.getText());} catch (NumberFormatException ex) {isbothNumber = true;}
            try {duracao = Integer.parseInt(emailArea.getText());} catch (NumberFormatException ex) {isbothNumber = true;}

            if (nifArea.getText().equals("") || emailArea.getText().equals("")) out.setText("Campos Vazios.");
            else if (isbothNumber){out.setText("Valores Invalidos");}
            else {
                umPasso.add(new Passo(custo, duracao));
                out.setForeground(Color.black);
                out.setText("Sub-Passo criado!");
                nifArea.setText("");
                emailArea.setText("");
                passos.setText(passos.getText() + "\nCusto: " + custo + "\nDuração: " + duracao);
            }
        });*/

        JButton confirm2 = new JButton("Terminar Plano");
        /*confirm2.addActionListener(e -> {
            plano.add(umPasso);
            passos.setText(passos.getText() + "\n" + model.calcularOrcamento(plano).toString());
        });
        */
        JButton ret = new JButton("Voltar");
        ret.addActionListener(this::retAddActionListener);

        GroupLayout pd = new GroupLayout(this.menu[9]);
        this.menu[9].setLayout(pd);
        pd.setAutoCreateGaps(true);
        pd.setAutoCreateContainerGaps(true);

        pd.setHorizontalGroup(
                pd.createSequentialGroup()
                        .addGap(0,0,300)
                        .addGroup(
                                pd.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(description)
                                        .addComponent(dA,200,200,400)
                                        .addComponent(plan)
                                        .addComponent(dE,200,200,400)
                        )
                        .addGap(0,0,300)
                        .addGroup(
                                pd.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(nif)
                                        .addComponent(nifArea,100,150,300)
                                        .addComponent(email)
                                        .addComponent(emailArea,100,150,300)
                                        .addGroup(pd.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(out,0,0,300))
                                        .addComponent(confirm1,0,0,150)
                                        .addComponent(confirm2,0,0,150)
                                        .addComponent(confirm,0,0,150)
                                        .addComponent(ret,0,0,150)
                        )
                        .addGap(0,0,300));

        pd.setVerticalGroup(
                pd.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(
                                pd.createSequentialGroup()
                                        .addGap(0,0,30)
                                        .addComponent(description)
                                        .addComponent(dA,0,0,50)
                                        .addComponent(plan)
                                        .addComponent(dE,0,0,400)
                                        .addGap(0,0,30)
                        )
                        .addGroup(
                                pd.createSequentialGroup()
                                        .addGap(0,0,30)
                                        .addComponent(nif)
                                        .addComponent(nifArea,0,0,30)
                                        .addComponent(email)
                                        .addComponent(emailArea,0,0,30)
                                        .addComponent(out,30,30,30)
                                        .addComponent(confirm,0,0,50)
                                        .addComponent(confirm1,0,0,50)
                                        .addComponent(confirm2,0,0,50)
                                        .addComponent(ret,0,0,50)
                                        .addGap(0,0,30)
                        ));

    }

    private void retAddActionListener(ActionEvent actionEvent) {handler(lastPanels.pop());}

    private void next(int d) {lastPanels.push(current);handler(d);}

    private void handler(int panelD) {
        current = panelD;

        switch (panelD) {
            case 0 -> {
                menuPrincipal();
                frame.setSize(400, 400);
            }
            case 1 -> {
                funcMenu();
                frame.setSize(400, 400);
            }
            case 2 -> {
                funcMenu();
                frame.setSize(400, 450);
            }
            case 3 -> {
                houseCreate();
                frame.setSize(760,300);
            }
            case 4 -> {
                companyCreate();
                frame.setSize(400,400);
            }
            case 5 -> {
                budgets();
                frame.setSize(760,300);
            }
            case 6 -> {
                houseCreate();
                frame.setSize(760,300);
            }
            case 7 -> {
                sistemaPrincipal();
                frame.setSize(400,400);
            }
            case 8 -> {
                tecMenu();
                frame.setSize(400,400);
            }
            case 9 -> {
                plano();
                frame.setSize(760,450);
            }
        }

        frame.setLocationRelativeTo(null);
        frame.setContentPane(menu[panelD]);
        frame.revalidate();

    }
}

class TextAPI {
    public static String toText(char[] text) {
        StringBuilder ret = new StringBuilder();
        for (char c : text) ret.append(c);
        return ret.toString();
    }
}
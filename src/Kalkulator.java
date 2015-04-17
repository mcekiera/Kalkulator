/** Kalkulator v.2 
 * Marcin Cekiera, 02.2015 
 * prosty kalkulator, oparty o klasę BigDecimal 
 *
 **/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.*;

public class Kalkulator {
    JFrame frame;
    JPanel mainPanel;
    JPanel displayPanel;
    JPanel sidePanel;
    JTextArea mainDisplay;
    JTextArea secDisplay;
    JTextArea sideTextArea;
    JTextField sideTextField;

    String numValue;
    ArrayList<BigDecimal> memory;
    ArrayList<String> operation;
    BigDecimal result;
    BigDecimal memoryStore;
    Operations var;
    Integer prec;
    boolean root;

    JCheckBoxMenuItem wrap;
    JCheckBoxMenuItem history;

    public static void main(String[] args){
        Kalkulator calc = new Kalkulator();
        calc.go();
    }
    public void go(){

        /**kluczowe zmienne i obiekty*/
        numValue = "";              //string, do którego wprowadzane będą liczby
        memory = new ArrayList<BigDecimal>();   // arraylist przechowywujący wprowadzone wartości 
        operation = new ArrayList<String>();    // allaylist dla oznaczen operacji 
        prec = 15;                  // kontroluje ilość wyświetlanych miejsc po przecinku,
        memoryStore = new BigDecimal(0);         // BigDecimal przechowywujący wartości w memory 
        var = new Operations();                     // obiekt dzięki któremu można przeprowadzić metodę uzyskującą wyniki
        root=false;

        /** GUI **/
        ImageIcon img = new ImageIcon("src/icon.png");
        frame = new JFrame("Kalkulator v.2");
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel = new JPanel();                                               //głowny panel, na nim jest oparte całe GUI 
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        mainPanel.setLayout(new GridLayout(6,4));
        mainPanel.setBackground(Color.GRAY); 
 
         /*menu*/
        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Opcje");
        JMenu help = new JMenu("Pomoc");
        JMenuItem viewHelp = new JMenuItem("Pomoc");
        viewHelp.addActionListener(new helpActList());
        JMenuItem info = new JMenuItem("Informacje");
        info.addActionListener(new infoActList());
        wrap = new JCheckBoxMenuItem("Zawijaj wiersze");
        history = new JCheckBoxMenuItem("Historia wyników");
        wrap.addItemListener(new wrapList());
        history.addActionListener(new histList());
        JMenuItem precison = new JMenuItem("Precyzja");
        precison.addActionListener(new precList());

        menuBar.add(options);
        menuBar.add(help);
        options.add(wrap);
        options.add(history);
        options.add(precison);
        help.add(viewHelp);
        help.add(info);
        frame.setJMenuBar(menuBar); 
 
        /*czcionki*/
        Font mainDisplayFont = new Font("Ariala",Font.BOLD, 20);                // czcionka mainDisplay 
        Font secDisplayFont = new Font("Ariala", Font.PLAIN, 10);               //czcionka secDisplay 
        Font buttonsFont = new Font("Ariala Black", Font.PLAIN,20);             //czcionka przycisków 
        Font cButtonFont = new Font("Ariala Black", Font.BOLD,20);              //czcionka przycisku C 
 
        /*wyświetlacze*/
        mainDisplay = new JTextArea(2,10);                                      //główny wyświetlacz 
        mainDisplay.setFont(mainDisplayFont);
        mainDisplay.setText("");
        mainDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mainDisplay);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        secDisplay = new JTextArea(1,20);                                       //mniejszy wyświetlacz 
        secDisplay.setFont(secDisplayFont);
        secDisplay.setText(numValue);
        secDisplay.setEditable(false);

        displayPanel = new JPanel();                                            // panel z wyświetlaczami 
        displayPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.NORTH, displayPanel);
        displayPanel.add(BorderLayout.NORTH, secDisplay);
        displayPanel.add(BorderLayout.CENTER, scrollPane);
        displayPanel.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY)); 
 
        /* budowa poszczególnych przycisków*/
        JButton C = new JButton("C");
        C.setFont(cButtonFont);
        C.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        C.addActionListener(new CActList());
        mainPanel.add(C);

        JButton MC = new JButton("MC");
        MC.setFont(buttonsFont);
        MC.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        MC.addActionListener(new MCActList());
        mainPanel.add(MC);

        JButton MS = new JButton("MS");
        MS.setFont(buttonsFont);
        MS.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        MS.addActionListener(new MSActList());
        mainPanel.add(MS);

        JButton MR = new JButton("MR");
        MR.setFont(buttonsFont);
        MR.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        MR.addActionListener(new MRActList());
        mainPanel.add(MR);

        JButton percent = new JButton("%");
        percent.setFont(buttonsFont);
        percent.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        percent.addActionListener(new percentActList());
        mainPanel.add(percent);

        JButton rootSquere = new JButton("\u221A");
        rootSquere.setFont(buttonsFont);
        rootSquere.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        rootSquere.addActionListener(new rootActList());
        mainPanel.add(rootSquere);

        JButton nPower = new JButton("x"+"\u207F");
        nPower.setFont(buttonsFont);
        nPower.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        nPower.addActionListener(new nActList());
        mainPanel.add(nPower);

        JButton plusminus = new JButton("\u00B1");
        plusminus.setFont(buttonsFont);
        plusminus.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        plusminus.addActionListener(new pmActList());
        mainPanel.add(plusminus);

        JButton B7 = new JButton("7");
        B7.setFont(buttonsFont);
        B7.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B7.addActionListener(new B7ActList());
        mainPanel.add(B7);

        JButton B8 = new JButton("8");
        B8.setFont(buttonsFont);
        B8.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B8.addActionListener(new B8ActList());
        mainPanel.add(B8);

        JButton B9 = new JButton("9");
        B9.setFont(buttonsFont);
        B9.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B9.addActionListener(new B9ActList());
        mainPanel.add(B9);

        JButton Bplus = new JButton("+");
        Bplus.setFont(buttonsFont);
        Bplus.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bplus.addActionListener(new BplusActList());
        mainPanel.add(Bplus);

        JButton B4 = new JButton("4");
        B4.setFont(buttonsFont);
        B4.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B4.addActionListener(new B4ActList());
        mainPanel.add(B4);

        JButton B5 = new JButton("5");
        B5.setFont(buttonsFont);
        B5.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B5.addActionListener(new B5ActList());
        mainPanel.add(B5);

        JButton B6 = new JButton("6");
        B6.setFont(buttonsFont);
        B6.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B6.addActionListener(new B6ActList());
        mainPanel.add(B6);

        JButton Bmulti = new JButton("\u00D7");
        Bmulti.setFont(buttonsFont);
        Bmulti.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bmulti.addActionListener(new BmultiActList());
        mainPanel.add(Bmulti);

        JButton B1 = new JButton("1");
        B1.setFont(buttonsFont);
        B1.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B1.addActionListener(new B1ActList());
        mainPanel.add(B1);

        JButton B2 = new JButton("2");
        B2.setFont(buttonsFont);
        B2.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B2.addActionListener(new B2ActList());
        mainPanel.add(B2);

        JButton B3 = new JButton("3");
        B3.setFont(buttonsFont);
        B3.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B3.addActionListener(new B3ActList());
        mainPanel.add(B3);

        JButton Bdiv = new JButton(":");
        Bdiv.setFont(buttonsFont);
        Bdiv.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bdiv.addActionListener(new BdivActList());
        mainPanel.add(Bdiv);

        JButton B0 = new JButton("0");
        B0.setFont(buttonsFont);
        B0.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        B0.addActionListener(new B0ActList());
        mainPanel.add(B0);

        JButton Bpoint = new JButton(".");
        Bpoint.setFont(buttonsFont);
        Bpoint.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bpoint.addActionListener(new BpointActList());
        mainPanel.add(Bpoint);

        JButton Bequal = new JButton("=");
        Bequal.setFont(buttonsFont);
        Bequal.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bequal.addActionListener(new BequalActList());
        mainPanel.add(Bequal);

        JButton Bminus = new JButton("-");
        Bminus.setFont(buttonsFont);
        Bminus.setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
        Bminus.addActionListener(new BminusActList());
        mainPanel.add(Bminus);

        //panel historii wyników 

        sidePanel = new JPanel();
        JPanel sideTextPanel = new JPanel();
        sideTextPanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.WHITE);
        sideTextField = new JTextField("M: ",7);
        sideTextArea = new JTextArea(11,7);
        sideTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sideTextField.setEditable(false);
        sideTextArea.setEditable(false);
        sideTextArea.setLineWrap(true);
        JScrollPane sideScroll = new JScrollPane(sideTextArea);
        sideScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        sideTextPanel.add(BorderLayout.NORTH, sideTextField);
        sideTextPanel.add(BorderLayout.CENTER, sideScroll);
        sideScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sideScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidePanel.add(sideTextPanel);
        frame.getContentPane().add(BorderLayout.EAST,sidePanel);
        sidePanel.setVisible(false);

        frame.setSize(250, 300);
        frame.isResizable();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    /** Operations to metoda dająca wyniki dla poszczególnych operacji, pobiera ona ArrayList<BigDecimal> z wartościami
     * listy memory, oraz ArrayList<String> z wartościami listy operation. Pobiera ostatnie wartości wcześniej wprowadzone 
     * i zapisane w liście memory, a następnie na podstawie oznaczenia operacji (1+,2-,3*,4:,5 potęgowanie, 7 % 
     * - nie ma 6, pierwiastkowania, jest ono obliczene włąsną metodą) pobranej z listy operation, dokonuje obliczeń i 
     * zwraca BigDecimal temp z wynikiem. Wszystkie obliczenia oparte są o metody klasy BigDecimal(add, substract, multiply, 
     * divide, pow) 
     * Operations należy do odrębnej klasy wewnętrznej, dzięki czemu może swobodnie używać zmiennych publicznych innych klas,
     * */

    class Operations {
        public BigDecimal opEqual(ArrayList<BigDecimal> list, ArrayList<String> s){
            BigDecimal temp = new BigDecimal(0);
            int op = Integer.parseInt(s.get(0));
            switch(op){
                case 1:
                    temp = list.get(0).add(list.get(1));
                    break;
                case 2:
                    temp = list.get(0).subtract(list.get(1));
                    break;
                case 3:
                    temp = list.get(0).multiply(list.get(1));
                    break;
                case 4:
                    temp = list.get(0).divide(list.get(1), prec, RoundingMode.HALF_DOWN).stripTrailingZeros();
                    break;
                case 5:
                    temp = list.get(0).pow(list.get(1).intValue());
                    break;
                case 7:
                    temp = list.get(0).divide(new BigDecimal("100")).multiply(list.get(1));
                    break;
            }
            return temp;
        }
    }
    /** Listenery poszczególnych przycisków. 
     * Listenery przycisków wartości liczbowych wyglądają tak samo, poza kodem wprowadzającym wartości na wyświetlacz oraz 
     * do pamięci, pojawia się tu również kod odpowiedzialny za blokowanie wprowadzania dodatkowych cyfr i znaków, do 
     * wyników pierwiastkowania, zmiany znaku, oraz przy wyświetlaniu błędów. 
     * Początkowo miało to być wykorzystane tylko w przypadku pierwiastkowania, opartego o inną metodę niż reszta podstawowych 
     * działań, ostatecznie okazało się to być przydatne rozwiązanie również w inncy przypakach. 
     *
     * Wszystkie listenery zostały oparte o klasy wewnętrze 
     * */
    class B9ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="9";
            mainDisplay.setText(numValue);
            secDisplay.append("9");

            root=false;
        }
    }
    class B8ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="8";
            mainDisplay.setText(numValue);
            secDisplay.append("8");

            root=false;
        }
    }
    class B7ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="7";
            mainDisplay.setText(numValue);
            secDisplay.append("7");

            root=false;
        }
    }
    class B6ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="6";
            mainDisplay.setText(numValue);
            secDisplay.append("6");

            root=false;
        }
    }
    class B5ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="5";
            mainDisplay.setText(numValue);
            secDisplay.append("5");

            root=false;
        }
    }
    class B4ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="4";
            mainDisplay.setText(numValue);
            secDisplay.append("4");

            root=false;
        }
    }
    class B3ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="3";
            mainDisplay.setText(numValue);
            secDisplay.append("3");

            root=false;
        }
    }
    class B2ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="2";
            mainDisplay.setText(numValue);
            secDisplay.append("2");

            root=false;
        }
    }
    class B1ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="1";
            mainDisplay.setText(numValue);
            secDisplay.append("1");

            root=false;
        }
    }
    class B0ActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
            numValue+="0";
            mainDisplay.setText(numValue);
            secDisplay.append("0");

            root=false;
        }
    }
    class BpointActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if(numValue.equals("")){
                numValue+="0.";
                secDisplay.append("0.");
                mainDisplay.setText(numValue);
            }else{
                numValue+=".";
                secDisplay.append(".");
                mainDisplay.setText(numValue);
            }
        }
    }

    /**
     * Listenery działań są dużo bardziej rozbudowane, każdy rozbudowywany byl o dadatkowe opcje w przypakdu zaintnienia 
     * odmiennej sytuacji. 
     */
    class BequalActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;                                                         //wyłącza blokadę przycików wprowadzania liczb, 
            if(numValue.equals("0") && operation.get(0).equals("4")){           //jeżeli ktoś próbuje dzielić przez 0 
                secDisplay.setText("Błąd: nie można dzielić przez \"0\"");      //wyświetlenie komunikatu, 
                operation.removeAll(operation);                                 //kasowanie wprowadzonych wartości 
                memory.removeAll(memory);
                numValue="";
                root=true;                                                       //blokada wprowadzania, żeby nie dopisywało cyfr do informacji o błędzie, 
            }else if(operation.isEmpty() || operation.get(0).equals("0")){       //jeżeli ktoś wcisnął "=" bez przeprowadzenia działania 
                sideTextArea.append("\u00BB"+mainDisplay.getText()+"\n");        //wprowadza wartość do historii, bez względu na to czy ją widać czy nie, 

            }else{
                try{                                                          //w przypadku normalnej próby uzyskania wyniku działania: 
                    memory.add(new BigDecimal(numValue));       // dodanie drugiej wartosci do pamieci
                    numValue="";
                    result = var.opEqual(memory, operation);    //przeprowadzenie metody Operations na zmiennych
                    mainDisplay.setText(result.setScale(prec,RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString());

                    operation.add(0,"0");
                    memory.removeAll(memory);
                    secDisplay.setText("");
                    sideTextArea.append("\u00BB"+mainDisplay.getText()+"\n");
 
                /*tylko w sytuacji, kiedy ktoś przez pomyłkę wprowadzi kolejną operację(np."+"), a następnie wciśnie "="*/
                }catch (Exception ex){
                    sideTextArea.append("\u00BB"+mainDisplay.getText()+"\n");
                    if(ex instanceof NumberFormatException){
                        memory.removeAll(memory);
                        operation.removeAll(operation);
                        numValue=mainDisplay.getText();
                        secDisplay.setText(numValue);
                    }else if(ex instanceof IndexOutOfBoundsException){
                        memory.removeAll(memory);
                        operation.removeAll(operation);
                        numValue=mainDisplay.getText();
                    }
                }
            }
        }
    }

    /**
     * Schemat listenerów podstawowych działąń matematycznych jest bardzo podobny, obejmuje 3 przypadki: 
     * - jeżeli jest to pierwsze przeprowadzane działanie, 
     * - jeżeli jest to działanie podejmowane na wyniku wcześniejszych działań, 
     * - jeżeli ktoś przeprowadza kolejne operacje bez użycia "=" 
     *
     */
    class BplusActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));   //wprowadzenie wartości do pamięci z numValue, 
                secDisplay.append("+");                 //dopisanie znaku do wyświetlacza pomocniczego 
                numValue="";                            //zerowanie wartości roboczej 
                operation.add(0,"1");                   //dodanie oznaczeń operacji matematycznej 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));  //wprowadzenie wartości do pamięci z wyświetlacza 
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,"1");
                secDisplay.append("+"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& !operation.get(0).equals("0")){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.toString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"1");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+"+");
                }catch (NumberFormatException ex){
                    ex.printStackTrace(); 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje*/
                    if(!operation.get(0).equals("1")){
                        operation.add(0,"1");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+"+");
                    }
                }
            }
        }
    }
    class BminusActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append("-");
                numValue="";
                operation.add(0,"2"); 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,"2");
                secDisplay.append("-"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& !operation.get(0).equals("0")){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.toString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"2");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+"-");
                }catch (NumberFormatException ex){
                    ex.printStackTrace(); 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje*/
                    if(!operation.get(0).equals("2")){
                        operation.add(0,"2");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+"-");
                    }
                }
            }
        }
    }
    class BmultiActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append("\u00D7");
                numValue="";
                operation.add(0,"3"); 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,"3");
                secDisplay.append("\u00D7"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& !operation.get(0).equals("0")){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.toString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"3");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+"\u00D7");
                }catch (NumberFormatException ex){
                    ex.printStackTrace(); 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje*/
                    if(!operation.get(0).equals("3")){
                        operation.add(0,"3");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+"\u00D7");
                    }
                }
            }
        }
    }
    class BdivActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append(":");
                numValue="";
                operation.add(0,"4"); 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,"4");
                secDisplay.append(":"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& !operation.get(0).equals("0")){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.setScale(prec,RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"4");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+":");
                }catch (NumberFormatException ex){
                    ex.printStackTrace(); 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje*/
                    if(!operation.get(0).equals("4")){
                        operation.add(0,"4");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+":");
                    }
                }
            }
        }
    }
    class nActList implements ActionListener{
        public void actionPerformed(ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append("^");
                numValue="";
                operation.add(0,"5"); 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                operation.add(0,"5");
                secDisplay.append("^"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& (!operation.get(0).equals("0"))){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.toString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"5");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+"^");
                }catch (NumberFormatException ex){ 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje lub numValue jest puste, w związku z pierwiastkowaniem*/
                    if(!operation.get(0).equals("5")){
                        operation.add(0,"5");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+"^");
                    }
                }
            }
        }
    }
    class percentActList implements ActionListener{
        public void actionPerformed(ActionEvent event){
            root=false;
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append("%");
                numValue="";
                operation.add(0,"7"); 
            /*w sytuacji, kiedy ktoś chce kontynuować liczenie na uzyskanym wyniku*/
            }else if(operation.get(0).equals("0")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,"7");
                secDisplay.append("%"); 
            /*w sytuacji, kiedy ktoś kontytuuje wpisywanie operacji bez użycia przycisku "="*/
            }else if((!operation.isEmpty())&& !operation.get(0).equals("0")){
                try{
                    memory.add(new BigDecimal(numValue));               // dodaje obecną wartość numValue do pamieci 
                    numValue="";                                        // czyści wartość numValue 
                    result = var.opEqual(memory, operation);            // przeprowadzenie operacje na zmiennych
                    mainDisplay.setText(result.toString());             // wyświetla wynik 
                    memory.removeAll(memory);                           //czyści pamięć, żeby wcześniej dodane liczby nie przeszkadzały 
                    memory.add(new BigDecimal(mainDisplay.getText()));  // dodaje wynik jako pierwsza liczbe w zbiorze 
                    operation.add(0,"7");                               // zaznacze żę osatanią operacją boło "=" 
                    secDisplay.setText("("+secDisplay.getText()+")"+"%");
                }catch (NumberFormatException ex){
                    ex.printStackTrace(); 
                    /*w sytuacji, kiedy user zmieni wykonywana operacje, wprowadza druga operacje*/
                    if(!operation.get(0).equals("7")){
                        operation.add(0,"7");
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+"%");
                    }
                }
            }
        }
    }

    /**
     * Listener pierwiastkowania to najbardziej odmienna metoda, spośród dostępnych działań. W przeciwieńśtwie do pozostałych 
     * operacji, nie wykorzystuje metody Operations do uzyskania wartości, ale robi to samodzielnie i podstawia wynik.
     * Spowodowało to sporo problemów: odmienna kolejnosć dodawania do pamięci, wyświetlanie, zaburzenie kolejnośći wprowadzania 
     * wartości do listy memory, brak operation, etc. Metoda odpowiada na nastepujące możliwości: 
     * - pierwiastkowanie wyniku wcześniejszych operacji, 
     * - pierwiastkowanie podanej wartości 
     * można przy tym wczejśniej okreśłić operację jaką wykona się na spierwiastkowanej liczbie. Pierwiastkowanie nie jest liczone 
     * jako odrębne działanie, jego wynik nie trafia do historii, bez użycia "pustego" =. 
     * Metoda jest rozbudowana w związku ze zróżńicowaniem sytuacji w jakich dodawany jest symbol pierwiastkowania, 
     * w zależności od tego kiedy i na czym jest ono przeprowadzane 
     */
    class rootActList implements ActionListener{
        public void actionPerformed (ActionEvent event){
            root=false;
            if((!operation.isEmpty()) && operation.get(0).equals("0")){    //jeżeli chcemy pierwiastkować wynik wczeąniejszych diałań 
                secDisplay.setText(mainDisplay.getText());                 //ten wiersz wysyła wynik z main do sec display, który później jest pobierany jako sqrt 
            }
            String sqrt=secDisplay.getText();
            try{
                BigDecimal squareRoot = new BigDecimal(Math.sqrt(Double.parseDouble(mainDisplay.getText()))).setScale(prec,RoundingMode.HALF_DOWN);

                for(int i=sqrt.length()-1; i>=0;i--){
                    char c = sqrt.charAt(i);
                    //jeżeli dokonano już pierwiastkowania na ostatniej liczbie
                    if((!Character.isDigit(c) && sqrt.substring(i,i+1).equals("\u221A"))){ //żeby nie dublowało znaku pierwiastka
                        sqrt = sqrt.substring(0,i+1)+mainDisplay.getText();
                        break;
                    }else if(!(Character.isDigit(c) || c=='.' || sqrt.substring(i,i+1).equals("\u221A"))){
                        sqrt=sqrt.substring(0,i+1)+"\u221A"+sqrt.substring(i+1);
                        break;
                    }else if(i==0 && Character.isDigit(c)){
                        sqrt="\u221A"+sqrt;
                        break;
                    }
                }
                secDisplay.setText(sqrt); 
                /*jeżeli ktoś kilkakrotnie przeprowadza pierwiastkowanie, na secDisplay zmienia się pierwiastkowana wartość*/
                if((!operation.isEmpty()) && operation.get(0).equals("6")){
                    secDisplay.setText("\u221A" + mainDisplay.getText());
                }
                mainDisplay.setText(squareRoot.setScale(prec, RoundingMode.HALF_UP).stripTrailingZeros().toString());
                numValue = mainDisplay.getText();
                root=true;
            }catch (NumberFormatException ex){          //póba pierwiastkowania liczby ujemnej 
                secDisplay.setText("Błąd: nieprawidłowe dane");
                operation.removeAll(operation);
                memory.removeAll(memory);
                numValue="";
                root=true;
            }
        }
    }
    class pmActList implements ActionListener{          //+- zmienia znak, 
        public void actionPerformed (ActionEvent event){
            root=false;
            String pm = numValue;
            if(numValue.charAt(0)=='-'){                //sprawdza znak wprowadzonej wartości i zwraca z odmienna 
                numValue=numValue.substring(1);
            }else{
                numValue="-"+numValue;
            }
            mainDisplay.setText(numValue);

            String s = secDisplay.getText();            //wprowadzanie odmiennego znaku do wyświetlacza pomocniczego, 
            secDisplay.setText(s.substring(0,s.length()-pm.length())+"("+numValue+")"); //różnie w zaleśności od miejsca w Stringu, 
            root=true;

        }
    }

    /**
     * Klawisze funkcyjne 
     */
    class CActList implements ActionListener{               //Kasowanie wprowadzonywa wartości, 
        public void actionPerformed (ActionEvent event){

            memory.removeAll(memory);
            operation.removeAll(operation);
            numValue="";
            secDisplay.setText("");
            mainDisplay.setText(numValue);
            root=false;
        }
    }
    class MCActList implements ActionListener{               //Kasowanie pamięci podręcznej i historii wyników 
        public void actionPerformed(ActionEvent event){
            memoryStore = new BigDecimal(0);
            sideTextField.setText("M: ");
            sideTextArea.setText("");

        }
    }
    class MSActList implements ActionListener{                 //wprowadzanie wartości do pamięci podręcznej 
        public void actionPerformed(ActionEvent event){
            memoryStore = new BigDecimal(mainDisplay.getText());
            sideTextField.setText("M: "+ mainDisplay.getText());


        }
    }
    class MRActList implements ActionListener{                  //przywoływanie wartości z pamięci roboczej, 
        public void actionPerformed(ActionEvent event){
            String s = secDisplay.getText();
            String m = memoryStore.toString();
            numValue=m;
            mainDisplay.setText(numValue);

            if(s.length()==0 ||(s.length()>0 && (!s.substring(s.length()-m.length()).equals(m)))){
                secDisplay.append(numValue);
            }

        }
    }

    /**
     * Opcje dostępne z menu: 
     */
    class wrapList implements ItemListener{                 //zawijanie wierszy w głownym wyświetlaczu 
        public void itemStateChanged(ItemEvent event){
            if(wrap.isSelected()){
                mainDisplay.setLineWrap(true);
            }else{
                mainDisplay.setLineWrap(false);
            }
        }
    }
    class precList implements ActionListener{                   //określanie ilośći wyświetlanych miejsc po przecinku 
        public void actionPerformed(ActionEvent event){
            try{                                                // wartość wprowadzana przez usera, mozę powodować wyjątki, 
                prec=Integer.parseInt(JOptionPane.showInputDialog(frame,"Ilość wyświetlanych miejsc po przecinku:\n"+prec+"\nZmień ilość miejsc po przecinku:"));
            }catch (NumberFormatException ex){                  // wartość wpisuje się w oknie dialogowym - wyświetla ono obecną wartość opcji, oraz miejsce do wpisania nowej - 'input' 
                ex.printStackTrace();
            }
        }
    }
    class infoActList implements ActionListener{                //wyświetla informajce, 
        public void actionPerformed(ActionEvent event){
            JOptionPane.showMessageDialog(frame,"Kalkulator v.2\nMarcin Cekiera\nKraków 2015\n\nJest to druga wersja mojego kalkulatora, najważniejszym odstępstwem,\n jest rezygnacja ze zmiennych \"double\" na rzecz \"BigDecimal\".","Informacja o programie",JOptionPane.INFORMATION_MESSAGE);

        }
    }
    class helpActList implements ActionListener{                //wyświetla pomoc 
        public void actionPerformed(ActionEvent event){
            JPanel helpPanel = new JPanel();
            JTextArea helpArea = new JTextArea(10,20);
            Font helpFont = new Font("New Times Roman", Font.PLAIN,10);
            helpArea.setFont(helpFont);
            helpArea.setLineWrap(true);
            helpArea.setWrapStyleWord(true);
            helpArea.setBorder(BorderFactory.createBevelBorder(4,Color.LIGHT_GRAY,Color.GRAY));

            helpArea.setSize(400,400);
            helpArea.setEditable(false);
            File helpFile = new File("src/help");
            try{
                BufferedReader reader = new BufferedReader(new FileReader(helpFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    helpArea.append(line+"\n");
                }

            }catch (Exception ex){
                secDisplay.setText("Error");
            }
            root=true;
            helpPanel.add(helpArea);
            JOptionPane.showMessageDialog(frame,helpArea,"Pomoc",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    class histList implements ActionListener{
        public void actionPerformed(ActionEvent event){         //czyni widzialnym panel z pamięcią i historią, 
            if(history.isSelected()){
                sidePanel.setVisible(true);
                frame.setSize(340,300);
            }else{
                sidePanel.setVisible(false);
                frame.setSize(250,300);
            }
        }
    }
} 
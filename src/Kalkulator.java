import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.*;

public class Kalkulator implements ActionListener{
    private JFrame frame;
    protected JPanel sidePanel;
    private JTextArea mainDisplay;       //main display, holds only recently introduced value
    private JTextArea secDisplay;        //secondary display, holds whole operation date, until use of "="
    private JTextArea sideTextArea;
    private JTextField sideTextField;

    private String numValue;
    private ArrayList<BigDecimal> memory;     //working memory of calculator
    private ArrayList<String> operation;      //list holding symbols of operations
    private BigDecimal result;
    private BigDecimal memoryStore;                  //function of memory, holds one value
    private Integer prec;
    private boolean root;
    private Logic num;

    private JCheckBoxMenuItem wrap;
    private JCheckBoxMenuItem history;
    private JMenuItem viewHelp;
    private JMenuItem info;
    private JMenuItem precision;

    public static void main(String[] args){
        Kalkulator calc = new Kalkulator();
        calc.go();
    }
    public void go(){

        numValue = "";                              //String with currently introduced values
        memory = new ArrayList<BigDecimal>();       //holds String with values for operations
        operation = new ArrayList<String>();        //holds symbols of operations
        prec = 15;                                  //decimal point accuracy,
        memoryStore = new BigDecimal(0);            // BigDecimal keeps one value in memory
        root=false;                                 // controls blocking of buttons
        num = new Logic();

        // GUI
        ImageIcon img = new ImageIcon(getClass().getResource("icon.png"));
        frame = new JFrame("Kalkulator");
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        mainPanel.setLayout(new GridLayout(6, 4));
        mainPanel.setBackground(Color.GRAY);

         /*menu*/
        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Opcje");
        JMenu help = new JMenu("Pomoc");
        viewHelp = new JMenuItem("Pomoc");
        viewHelp.addActionListener(new MenuActionListener());
        info = new JMenuItem("Informacje");
        info.addActionListener(new MenuActionListener());
        wrap = new JCheckBoxMenuItem("Zawijaj wiersze");
        wrap.addActionListener(new MenuActionListener());
        history = new JCheckBoxMenuItem("Historia wyników");
        history.addActionListener(new MenuActionListener());
        precision = new JMenuItem("Precyzja");
        precision.addActionListener(new MenuActionListener());

        menuBar.add(options);
        menuBar.add(help);
        options.add(wrap);
        options.add(history);
        options.add(precision);
        help.add(viewHelp);
        help.add(info);
        frame.setJMenuBar(menuBar);

        /*fonts*/
        Font mainDisplayFont = new Font("Ariala",Font.BOLD, 20);
        Font secDisplayFont = new Font("Ariala", Font.PLAIN, 10);
        Font buttonsFont = new Font("Ariala Black", Font.PLAIN,20);
        /*wyświetlacze*/
        mainDisplay = new JTextArea(2,10);
        mainDisplay.setFont(mainDisplayFont);
        mainDisplay.setText("");
        mainDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mainDisplay);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        secDisplay = new JTextArea(1,20);
        secDisplay.setFont(secDisplayFont);
        secDisplay.setText(numValue);
        secDisplay.setEditable(false);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.NORTH, displayPanel);
        displayPanel.add(BorderLayout.NORTH, secDisplay);
        displayPanel.add(BorderLayout.CENTER, scrollPane);
        displayPanel.setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.LIGHT_GRAY));

        //creation of buttons
        JButton[] jButtons = new JButton[24];
        String[] signs = {"C", "MC", "MS", "MR", "%", "\u221A", "x"+"\u207F", "\u00B1", "7", "8", "9",
                "+", "4", "5", "6", "\u00D7", "1", "2", "3", ":", "0", ".", "=", "-"};
        for(int i=0; i<24; i++){
            jButtons[i] = new JButton(signs[i]);
            jButtons[i].addActionListener(this);
            jButtons[i].setActionCommand(signs[i]);
            jButtons[i].setFont(buttonsFont);
            jButtons[i].setBorder(BorderFactory.createBevelBorder(1,Color.GRAY,Color.LIGHT_GRAY));
            mainPanel.add(jButtons[i]);
        }

        /*Results history - holds results of former operations*/
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

        frame.setSize(250, 325);
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event){
        Object source = event.getActionCommand();
        if(Character.isDigit(source.toString().charAt(0))){
            num.block();
            num.numButton(source.toString());
        }else if(source.equals("+")||source.equals("-")||source.equals("\u00D7")||source.equals(":")||source.equals("%")){
            num.numOperation(source.toString().substring(source.toString().length()-1));
        }else if(source.equals("x"+"\u207F")){
            num.numOperation("^");
        }else if(source.equals("=")){
            root=false;
            if(numValue.equals("0") && operation.get(0).equals(":")){           //attempt to divide by "O"
                secDisplay.setText("Błąd: nie można dzielić przez \"0\"");
                operation.removeAll(operation);
                memory.removeAll(memory);
                numValue="";
                root=true;
            }else if(operation.isEmpty() || operation.get(0).equals("=")){       //unnecessary use of "=" button
                sideTextArea.append("\u00BB"+mainDisplay.getText()+"\n");        //send display content to history
            }else{
                try{                                                            //standard "=" operation
                    memory.add(new BigDecimal(numValue));
                    numValue="";
                    result = num.opEqual(memory, operation);
                    mainDisplay.setText(result.setScale(prec,RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString());

                    operation.add(0,"=");
                    memory.removeAll(memory);
                    secDisplay.setText("");
                    sideTextArea.append("\u00BB"+mainDisplay.getText()+"\n");

                /*if user will try to get result, after unnecessary usage of operation button(+,-,etc.*/
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
        }else if(source.equals(".")){
            num.block();
            boolean point = false;
            for(int i=numValue.length()-1;i>=0;i--){
                if(numValue.substring(i,i+1).equals(".")){
                    point=true;
                }
            }
            if(numValue.equals("")){
                num.numButton("0.");
            }else if(point){
                //intentionally blank
            }else{
                num.numButton(".");
            }
        }else if(source.equals("\u221A")){
            root=false;
            if((!operation.isEmpty()) && operation.get(0).equals("=")){    //if user want to use root one result of former operations
                secDisplay.setText(mainDisplay.getText());
            }
            String sqrt=secDisplay.getText();
            try{
                BigDecimal squareRoot = new BigDecimal(Math.sqrt(Double.parseDouble(mainDisplay.getText()))).setScale(prec,RoundingMode.HALF_DOWN);

                for(int i=sqrt.length()-1; i>=0;i--){
                    char c = sqrt.charAt(i);
                    //root on already rooted number
                    if((!Character.isDigit(c) && sqrt.substring(i,i+1).equals("\u221A"))){ //to avoid root symbol doubling
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
                /*changing a value on secondary display after use root one before rooted number*/
                if((!operation.isEmpty()) && operation.get(0).equals("\u221A")){
                    secDisplay.setText("\u221A" + mainDisplay.getText());
                }
                mainDisplay.setText(squareRoot.setScale(prec, RoundingMode.HALF_UP).stripTrailingZeros().toString());
                numValue = mainDisplay.getText();
                root=true;
            }catch (NumberFormatException ex){          //attempt to root negative number
                secDisplay.setText("Błąd: nieprawidłowe dane");
                operation.removeAll(operation);
                memory.removeAll(memory);
                numValue="";
                root=true;
            }
        }else if(source.equals("\u00B1")){
            root=false;
            if(numValue.charAt(0)=='-'){
                numValue=numValue.substring(1);
            }else{
                numValue="-"+numValue;
            }
            mainDisplay.setText(numValue);

            String s = secDisplay.getText();            //adding sign to secondary display
            if(numValue.substring(0,1).equals("-")){
                secDisplay.setText(s.substring(0,s.length()-(numValue.length()-1))+"("+numValue+")");
            }else{
                secDisplay.setText(s.substring(0,s.length()-(numValue.length()+3))+numValue);
            }
            root=true;
        }else if(source.equals("C")){
            memory.removeAll(memory);
            operation.removeAll(operation);
            numValue="";
            secDisplay.setText("");
            mainDisplay.setText(numValue);
            root=false;
        }else if(source.equals("MC")){
            memoryStore = new BigDecimal(0);
            sideTextField.setText("M: ");
            sideTextArea.setText("");
        }else if(source.equals("MS")){
            memoryStore = new BigDecimal(mainDisplay.getText());
            sideTextField.setText("M: "+ mainDisplay.getText());
        }else if(source.equals("MR")){
            String s = secDisplay.getText();
            String m = memoryStore.toString();
            numValue=m;
            mainDisplay.setText(numValue);
            if(!(s.length()>=m.length() && (s.substring(s.length()-m.length()).equals(m)))){
                secDisplay.append(numValue);
            }
        }
    }
    class Logic{
        void numButton(String s){            //reaction for use of button: adding number to displays and memory
            numValue+=s;
            mainDisplay.setText(numValue);
            secDisplay.append(s);
            root=false;                             //unblocking of buttons block
        }
        void numOperation(String s){       //controls operation on numbers, without math
            root=false;                                     // s - symbol of operation (=,+,-,*,/,^,6%);
            if(operation.isEmpty()){
                memory.add(new BigDecimal(numValue));
                secDisplay.append(s);
                numValue="";
                operation.add(0,s);
                /*if user want to continue counting on result of former operations*/
            }else if(operation.get(0).equals("=")){
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue="";
                operation.add(0,s);
                secDisplay.append(s);
                /*if user do more than one operation, without usage of "=" - displays mid-result after every operation*/
            }else if((!operation.isEmpty())&& (!operation.get(0).equals("="))){
                try{
                    memory.add(new BigDecimal(numValue));
                    numValue="";
                    result = opEqual(memory, operation);
                    mainDisplay.setText(result.toString());
                    memory.removeAll(memory);
                    memory.add(new BigDecimal(mainDisplay.getText()));
                    operation.add(0,s);
                    secDisplay.setText("("+secDisplay.getText()+")"+s);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                        /*if user decide to change current operation - change symbol*/
                    if(!operation.get(0).equals(s)){
                        operation.add(0,s);
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0,error.length()-1)+s);
                    }
                }
            }
        }
        void block(){     //if buttons are blocked(there is final value on display) use of next button cause a display reset
            if(root){
                secDisplay.setText("");
                mainDisplay.setText("");
                numValue="";
            }
        }
        public BigDecimal opEqual(ArrayList<BigDecimal> list, ArrayList<String> s){         //operations on numbers
            BigDecimal temp = new BigDecimal(0);
            char op = s.get(0).charAt(0);
            switch(op){
                case '+':
                    temp = list.get(0).add(list.get(1));
                    break;
                case '-':
                    temp = list.get(0).subtract(list.get(1));
                    break;
                case '\u00D7':
                    temp = list.get(0).multiply(list.get(1));
                    break;
                case ':':
                    temp = list.get(0).divide(list.get(1), prec, RoundingMode.HALF_DOWN).stripTrailingZeros();
                    break;
                case '^':
                    temp = list.get(0).pow(list.get(1).intValue());
                    break;
                case '%':
                    temp = list.get(0).divide(new BigDecimal("100")).multiply(list.get(1));
                    break;
            }
            return temp;
        }
    }
    class MenuActionListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Object source = event.getSource();
            if(source == wrap){
                if(wrap.isSelected()){
                    mainDisplay.setLineWrap(true);
                }else{
                    mainDisplay.setLineWrap(false);
                }
            }else if(source == history){
                if(history.isSelected()){
                    sidePanel.setVisible(true);
                    frame.setSize(340,325);
                }else{
                    sidePanel.setVisible(false);
                    frame.setSize(250,325);
                }
            }else if(source == viewHelp){
                JPanel helpPanel = new JPanel();
                JTextArea helpArea = new JTextArea(10,20);
                Font helpFont = new Font("New Times Roman", Font.PLAIN,10);
                helpArea.setFont(helpFont);
                helpArea.setLineWrap(true);
                helpArea.setWrapStyleWord(true);
                helpArea.setBorder(BorderFactory.createBevelBorder(4,Color.LIGHT_GRAY,Color.GRAY));
                helpArea.setSize(400,400);
                helpArea.setEditable(false);
                InputStream is = getClass().getResourceAsStream("help.txt");
                InputStreamReader isr = new InputStreamReader(is);

                try{
                    Reader reader = new InputStreamReader(is,"UTF-8");
                    while (true) {
                        char c = (char) reader.read();
                        if(c=='`'){
                            break;
                        }
                        helpArea.append(Character.toString(c));
                    }
                    reader.close();
                    isr.close();
                    is.close();

                }catch (Exception ex){
                    secDisplay.setText("Błąd: nie znaleziono pliku");
                }

                root=true;
                helpPanel.add(helpArea);
                JOptionPane.showMessageDialog(frame,helpArea,"Pomoc",JOptionPane.INFORMATION_MESSAGE);

            }else if(source == info){
                JOptionPane.showMessageDialog(frame,"Kalkulator\n Kraków 02.2015\n \nProsty kalkulator do podstawowych zastosowań.","O programie",JOptionPane.INFORMATION_MESSAGE);
            }else if(source == precision){
                try{
                    prec=Integer.parseInt(JOptionPane.showInputDialog(frame,"Wyświetlana ilość miejsc po przecinku:\n"+prec+"\nZmień wyświetlaną ilość miejsc po przecinku:"));
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}

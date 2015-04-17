import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Kalkulator implements ActionListener {
    private JPanel sidePanel;
    private JPanel mainPanel;
    private JFrame frame;
    private JPanel displayPanel;
    private JTextArea mainDisplay;       //main display, holds only recently introduced value
    private JTextArea secDisplay;        //secondary display, holds whole operation date, until use of "="
    protected JTextArea orderDisplay;
    public JTextArea secOrderDisplay;
    private JTextArea sideTextArea;
    private JTextField sideTextField;
    private String numValue;
    private ArrayList<BigDecimal> memory;     //working memory of calculator
    private ArrayList<String> operationList;      //list holding symbols of operations


    private BigDecimal result;
    protected boolean buttonBlock;

    private BigDecimal memoryStorage;                  //function of memory, holds one value
    public static Integer decimalPoint;
    private JCheckBoxMenuItem wrapLines;
    private JCheckBoxMenuItem history;
    private JCheckBoxMenuItem equitationMode;
    private JMenuItem viewHelp;
    private JMenuItem viewInfo;
    private JMenuItem decimalPrecision;
    private JScrollPane scrollPane;
    private JScrollPane scrollOrderPane;
    protected JButton[] jButtons;

    public static void main(String[] args) {
        Kalkulator calc = new Kalkulator();
        calc.buildGUI();
    }

    public void buildGUI() {

        numValue = "";                              //String with currently introduced values
        memory = new ArrayList<BigDecimal>();       //holds String with values for operations
        operationList = new ArrayList<String>();        //holds symbols of operations
        decimalPoint = 15;                                  //decimal point accuracy,
        memoryStorage = new BigDecimal(0);            // BigDecimal keeps one value in memory
        buttonBlock = false;                                 // controls blocking of buttons

        // GUI
        ImageIcon img = new ImageIcon(getClass().getResource("icon.png"));
        frame = new JFrame("Kalkulator");
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

         /*menu*/
        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Opcje");
        JMenu help = new JMenu("Pomoc");
        viewHelp = new JMenuItem("Pomoc");
        viewHelp.addActionListener(new MenuActionListener());
        viewInfo = new JMenuItem("Informacje");
        viewInfo.addActionListener(new MenuActionListener());
        wrapLines = new JCheckBoxMenuItem("Zawijaj wiersze");
        wrapLines.addActionListener(new MenuActionListener());
        history = new JCheckBoxMenuItem("Historia wyników");
        history.addActionListener(new MenuActionListener());
        equitationMode = new JCheckBoxMenuItem("Kolejność działań");
        equitationMode.addActionListener(new MenuActionListener());
        decimalPrecision = new JMenuItem("Precyzja");
        decimalPrecision.addActionListener(new MenuActionListener());


        menuBar.add(options);
        menuBar.add(help);
        options.add(wrapLines);
        options.add(history);
        options.add(equitationMode);
        options.add(decimalPrecision);
        help.add(viewHelp);
        help.add(viewInfo);
        frame.setJMenuBar(menuBar);

        /*fonts*/
        Font mainDisplayFont = new Font("Ariala", Font.BOLD, 20);
        Font secDisplayFont = new Font("Ariala", Font.PLAIN, 10);

        /*wyświetlacze*/
        mainDisplay = new JTextArea(2, 10);
        mainDisplay.setFont(mainDisplayFont);
        mainDisplay.setText("");
        mainDisplay.setEditable(false);

        orderDisplay = new JTextArea(2, 10);
        orderDisplay.setFont(mainDisplayFont);
        orderDisplay.setText("");
        orderDisplay.setEditable(true);

        scrollPane = new JScrollPane(mainDisplay);
        scrollOrderPane = new JScrollPane(orderDisplay);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollOrderPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollOrderPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        secDisplay = new JTextArea(1, 20);
        secDisplay.setFont(secDisplayFont);
        secDisplay.setText(numValue);
        secDisplay.setEditable(false);

        secOrderDisplay = new JTextArea(1, 20);
        secOrderDisplay.setFont(secDisplayFont);
        secOrderDisplay.setText(numValue);
        secOrderDisplay.setEditable(false);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.NORTH, displayPanel);
        displayPanel.add(BorderLayout.NORTH, secDisplay);
        displayPanel.add(BorderLayout.CENTER, scrollPane);

        displayPanel.setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.LIGHT_GRAY));

        createMainPanel();

        /*Results history - holds results of former operations*/
        sidePanel = new JPanel();
        JPanel sideTextPanel = new JPanel();
        sideTextPanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.WHITE);
        sideTextField = new JTextField("M: ", 7);
        sideTextArea = new JTextArea(11, 7);
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
        frame.getContentPane().add(BorderLayout.EAST, sidePanel);
        sidePanel.setVisible(false);

        frame.setSize(250, 325);
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void createMainPanel() {
        mainPanel = new JPanel();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        mainPanel.setLayout(new GridLayout(6, 4));
        mainPanel.setBackground(Color.GRAY);
        Font buttonsFont = new Font("Ariala Black", Font.PLAIN, 20);
        String percentOpen;
        String plusminusClose;
        String MCback;
        if (equitationMode.isSelected()) {
            percentOpen = "(";
            plusminusClose = ")";
            MCback = "\u2190";
        } else {
            percentOpen = "%";
            plusminusClose = "\u00B1";
            MCback = "MC";
        }
        jButtons = new JButton[24];
        String[] signs = {"C", MCback, "MS", "MR", percentOpen, plusminusClose, "x" + "\u207F", "\u221A", "7", "8", "9",
                "+", "4", "5", "6", "\u00D7", "1", "2", "3", ":", "0", ".", "=", "-", "\u2b05"};
        for (int i = 0; i < 24; i++) {
            jButtons[i] = new JButton(signs[i]);
            jButtons[i].addActionListener(this);
            jButtons[i].setActionCommand(signs[i]);
            jButtons[i].setFont(buttonsFont);
            jButtons[i].setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.LIGHT_GRAY));
            mainPanel.add(jButtons[i]);
        }
    }

    public void actionPerformed(ActionEvent event) {
        Logic num = new Logic();
        Object source = event.getActionCommand();
        if (Character.isDigit(source.toString().charAt(0))) {
            num.buttonsBlock();
            num.pressNumericButton(source.toString());
        } else if (source.equals("+") || source.equals("-") || source.equals("\u00D7") || source.equals(":") || source.equals("%")) {
            num.pressOperatorButton(source.toString().substring(source.toString().length() - 1));
        } else if (source.equals("x" + "\u207F")) {
            num.pressOperatorButton("^");
        } else if (source.equals("=")) {
            buttonBlock = false;
            OrderOfOperations order = new OrderOfOperations();
            secOrderDisplay.setText(orderDisplay.getText());
            orderDisplay.setText(order.findBrackets(orderDisplay.getText()));

            if (numValue.equals("0") && operationList.get(0).equals(":")) {           //attempt to divide by "O"
                secDisplay.setText("Błąd: nie można dzielić przez \"0\"");
                secOrderDisplay.setText("Błąd: nie można dzielić przez \"0\"");
                operationList.removeAll(operationList);
                memory.removeAll(memory);
                numValue = "";
                buttonBlock = true;
            } else if (operationList.isEmpty() || operationList.get(0).equals("=")) {       //unnecessary use of "=" button
                sideTextArea.append("\u00BB" + mainDisplay.getText() + "\n");        //send display content to history
            } else {
                try {                                                            //standard "=" operation
                    memory.add(new BigDecimal(numValue));
                    numValue = "";
                    result = num.giveResult(memory.get(0), memory.get(1), operationList.get(0));
                    mainDisplay.setText(result.setScale(decimalPoint, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString());

                    operationList.add(0, "=");
                    memory.removeAll(memory);
                    secDisplay.setText("");
                    sideTextArea.append("\u00BB" + mainDisplay.getText() + "\n");

                /*if user will try to get result, after unnecessary usage of operation button(+,-,etc.*/
                } catch (Exception ex) {
                    sideTextArea.append("\u00BB" + mainDisplay.getText() + "\n");
                    if (ex instanceof NumberFormatException) {
                        memory.removeAll(memory);
                        operationList.removeAll(operationList);
                        numValue = mainDisplay.getText();
                        secDisplay.setText(numValue);
                    } else if (ex instanceof IndexOutOfBoundsException) {
                        memory.removeAll(memory);
                        operationList.removeAll(operationList);
                        numValue = mainDisplay.getText();
                    }
                }
            }
            if (equitationMode.isSelected()) {
                buttonBlock = true;
            }
        } else if (source.equals(".")) {
            num.buttonsBlock();
            boolean point = false;
            for (int i = numValue.length() - 1; i >= 0; i--) {
                if (numValue.substring(i, i + 1).equals(".")) {
                    point = true;
                }
            }
            if (numValue.equals("")) {
                num.pressNumericButton("0.");
            } else if (point) {
                //intentionally blank
            } else {
                num.pressNumericButton(".");
            }
        } else if (source.equals("\u221A")) {           //root
            if(orderDisplay.getText().length()>0 && orderDisplay.getText().substring(orderDisplay.
                    getText().length()-1).equals("\u221A")){
            }else{
                orderDisplay.append("\u221A");
            }
            buttonBlock = false;

            if ((!operationList.isEmpty()) && operationList.get(0).equals("=")) {    //if user want to use root one result of former operations
                secDisplay.setText(mainDisplay.getText());
            }
            String sqrt = secDisplay.getText();
            try {
                BigDecimal squareRoot = new BigDecimal(Math.sqrt(Double.parseDouble(mainDisplay.getText()))).setScale(decimalPoint, RoundingMode.HALF_DOWN);

                for (int i = sqrt.length() - 1; i >= 0; i--) {
                    char c = sqrt.charAt(i);
                    //root on already rooted number
                    if ((!Character.isDigit(c) && sqrt.substring(i, i + 1).equals("\u221A"))) { //to avoid root symbol doubling
                        sqrt = sqrt.substring(0, i + 1) + mainDisplay.getText();
                        System.out.println("1");
                        break;
                    } else if (!(Character.isDigit(c) || c == '.' || sqrt.substring(i, i + 1).equals("\u221A"))) {
                        sqrt = sqrt.substring(0, i + 1) + "\u221A" + mainDisplay.getText();
                        System.out.println("2");
                        break;
                    } else if (i == 0 && Character.isDigit(c)) {
                        sqrt = "\u221A" + sqrt;
                        System.out.println("3");
                        break;
                    }
                }
                secDisplay.setText(sqrt);
                /*changing a value on secondary display after use root one before rooted number*/
                if ((!operationList.isEmpty()) && operationList.get(0).equals("\u221A")) {
                    secDisplay.setText("\u221A" + mainDisplay.getText());
                }
                mainDisplay.setText(squareRoot.setScale(decimalPoint, RoundingMode.HALF_UP).stripTrailingZeros().toString());
                numValue = mainDisplay.getText();
            } catch (NumberFormatException ex) {          //attempt to root negative number
                secDisplay.setText("Błąd: nieprawidłowe dane");
                mainDisplay.setText("");
                operationList.removeAll(operationList);
                memory.removeAll(memory);
                numValue = "";
            }
            if (!equitationMode.isSelected()) {
                buttonBlock = true;
            }
        } else if (source.equals("\u00B1")) {
            buttonBlock = false;
            if (numValue.charAt(0) == '-') {
                numValue = numValue.substring(1);
            } else {
                numValue = "-" + numValue;
            }
            mainDisplay.setText(numValue);

            String s = secDisplay.getText();            //adding sign to secondary display
            if (numValue.substring(0, 1).equals("-")) {
                secDisplay.setText(s.substring(0, s.length() - (numValue.length() - 1)) + "(" + numValue + ")");
            } else {
                secDisplay.setText(s.substring(0, s.length() - (numValue.length() + 3)) + numValue);
            }
            buttonBlock = true;
        } else if (source.equals("C")) {
            memory.removeAll(memory);
            operationList.removeAll(operationList);
            numValue = "";
            secDisplay.setText("");
            secOrderDisplay.setText("");
            mainDisplay.setText(numValue);
            orderDisplay.setText("");
            buttonBlock = false;
        } else if (source.equals("MC")) {
            memoryStorage = new BigDecimal(0);
            sideTextField.setText("M: ");
            sideTextArea.setText("");
        } else if (source.equals("MS")) {
            if (equitationMode.isSelected()) {
                memoryStorage = new BigDecimal(orderDisplay.getText());
            } else {
                memoryStorage = new BigDecimal(mainDisplay.getText());
            }
            sideTextField.setText("M: " + mainDisplay.getText());
        } else if (source.equals("MR")) {
            String s = secDisplay.getText();
            String m = memoryStorage.toString();
            numValue = m;
            if (equitationMode.isSelected()) {
                orderDisplay.setText(numValue);
            } else {
                mainDisplay.setText(numValue);
                if (!(s.length() >= m.length() && (s.substring(s.length() - m.length()).equals(m)))) {
                    secDisplay.append(numValue);
                }
            }
        } else if (source.equals("(")) {
            orderDisplay.append("(");
        } else if (source.equals(")")) {
            orderDisplay.append(")");
        } else if (source.equals("\u2190")) {
            try {
                orderDisplay.setText(orderDisplay.getText().substring(0, orderDisplay.getText().length() - 1));
            } catch (Exception ex) {
                ex.printStackTrace();
                orderDisplay.setText("");
            }
        }
    }

    class Logic {
        void pressNumericButton(String s) {            //reaction for use of button: adding number to displays and memory
            numValue += s;
            mainDisplay.setText(numValue);
            secDisplay.append(s);
            buttonBlock = false;                             //unblocking of buttons block
            orderDisplay.append(s);
        }

        void pressOperatorButton(String s) {       //controls operation on numbers, without math
            orderDisplay.append(s);
            buttonBlock = false;                                     // s - symbol of operation (=,+,-,*,/,^,6%);
            if (operationList.isEmpty()) {
                memory.add(new BigDecimal(numValue));
                secDisplay.append(s);
                numValue = "";
                operationList.add(0, s);
                /*if user want to continue counting on result of former operations*/
            } else if (operationList.get(0).equals("=")) {
                memory.add(new BigDecimal(mainDisplay.getText()));
                secDisplay.setText(mainDisplay.getText());
                numValue = "";
                operationList.add(0, s);
                secDisplay.append(s);
                /*if user do more than one operation, without usage of "=" - displays mid-result after every operation*/
            } else if ((!operationList.isEmpty()) && (!operationList.get(0).equals("="))) {
                try {
                    memory.add(new BigDecimal(numValue));
                    numValue = "";
                    result = giveResult(memory.get(0), memory.get(1), operationList.get(0));
                    mainDisplay.setText(result.toString());
                    memory.removeAll(memory);
                    memory.add(new BigDecimal(mainDisplay.getText()));
                    operationList.add(0, s);
                    secDisplay.setText("(" + secDisplay.getText() + ")" + s);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                        /*if user decide to change current operation - change symbol*/
                    if (!operationList.get(0).equals(s)) {
                        operationList.add(0, s);
                        String error = secDisplay.getText();
                        secDisplay.setText(error.substring(0, error.length() - 1) + s);
                    }
                }
            }

        }

        void buttonsBlock() {     //if buttons are blocked(there is final value on display) use of next button cause a display reset
            if (buttonBlock) {
                secDisplay.setText("");
                mainDisplay.setText("");
                orderDisplay.setText("");
                secOrderDisplay.setText("");
                numValue = "";
            }
        }

        public BigDecimal giveResult(BigDecimal one, BigDecimal two, String s) {         //operations on numbers
            BigDecimal temp = new BigDecimal(0);
            char op = s.charAt(0);
            switch (op) {
                case '+':
                    temp = one.add(two);
                    break;
                case '-':
                    temp = one.subtract(two);
                    break;
                case '\u00D7':
                    temp = one.multiply(two);
                    break;
                case ':':
                    temp = one.divide(two, decimalPoint, RoundingMode.HALF_DOWN).stripTrailingZeros();
                    break;
                case '^':
                    temp = one.pow(two.intValue());
                    break;
                case '%':
                    temp = one.divide(new BigDecimal("100")).multiply(two);
                    break;
                case '\u221A':
                    temp = new BigDecimal(Math.sqrt(Double.parseDouble(two.toString())));
                    break;

            }
            return temp;
        }
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == wrapLines) {
                if (wrapLines.isSelected()) {
                    mainDisplay.setLineWrap(true);
                    orderDisplay.setLineWrap(true);
                } else {
                    mainDisplay.setLineWrap(false);
                    orderDisplay.setLineWrap(true);
                }
            } else if (source == history) {
                if (history.isSelected()) {
                    sidePanel.setVisible(true);
                    frame.setSize(340, 325);
                } else {
                    sidePanel.setVisible(false);
                    frame.setSize(250, 325);
                }
            } else if (source == viewHelp) {
                JPanel helpPanel = new JPanel();
                JTextArea helpArea = new JTextArea(10, 20);
                JScrollPane helpPane = new JScrollPane(helpArea);
                helpPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                helpPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                Font helpFont = new Font("New Times Roman", Font.PLAIN, 10);
                helpArea.setFont(helpFont);
                helpArea.setLineWrap(true);
                helpArea.setWrapStyleWord(true);
                helpArea.setBorder(BorderFactory.createBevelBorder(4, Color.LIGHT_GRAY, Color.GRAY));
                helpArea.setSize(400, 200);
                helpArea.setEditable(false);
                InputStream is = getClass().getResourceAsStream("help.txt");
                InputStreamReader isr = new InputStreamReader(is);

                try {
                    Reader reader = new InputStreamReader(is, "UTF-8");
                    while (true) {
                        char c = (char) reader.read();
                        if (c == '`') {
                            break;
                        }
                        helpArea.append(Character.toString(c));
                    }
                    reader.close();
                    isr.close();
                    is.close();

                } catch (Exception ex) {
                    secDisplay.setText("Błąd: nie znaleziono pliku");
                }

                buttonBlock = true;
                helpPanel.add(helpPane);
                JOptionPane.showMessageDialog(frame, helpPane, "Pomoc", JOptionPane.INFORMATION_MESSAGE);

            } else if (source == viewInfo) {
                JOptionPane.showMessageDialog(frame, "Kalkulator\n Kraków 02.2015\n \nProsty kalkulator do podstawowych zastosowań.", "O programie", JOptionPane.INFORMATION_MESSAGE);
            } else if (source == decimalPrecision) {
                try {
                    decimalPoint = Integer.parseInt(JOptionPane.showInputDialog(frame, "Wyświetlana ilość miejsc po przecinku:\n" + decimalPoint + "\nZmień wyświetlaną ilość miejsc po przecinku:", "Precyzja", JOptionPane.QUESTION_MESSAGE));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            } else if (source == equitationMode) {
                if (equitationMode.isSelected()) {
                    displayPanel.remove(secDisplay);
                    displayPanel.remove(scrollPane);
                    frame.getContentPane().remove(mainPanel);
                    createMainPanel();
                    displayPanel.add(BorderLayout.NORTH, secOrderDisplay);
                    displayPanel.add(BorderLayout.CENTER, scrollOrderPane);
                    buttonBlock = true;
                    frame.revalidate();
                    frame.repaint();
                    secOrderDisplay.setText("Wprowadź działanie:");

                } else {
                    displayPanel.remove(secOrderDisplay);
                    displayPanel.remove(scrollOrderPane);
                    frame.getContentPane().remove(mainPanel);
                    displayPanel.add(BorderLayout.NORTH, secDisplay);
                    displayPanel.add(BorderLayout.CENTER, scrollPane);
                    createMainPanel();
                    buttonBlock = true;
                    frame.revalidate();
                    frame.repaint();
                    numValue = "";
                }
            }
        }
    }

    class OrderOfOperations {

        public String findBrackets(String line) {             //method which deal with brackets separately
            while (line.contains(Character.toString('(')) || line.contains(Character.toString(')'))) {
                for (int forward = 0; forward < line.length(); forward++) {                           //i there is not sign
                    try {
                        if ((line.charAt(forward) == ')' || Character.isDigit(line.charAt(forward))) //between separate findBrackets
                                && (forward < line.length() - 1 && line.charAt(forward + 1) == '(')) {                         //or number and bracket,
                            line = line.substring(0, forward + 1) + "\u00D7" + (line.substring(forward + 1));        //it treat it as
                        }                                                       //a multiplication
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (line.charAt(forward) == ')') {                                  //search for a forward bracket
                        for (int back = forward; back >= 0; back--) {
                            if (line.charAt(back) == '(') {                          //search for a opening bracket
                                String in = line.substring(back + 1, forward);
                                in = findElements(in);
                                line = line.substring(0, back) + in + line.substring(forward + 1);
                                back = forward = 0;
                            }
                        }
                    }
                }
                if (line.contains(Character.toString('(')) || line.contains(Character.toString(')')) ||
                        line.contains(Character.toString('(')) || line.contains(Character.toString(')'))) {
                    secOrderDisplay.setText("Błąd: nieprawidłowo rozmieszczone nawiasy");
                    return "0";
                }
            }

            line = findElements(line);
            return line;
        }

        public String findElements(String line) {              //method divide String on numbers and operators
            ArrayList<String> elementsList = new ArrayList<String>();         //holds numbers and operators
            String item = "";
            for (int elementIndex = line.length() - 1; elementIndex >= 0; elementIndex--) {           //is scan String from right to left,
                if (Character.isDigit(line.charAt(elementIndex))) {     //Strings are added to list, if scan finds
                    item = line.charAt(elementIndex) + item;              //a operator, or beginning of String
                    if (elementIndex == 0) {
                        elementsList.add(0, item);
                    }
                } else {
                    if (line.charAt(elementIndex) == ' ') {
                    } else if (line.charAt(elementIndex) == '.') {
                        item = line.charAt(elementIndex) + item;
                    } else if (line.charAt(elementIndex) == '-' && (elementIndex == 0 || (!Character.isDigit(line.charAt(elementIndex - 1))))) {
                        item = line.charAt(elementIndex) + item;          //this part should findElements
                        elementsList.add(0, item);                    //negative numbers
                        item = "";
                    } else {
                        if (!item.equals("")) {
                            elementsList.add(0, item);
                        }
                        elementsList.add(0, Character.toString(line.charAt(elementIndex)));     //it add already formed number and
                        item = "";                                                             //operators to list
                        if (line.charAt(elementIndex) == '\u221A') {       //add empty String to list, before "\u221A" sign,
                            elementsList.add(0, " ");          //to avoid removing of any meaningful String
                            //in last part of giveResult method
                        }
                    }
                }
            }

            elementsList = giveResult(elementsList, "^", "\u221A");    //check Strings
            elementsList = giveResult(elementsList, "\u00D7", ":");    //for chosen
            elementsList = giveResult(elementsList, "+", "-");    //operators
            return elementsList.get(0);
        }

        public ArrayList<String> giveResult(ArrayList<String> arrayList, String op1, String op2) {
            Kalkulator.Logic operations = new Kalkulator().new Logic();
            BigDecimal result;
            try {
                for (int index = 0; index < arrayList.size(); index++) {
                    if (arrayList.get(index).equals(op1) || arrayList.get(index).equals(op2)) {
                        BigDecimal one;
                        if (arrayList.get(index - 1).equals(" ")) {
                            one = new BigDecimal(0);
                        } else {
                            one = new BigDecimal(arrayList.get(index - 1));
                        }
                        BigDecimal two = new BigDecimal(arrayList.get(index + 1));
                        if (arrayList.get(index).equals(":") && two.toString().equals("0")) {

                            arrayList.removeAll(arrayList);
                            arrayList.add("0");
                            return arrayList;

                        } else {
                            result = operations.giveResult(one, two, arrayList.get(index));
                        }
                        try {       //in a case of to "out of range" ex
                            arrayList.set(index, (result.setScale(decimalPoint, RoundingMode.HALF_DOWN).
                                    stripTrailingZeros().toPlainString()));
                            arrayList.remove(index + 1);            //it replace the operator with giveResult
                            arrayList.remove(index - 1);              //and remove used numbers from list
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    } else {
                        continue;            //
                    }
                    index = 0;                     //loop reset, as arrayList changed size
                }
            } catch (Exception ex) {
                orderDisplay.setText("");
                secOrderDisplay.setText("Błąd: niewłaściwe dane");
                ex.printStackTrace();
                arrayList.add(0, "0");

            }
            return arrayList;
        }
    }
}



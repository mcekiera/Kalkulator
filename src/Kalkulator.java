import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


public class Kalkulator {
    JFrame frame;
    JPanel background;
    JTextArea area;
    String workingMemory1; // druga pamiec robocza, przechowywuje wprowadzone wartości
    String workingMemory2; // glowna pamiec robocza, do niej w pierwszej kolejnosci wprowadza sie liczby
    int op;                // decyduje o tym jakie działanie zostanie zastosowana z NASTEPNA liczba
    NumberFormat nf;
    double r;

    public static void main(String[] args){
        Kalkulator kalkulator = new Kalkulator();
        kalkulator.run();

    }
    public void run(){
        workingMemory1 = "";
        workingMemory2 = "";
        frame = new JFrame("KALKULEJTER 2016");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        background = new JPanel();
        background.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JPanel display = new JPanel();
        JPanel down = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");
        JMenu exit = new JMenu("Exit");


        JMenuItem New = new JMenuItem("New");
        JMenuItem Open = new JMenuItem("Open");
        JMenuItem Exit = new JMenuItem(("Exit"));
        Exit.addActionListener(new exitListener());
        file.add(New);
        file.add(Open);
        file.add(Exit);


        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(help);
        menuBar.add(exit);
        frame.setJMenuBar(menuBar);
        file.setMnemonic(KeyEvent.VK_F);


        GridLayout grid = new GridLayout(4,4);
        panel.setLayout(grid);

        Font font = new Font("Ariala", Font.BOLD, 20);
        DecimalFormatSymbols point = new DecimalFormatSymbols();
        point.setDecimalSeparator('.');
        nf = new DecimalFormat("#.#", point);

        area = new JTextArea(1,10);
        area.setFont(font);
        display.add(area);
        display.setBackground(Color.WHITE);
        down.setLayout(new GridLayout(1,2));
        area.setEditable(false);

        frame.getContentPane().add(background);
        frame.getContentPane().add(BorderLayout.SOUTH, down);

        JButton C = new JButton("C");
        C.addActionListener(new Click());
        down.add(C);

        JButton rootSquere = new JButton("\u221A");
        rootSquere.addActionListener(new rootClick());
        down.add(rootSquere);

        JButton square = new JButton("^2");
        square.addActionListener(new squareClick());
        down.add(square);

        JButton plusminus = new JButton("\u2213");
        plusminus.addActionListener(new pmClick());
        down.add(plusminus);

        JButton B9 = new JButton("9");
        B9.addActionListener(new B9click());
        panel.add(B9);

        JButton B8 = new JButton("8");
        B8.addActionListener(new B8click());
        panel.add(B8);

        JButton B7 = new JButton("7");
        B7.addActionListener(new B7click());
        panel.add(B7);

        JButton Bequal = new JButton("=");
        Bequal.addActionListener(new BequalClick());
        panel.add(Bequal);

        JButton B6 = new JButton("6");
        B6.addActionListener(new B6click());
        panel.add(B6);

        JButton B5 = new JButton("5");
        B5.addActionListener(new B5click());
        panel.add(B5);

        JButton B4 = new JButton("4");
        B4.addActionListener(new B4click());
        panel.add(B4);

        JButton Brazy = new JButton("*");
        Brazy.addActionListener(new BrazyClick());
        panel.add(Brazy);

        JButton B3 = new JButton("3");
        B3.addActionListener(new B3click());
        panel.add(B3);

        JButton B2 = new JButton("2");
        B2.addActionListener(new B2click());
        panel.add(B2);

        JButton B1 = new JButton("1");
        B1.addActionListener(new B1click());
        panel.add(B1);

        JButton Bdzieli = new JButton("/");
        Bdzieli.addActionListener(new BdzieliClick());
        panel.add(Bdzieli);

        JButton B0 = new JButton("0");
        B0.addActionListener(new B0click());
        panel.add(B0);

        JButton Bpoint = new JButton(".");
        Bpoint.addActionListener(new BpointClick());
        panel.add(Bpoint);

        JButton Bplus = new JButton("+");
        Bplus.addActionListener(new BplusClick());
        panel.add(Bplus);

        JButton Bminus = new JButton("-");
        Bminus.addActionListener(new BminusClick());
        panel.add(Bminus);



        background.add(BorderLayout.NORTH, display);
        background.add(BorderLayout.CENTER, panel);


        frame.setSize(200,200);
        frame.setVisible(true);
    }
    class B9click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){   // zapewnia puste pole przy wpisywaniu poerwszej cyfry,
                area.setText("");
            }
            area.append("9");
            workingMemory2+="9";  //przerzuca cyfre do pamieci roboczej
        }
    }
    class B8click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("8");
            workingMemory2+="8";
        }
    }
    class B7click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("7");
            workingMemory2+="7";
        }
    }
    class B6click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("6");
            workingMemory2+="6";
        }
    }
    class B5click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("5");
            workingMemory2+="5";
        }
    }
    class B4click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("4");
            workingMemory2+="4";
        }
    }
    class B3click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("3");
            workingMemory2+="3";
        }
    }
    class B2click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("2");
            workingMemory2+="2";
        }
    }
    class B1click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("1");
            workingMemory2+="1";
        }
    }
    class B0click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.setText("");
            }
            area.append("0");
            workingMemory2+="0";
        }
    }
    class BequalClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                double d1 = Double.parseDouble(workingMemory1);
                double d2 = Double.parseDouble(workingMemory2);
                if(op==1){
                    r=d1+d2;
                }else if(op==2){
                    r=d1-d2;
                }else if(op==3){
                    r=d1*d2;
                }else if(op==4){
                    r=d1/d2;
                }
                op=0;
                area.setText(nf.format(r));
                workingMemory1="";
                workingMemory2=nf.format(r);
            }catch (Exception ex){
                area.setText(workingMemory2);
            }
        }
    }
    class BplusClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                if(!workingMemory1.equals("")){
                    double r=0.0;
                    double d1 = Double.parseDouble(workingMemory1);
                    double d2 = Double.parseDouble(workingMemory2);
                    if(op==1){
                        r=d1+d2;
                    }else if(op==2){
                        r=d1-d2;
                    }else if(op==3){
                        r=d1*d2;
                    }else if(op==4){
                        r=d1/d2;
                    }
                    area.setText(nf.format(r));
                    workingMemory1=nf.format(r);
                    workingMemory2="";
                    op=1;
                }else{
                    area.setText("");
                    workingMemory1=workingMemory2;
                    workingMemory2="";
                    op=1;
                }
            }catch (Exception ex){
                workingMemory2="";
                area.setText("0");

            }
        }
    }
    class BminusClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                if(!workingMemory1.equals("")){
                    double r=0.0;
                    double d1 = Double.parseDouble(workingMemory1);
                    double d2 = Double.parseDouble(workingMemory2);
                    if(op==1){
                        r=d1+d2;
                    }else if(op==2){
                        r=d1-d2;
                    }else if(op==3){
                        r=d1*d2;
                    }else if(op==4){
                        r=d1/d2;
                    }
                    area.setText(nf.format(r));
                    workingMemory1=nf.format(r);
                    workingMemory2="";
                    op=2;
                }else{
                    area.setText("");
                    workingMemory1=workingMemory2;
                    workingMemory2="";
                    op=2;
                }
            }catch (Exception ex){
                workingMemory2="";
                area.setText("0");

            }
        }
    }
    class BrazyClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                if(!workingMemory1.equals("")){
                    double r=0.0;
                    double d2;
                    double d1 = Double.parseDouble(workingMemory1);
                    if(workingMemory2!=null){
                        d2 = Double.parseDouble(workingMemory2);
                    }else{
                        d2=0;
                    }
                    if(op==1){
                        r=d1+d2;
                    }else if(op==2){
                        r=d1-d2;
                    }else if(op==3){
                        r=d1*d2;
                    }else if(op==4){
                        r=d1/d2;
                    }
                    area.setText(nf.format(r));
                    workingMemory1=nf.format(r);
                    workingMemory2="";
                    op=3;
                }else{
                    area.setText("");
                    workingMemory1=workingMemory2;
                    workingMemory2="";
                    op=3;
                }
            }catch (Exception ex){
                workingMemory2="";
                area.setText("0");

            }
        }
    }
    class BdzieliClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                if(!workingMemory1.equals("")){
                    double r=0.0;
                    double d1 = Double.parseDouble(workingMemory1);
                    double d2 = Double.parseDouble(workingMemory2);
                    if(op==1){
                        r=d1+d2;
                    }else if(op==2){
                        r=d1-d2;
                    }else if(op==3){
                        r=d1*d2;
                    }else if(op==4){
                        r=d1/d2;
                    }
                    area.setText(nf.format(r));
                    workingMemory1=nf.format(r);
                    workingMemory2="";
                    op=4;
                }else{
                    area.setText("");
                    workingMemory1=workingMemory2;
                    workingMemory2="";
                    op=4;
                }
            }catch (Exception ex){
                workingMemory2="";
                area.setText("0");
            }


        }
    }
    class BpointClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            area.append(".");
            workingMemory2+=".";
        }
    }
    class Click implements ActionListener{
        public void actionPerformed (ActionEvent event){
            workingMemory1="";
            workingMemory2="";
            area.setText("");
        }
    }
    class squareClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                double d = Double.parseDouble(area.getText());
                double r = d*d;
                workingMemory2= nf.format(r);
                area.setText(nf.format(r));
            }catch (Exception ex){
                workingMemory2="";
                area.setText("0");

            }
        }
    }
    class rootClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                double r = Math.sqrt(Double.parseDouble(area.getText()));
                workingMemory2= nf.format(r);
                area.setText(nf.format(r));
            } catch (Exception ex){
                area.setText("0");
            }
        }
    }
    class pmClick implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(workingMemory2.equals("")){
                area.append("-");
                workingMemory2+="-";
            }else if(workingMemory2.substring(0,1).equals("-")){
                workingMemory2=workingMemory2.substring(1);
                area.setText(workingMemory2);
            }else{
                workingMemory2="-" + workingMemory2;
                area.setText(workingMemory2);
            }
        }
    }
    class exitListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            System.exit(0);
        }
    }
}


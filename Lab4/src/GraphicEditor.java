/*
 *  Program EdytorGraficzny - aplikacja z graficznym interfejsem
 *   - obs�uga zdarze� od klawiatury, myszki i innych element�w GUI.
 *
 *  Autor: Pawe� Rogalinski, ...
 *   Data: 1. 10, 2015 r.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class GraphicEditor extends JFrame implements ActionListener {


    private static final long serialVersionUID = 3727471814914970170L;


    private final String DESCRIPTION = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
            + "   strzalki ==> przesuwanie figur\n"
            + "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
            + "   +,-  ==> powiekszanie, pomniejszanie\n"
            + "   DEL  ==> kasowanie figur\n"
            + "   p  ==> dodanie nowego punktu\n"
            + "   c  ==> dodanie nowego kola\n"
            + "   t  ==> dodanie nowego trojkata\n"
            + "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
            + "   ALT + klik ==> zmiana zaznaczenia figur\n"
            + "   przeciaganie ==> przesuwanie figur";


    protected Picture picture;

    private JMenu[] menu = {new JMenu("Figury"),
            new JMenu("Edytuj")};

    private JMenuItem[] items = {new JMenuItem("Punkt"),
            new JMenuItem("Kolo"),
            new JMenuItem("Trojkat"),
            new JMenuItem("Wypisz wszystkie"),
            new JMenuItem("Przesun w gore"),
            new JMenuItem("Przesun w dol"),
            new JMenuItem("Powieksz"),
            new JMenuItem("Pomniejsz"),
    };

    private JButton buttonPoint = new JButton("Punkt");
    private JButton buttonCircle = new JButton("Kolo");
    private JButton buttonTriangle = new JButton("Trojkat");


    public GraphicEditor() {
        super("Edytor graficzny");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < items.length; i++)
            items[i].addActionListener(this);

        // dodanie opcji do menu "Figury"
        menu[0].add(items[0]);
        menu[0].add(items[1]);
        menu[0].add(items[2]);
        menu[0].addSeparator();
        menu[0].add(items[3]);

        // dodanie opcji do menu "Edytuj"
        menu[1].add(items[4]);
        menu[1].add(items[5]);
        menu[1].addSeparator();
        menu[1].add(items[6]);
        menu[1].add(items[7]);

        // dodanie do okna paska menu
        JMenuBar menubar = new JMenuBar();
        for (int i = 0; i < menu.length; i++)
            menubar.add(menu[i]);
        setJMenuBar(menubar);

        picture = new Picture();
        picture.addKeyListener(picture);
        picture.setFocusable(true);
        picture.addMouseListener(picture);
        picture.setLayout(new FlowLayout());

        buttonPoint.addActionListener(this);
        buttonCircle.addActionListener(this);
        buttonTriangle.addActionListener(this);

        picture.add(buttonPoint);
        picture.add(buttonCircle);
        picture.add(buttonTriangle);

        setContentPane(picture);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        Object zrodlo = evt.getSource();

        if (zrodlo == buttonPoint)
            picture.addFigure(new Point());
        if (zrodlo == buttonCircle)
            picture.addFigure(new Circle());
        if (zrodlo == buttonTriangle)
            picture.addFigure(new Triangle());

        if (zrodlo == items[0])
            picture.addFigure(new Point());
        if (zrodlo == items[1])
            picture.addFigure(new Circle());
        if (zrodlo == items[2])
            picture.addFigure(new Triangle());
        if (zrodlo == items[3])
            JOptionPane.showMessageDialog(null, picture.toString());

        if (zrodlo == items[4])
            picture.moveAllFigures(0, -10);
        if (zrodlo == items[5])
            picture.moveAllFigures(0, 10);
        if (zrodlo == items[6])
            picture.scaleAllFigures(1.1f);
        if (zrodlo == items[7])
            picture.scaleAllFigures(0.9f);

        picture.requestFocus(); // przywrocenie ogniskowania w celu przywrocenia
        // obslugi zadarez� pd klawiatury
        repaint();
    }

    public static void main(String[] args) {
        new GraphicEditor();
    }

}


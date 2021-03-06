
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Program: Aplikacja okienkowa z GUI, która umożliwia 
 *          zarządzanie grupami obiektów klasy Person.
 *    Plik: GroupManagerApp.java
 *          
 *   Autor: Paweł Rogalinski
 *    Data: pazdziernik 2017 r.
 */

public class GroupManagerApp extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String GREETING_MESSAGE =
            "Program do zarządzania grupami osób " +
                    "- wersja okienkowa\n\n" +
                    "Autor: Paweł Rogalinski\n" +
                    "Data:  październik 2017 r.\n";

    // Nazwa pliku w którym są zapisywane automatycznie dane przy
    // zamykaniu aplikacji i z którego są czytane dane po uruchomieniu.
    private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN";

    // Utworzenie obiektu reprezentującego główne okno aplikacji.
    // Po utworzeniu obiektu na pulpicie zostanie wyświetlone
    // główne okno aplikacji.
    public static void main(String[] args) {
        new GroupManagerApp();
    }


    WindowAdapter windowListener = new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {
            // Wywoływane gdy okno aplikacji jest zamykane za pomocą
            // wywołania metody dispose()

            JOptionPane.showMessageDialog(null, "Program zakończył działanie!");

        }


        @Override
        public void windowClosing(WindowEvent e) {
            // Wywoływane gdy okno aplikacji jest  zamykane za pomocą
            // systemowego menu okna tzn. krzyżyk w narożniku)
            windowClosed(e);
        }

    };



    // Zbiór grup osób, którymi zarządza aplikacja
    private List<GroupOfPeople> currentList = new ArrayList<GroupOfPeople>();

    // Pasek menu wyświetlany na panelu w głównym oknie aplikacji
    JMenuBar menuBar        = new JMenuBar();
    JMenu menuGroups        = new JMenu("Grupy");
    JMenu menuSpecialGroups = new JMenu("Grupy specjalne");
    JMenu menuAbout         = new JMenu("O programie");

    // Opcje wyświetlane na panelu w głównym oknie aplikacji
    JMenuItem menuNewGroup           = new JMenuItem("Utwórz grupę");
    JMenuItem menuEditGroup          = new JMenuItem("Edytuj grupę");
    JMenuItem menuDeleteGroup        = new JMenuItem("Usuń grupę");
    JMenuItem menuLoadGroup          = new JMenuItem("załaduj grupę z pliku");
    JMenuItem menuSaveGroup          = new JMenuItem("Zapisz grupę do pliku");

    JMenuItem menuGroupUnion         = new JMenuItem("Połączenie grup");
    JMenuItem menuGroupIntersection  = new JMenuItem("Część wspólna grup");
    JMenuItem menuGroupDifference    = new JMenuItem("Różnica grup");
    JMenuItem menuGroupSymmetricDiff = new JMenuItem("Różnica symetryczna grup");

    JMenuItem menuAuthor             = new JMenuItem("Autor");

    // Przyciski wyświetlane na panelu w głównym oknie aplikacji
    JButton buttonNewGroup = new JButton("Utwórz");
    JButton buttonEditGroup = new JButton("Edytuj");
    JButton buttonDeleteGroup = new JButton(" Unuń ");
    JButton buttonLoadGroup = new JButton("Otwórz");
    JButton buttonSavegroup = new JButton("Zapisz");

    JButton buttonUnion = new JButton("Suma");
    JButton buttonIntersection = new JButton("Iloczyn");
    JButton buttonDifference = new JButton("Różnica");
    JButton buttonSymmetricDiff = new JButton("Różnica symetryczna");


    // Widok tabeli z listą grup wyświetlany
    // na panelu w oknie głównym aplikacji
    ViewGroupList viewList;


    public GroupManagerApp() {
        // Konfiguracja parametrów głównego okna aplikacji
        setTitle("GroupManager - zarządzanie grupami osób");
        setSize(450, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Dodanie słuchacza zdarzeń od okna aplikacji, który
        // umożliwi automatyczny zapis danych do pliku,
        // gdy główne okno aplikacji jest zamykane.
        addWindowListener(new WindowAdapter() {
                              // To jest definicja anonimowej klasy (klasy bez nazwy)
                              // która dziedziczy po klasie WindowAdapter i przedefiniowuje
                              // metody windowClosed oraz windowClosing.

                              @Override
                              public void windowClosed(WindowEvent event) {
                                  // Wywoływane gdy okno aplikacji jest zamykane za pomocą
                                  // wywołania metody dispose()
                                  try {
                                      saveGroupListToFile(ALL_GROUPS_FILE);
                                      JOptionPane.showMessageDialog(null, "Dane zostały zapisane do pliku " + ALL_GROUPS_FILE);
                                  } catch (PersonException e) {
                                      JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                                  }
                              }

                              @Override
                              public void windowClosing(WindowEvent e) {
                                  // Wywoływane gdy okno aplikacji jest zamykane za pomocą
                                  // systemowego menu okna tzn. krzyżyk w narożniku)
                                  windowClosed(e);
                              }

                          } // koniec klasy anonimowej
        ); // koniec wywołania metody addWindowListener

        try {
            // Automatyczne załadowanie danych z pliku zanim
            // główne okno aplikacji zostanie pokazane na ekranie
            loadGroupListFromFile(ALL_GROUPS_FILE);
            JOptionPane.showMessageDialog(null, "Dane zostały wczytane z pliku " + ALL_GROUPS_FILE);
        } catch (PersonException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }


        // Utworzenie i konfiguracja menu aplikacji
        setJMenuBar(menuBar);
        menuBar.add(menuGroups);
        menuBar.add(menuSpecialGroups);
        menuBar.add(menuAbout);

        menuGroups.add(menuNewGroup);
        menuGroups.add(menuEditGroup);
        menuGroups.add(menuDeleteGroup);
        menuGroups.addSeparator();
        menuGroups.add(menuLoadGroup);
        menuGroups.add(menuSaveGroup);

        menuSpecialGroups.add(menuGroupUnion);
        menuSpecialGroups.add(menuGroupIntersection);
        menuSpecialGroups.add(menuGroupDifference);
        menuSpecialGroups.add(menuGroupSymmetricDiff);

        menuAbout.add(menuAuthor);

        // Dodanie słuchaczy zdarzeń do wszystkich opcji menu.
        // UWAGA: słuchaczem zdarzeń będzie metoda actionPerformed
        // zaimplementowana w tej klasie i wywołana dla
        // bieżącej instancji okna aplikacji - referencja this
        menuNewGroup.addActionListener(this);
        menuEditGroup.addActionListener(this);
        menuDeleteGroup.addActionListener(this);
        menuLoadGroup.addActionListener(this);
        menuSaveGroup.addActionListener(this);
        menuGroupUnion.addActionListener(this);
        menuGroupIntersection.addActionListener(this);
        menuGroupDifference.addActionListener(this);
        menuGroupSymmetricDiff.addActionListener(this);
        menuAuthor.addActionListener(this);

        // Dodanie słuchaczy zdarzeń do wszystkich przycisków.
        // UWAGA: słuchaczem zdarzeń będzie metoda actionPerformed
        // zaimplementowana w tej klasie i wywołana dla
        // bieżącej instancji okna aplikacji - referencja this
        buttonNewGroup.addActionListener(this);
        buttonEditGroup.addActionListener(this);
        buttonDeleteGroup.addActionListener(this);
        buttonLoadGroup.addActionListener(this);
        buttonSavegroup.addActionListener(this);
        buttonUnion.addActionListener(this);
        buttonIntersection.addActionListener(this);
        buttonDifference.addActionListener(this);
        buttonSymmetricDiff.addActionListener(this);

        // Utwotrzenie tabeli z listą osób należących do grupy
        viewList = new ViewGroupList(currentList, 400, 250);
        viewList.refreshView();

        // Utworzenie głównego panelu okna aplikacji.
        // Domyślnym menedżerem rozkładu dla panelu będzie
        // FlowLayout, który układa wszystkie komponenty jeden za drugim.
        JPanel panel = new JPanel();

        // Dodanie i rozmieszczenie na panelu wszystkich
        // komponentów GUI.
        panel.add(viewList);
        panel.add(buttonNewGroup);
        panel.add(buttonEditGroup);
        panel.add(buttonDeleteGroup);
        panel.add(buttonLoadGroup);
        panel.add(buttonSavegroup);
        panel.add(buttonUnion);
        panel.add(buttonIntersection);
        panel.add(buttonDifference);
        panel.add(buttonSymmetricDiff);

        // Umieszczenie Panelu w głównym oknie aplikacji.
        setContentPane(panel);

        // Pokazanie na ekranie głównego okna aplikacji
        // UWAGA: Tą instrukcję należy wykonać jako ostatnią
        // po zainicjowaniu i rozmieszczeniu na panelu
        // wszystkich komponentów GUI.
        // Od tego momentu aplikacja uruchamia główną pętlę zdarzeń
        // która działa w nowym wątku niezależnie od pozostałej części programu.
        setVisible(true);
    }



    @SuppressWarnings("unchecked")
    void loadGroupListFromFile(String file_name) throws PersonException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
            currentList = (List<GroupOfPeople>)in.readObject();
        } catch (FileNotFoundException e) {
            throw new PersonException("Nie odnaleziono pliku " + file_name);
        } catch (Exception e) {
            throw new PersonException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }


    void saveGroupListToFile(String file_name) throws PersonException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
            out.writeObject(currentList);
        } catch (FileNotFoundException e) {
            throw new PersonException("Nie odnaleziono pliku " + file_name);
        } catch (IOException e) {
            throw new PersonException("Wystąpił błąd podczas zapisu danych do pliku.");
        }
    }


    //  Metoda tworzy okno dialogowe do wyboru grupy podczas tworzenia
    //  grup specjalnych i innych operacji na grupach
    private  GroupOfPeople chooseGroup(Window parent, String message){
        Object[] groups = currentList.toArray();
        GroupOfPeople group = (GroupOfPeople)JOptionPane.showInputDialog(
                parent, message,
                "Wybierz grupę",
                JOptionPane.QUESTION_MESSAGE,
                null,
                groups,
                null);
        return group;
    }



    @Override
    public void actionPerformed(ActionEvent event) {
        // Odczytanie referencji do obiektu, który wygenerował zdarzenie.
        Object source = event.getSource();

        try {
            if (source == menuNewGroup || source == buttonNewGroup) {
                GroupOfPeople group = GroupOfPeopleWindowDialog.createNewGroupOfPeople(this);
                if (group != null) {
                    currentList.add(group);
                }
            }

            if (source == menuEditGroup || source == buttonEditGroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfPeople> iterator = currentList.iterator();
                    while (index-- > 0)
                        iterator.next();
                    new GroupOfPeopleWindowDialog(this, iterator.next());
                }
            }

            if (source == menuDeleteGroup || source == buttonDeleteGroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfPeople> iterator = currentList.iterator();
                    while (index-- >= 0)
                        iterator.next();
                    iterator.remove();
                }
            }

            if (source == menuLoadGroup || source == buttonLoadGroup) {
                JFileChooser chooser = new JFileChooser(".");
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    GroupOfPeople group = GroupOfPeople.readFromFile(chooser.getSelectedFile().getName());
                    currentList.add(group);
                }
            }

            if (source == menuSaveGroup || source == buttonSavegroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfPeople> iterator = currentList.iterator();
                    while (index-- > 0)
                        iterator.next();
                    GroupOfPeople group = iterator.next();

                    JFileChooser chooser = new JFileChooser(".");
                    int returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        GroupOfPeople.printToFile( chooser.getSelectedFile().getName(), group );
                    }
                }
            }

            if (source == menuGroupUnion || source == buttonUnion) {
                String message1 =
                        "SUMA GRUP\n\n" +
                                "Tworzenie grupy zawierającej wszystkie osoby z grupy pierwszej\n" +
                                "oraz wszystkie osoby z grupy drugiej.\n" +
                                "Wybierz pierwszą grupę:";
                String message2 =
                        "SUMA GRUP\n\n" +
                                "Tworzenie grupy zawierającej wszystkie osoby z grupy pierwszej\n" +
                                "oraz wszystkie osoby z grupy drugiej.\n" +
                                "Wybierz drugą grupę:";
                GroupOfPeople group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfPeople group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfPeople.createGroupUnion(group1, group2) );
            }

            if (source == menuGroupIntersection || source == buttonIntersection) {
                String message1 =
                        "ILOCZYN GRUP\n\n" +
                                "Tworzenie grupy osób, które należą zarówno do grupy pierwszej,\n" +
                                "jak i do grupy drugiej.\n" +
                                "Wybierz pierwszą grupę:";
                String message2 =
                        "ILOCZYN GRUP\n\n" +
                                "Tworzenie grupy osób, które należą zarówno do grupy pierwszej,\n" +
                                "jak i do grupy drugiej.\n" +
                                "Wybierz drugą grupę:";
                GroupOfPeople group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfPeople group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfPeople.createGroupIntersection(group1, group2) );
            }

            if (source == menuGroupDifference || source == buttonDifference) {
                String message1 =
                        "RÓŻNICA GRUP\n\n" +
                                "Tworzenie grupy osób, które należą do grupy pierwszej\n" +
                                "i nie ma ich w grupie drugiej.\n" +
                                "Wybierz pierwszą grupę:";
                String message2 =
                        "RÓŻNICA GRUP\n\n" +
                                "Tworzenie grupy osób, które należą do grupy pierwszej\n" +
                                "i nie ma ich w grupie drugiej.\n" +
                                "Wybierz drugą grupę:";
                GroupOfPeople group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfPeople group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfPeople.createGroupDifference(group1, group2) );
            }

            if (source == menuGroupSymmetricDiff || source == buttonSymmetricDiff) {
                String message1 = "RÓŻNICA SYMETRYCZNA GRUP\n\n"
                        + "Tworzenie grupy zawierającej osoby należące tylko do jednej z dwóch grup,\n"
                        + "Wybierz pierwszą grupę:";
                String message2 = "RÓŻNICA SYMETRYCZNA GRUP\n\n"
                        + "Tworzenie grupy zawierającej osoby należące tylko do jednej z dwóch grup,\n"
                        + "Wybierz drugą grupę:";
                GroupOfPeople group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfPeople group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfPeople.createGroupSymmetricDiff(group1, group2) );
            }

            if (source == menuAuthor) {
                JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
            }

        } catch (PersonException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        // Aktualizacja zawartości tabeli z listą grup.
        viewList.refreshView();
    }

} // koniec klasy GroupManagerApp



/*
 * Pomocnicza klasa do wyświetlania listy grup
 * w postaci tabeli na panelu okna głównego
 */
class ViewGroupList extends JScrollPane {
    private static final long serialVersionUID = 1L;

    private List<GroupOfPeople> list;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGroupList(List<GroupOfPeople> list, int width, int height){
        this.list = list;
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createTitledBorder("Lista grup:"));

        String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba osób" };
        tableModel = new DefaultTableModel(tableHeader, 0);
        table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Blokada możliwości edycji komórek tabeli
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        setViewportView(table);
    }

    void refreshView(){
        tableModel.setRowCount(0);
        for (GroupOfPeople group : list) {
            if (group != null) {
                String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
                tableModel.addRow(row);
            }
        }
    }

    int getSelectedIndex(){
        int index = table.getSelectedRow();
        if (index<0) {
            JOptionPane.showMessageDialog(this, "Żadana grupa nie jest zaznaczona.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }

} // koniec klasy ViewGroupList
package com.bdd.GUI;

import com.bdd.GUI.components.Component;
import com.bdd.mer.EERDiagram;
import com.bdd.GUI.menuBar.MenuBar;
import com.bdd.mer.components.relationship.Relationship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private final EERDiagram diagram;
    private final MenuBar menuBar;

    public MainFrame() {

        setUndecorated(false);  // Removing of the title bar.
        setTitle("Zylva EERD");
        setSize(800, 600);
        setLocationRelativeTo(null);

        /* ---------------------------------------------------------------------------------------------------------- */

        // Creation of the drawing panel, the bar menu and the menu.
        this.diagram = new EERDiagram();
        this.menuBar = new MenuBar(this, diagram);
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
        Dimension dimension = new Dimension(120, 30);

        menu.setPreferredSize(dimension);
        menu.setBackground(Color.LIGHT_GRAY);

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                         Add Entity Key                                                     */
        /* ---------------------------------------------------------------------------------------------------------- */

        JButton addEntityKey = new JButton("Add entity");
        addEntityKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "actionE");
        addEntityKey.getActionMap().put("actionE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.addEntity();
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                      Add Relationship Key                                                  */
        /* ---------------------------------------------------------------------------------------------------------- */

        JButton addRelationshipKey = new JButton("Add relationship");
        addRelationshipKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "actionR");
        addRelationshipKey.getActionMap().put("actionR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Relationship.addRelationship(diagram, diagram.getSelectedComponents().toArray(Component[]::new));
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                           Add Dependency Key                                                   */
        /* ---------------------------------------------------------------------------------------------------------- */

        JButton addDependencyKey= new JButton("Add dependency");
        addDependencyKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), "actionD");
        addDependencyKey.getActionMap().put("actionD", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Relationship.addDependency(diagram, diagram.getSelectedComponents().toArray(Component[]::new));
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                       Add Hierarchy Key                                                    */
        /* ---------------------------------------------------------------------------------------------------------- */


        JButton addHierarchyKey= new JButton("Add hierarchy");
        addHierarchyKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), "actionH");
        addHierarchyKey.getActionMap().put("actionH", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.addHierarchy();
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                          Add Note Key                                                      */
        /* ---------------------------------------------------------------------------------------------------------- */

        JButton addNoteKey= new JButton("Add note");
        addNoteKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "actionN");
        addNoteKey.getActionMap().put("actionN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.addNote();
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                              Delete Key                                                    */
        /* ---------------------------------------------------------------------------------------------------------- */

        JButton deleteKey = new JButton("Delete key");
        deleteKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Supr");
        deleteKey.getActionMap().put("Supr", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                diagram.deleteSelectedComponents();
            }
        });

        /* ---------------------------------------------------------------------------------------------------------- */
        /*                                            Add Association Key                                             */
        /* ---------------------------------------------------------------------------------------------------------- */

//        JButton addAssociationKey = new JButton("Add association key");
//        addAssociationKey.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "ActionA");
//        addAssociationKey.getActionMap().put("ActionA", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                diagram.addAssociation();
//            }
//        });

        // Adding the keys to the frame's functionalities.
        getContentPane().add(addEntityKey);
        getContentPane().add(addRelationshipKey);
        getContentPane().add(addDependencyKey);
        getContentPane().add(addHierarchyKey);
        getContentPane().add(addNoteKey);
        getContentPane().add(deleteKey);
//        getContentPane().add(addAssociationKey);

        add(diagram, BorderLayout.CENTER);
        setJMenuBar(menuBar);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public void resetLanguage() {
        this.menuBar.resetLanguage();
        this.diagram.resetLanguage();
    }

}

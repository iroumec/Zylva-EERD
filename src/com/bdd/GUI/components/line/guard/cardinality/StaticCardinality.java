package com.bdd.GUI.components.line.guard.cardinality;

import com.bdd.GUI.Diagram;
import com.bdd.GUI.components.line.Line;

import javax.swing.*;

/**
    This is a cardinality whose values cannot be changed.
 */

// I think it's not correct to it being a subclass... It's just a cardinality with a different JPopupMenu.
public class StaticCardinality extends Cardinality {

    public StaticCardinality(String firstValue, String secondValue, Line line, Diagram diagram) {
        super(firstValue, secondValue, line, diagram);
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                               Overridden Methods                                               */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    protected JPopupMenu getPopupMenu() {

        return new JPopupMenu();
    }
}

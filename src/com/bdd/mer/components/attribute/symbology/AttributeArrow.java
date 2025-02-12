package com.bdd.mer.components.attribute.symbology;

import java.io.Serializable;

public enum AttributeArrow implements Serializable {
    OPTIONAL("- - - "),
    NON_OPTIONAL("------");

    private final String arrowBody;

    AttributeArrow(String arrowBody) { this.arrowBody = arrowBody; }

    @Override
    public String toString() {
        return this.arrowBody;
    }
}

// Maybe all this should be drawing...
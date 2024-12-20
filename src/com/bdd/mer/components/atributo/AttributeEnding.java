package com.bdd.mer.components.atributo;

public enum AttributeEnding {
    MULTIVALUED(">"),
    NON_MULTIVALUED("-");

    private final String arrowEnding;

    AttributeEnding(String arrowEnding) { this.arrowEnding = arrowEnding; }

    public String getArrowEnding() { return this.arrowEnding; }
}

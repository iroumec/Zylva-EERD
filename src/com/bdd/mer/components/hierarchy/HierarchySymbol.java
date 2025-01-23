package com.bdd.mer.components.hierarchy;

import java.io.Serializable;

public enum HierarchySymbol implements Serializable {

    DISJUNCT("d"),
    OVERLAPPING("o");

    final String symbol;

    HierarchySymbol(String symbol) { this. symbol = symbol; }

    public String getSymbol() { return this.symbol; }

}
package com.bdd.mer.components.attribute.internal.cardinalities;

import com.bdd.mer.components.attribute.external.Attribute;
import com.bdd.mer.components.attribute.external.DescAttrEERComp;
import com.bdd.mer.derivation.Derivation;
import com.bdd.mer.derivation.elements.ElementDecorator;
import com.bdd.mer.derivation.elements.SingleElement;
import com.bdd.mer.derivation.elements.containers.Replacer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public final class Multivalued implements Cardinality {

    private final int arrowWidth;
    private final int arrowHeight;
    private final static Multivalued DEFAULT_INSTANCE = new Multivalued(3, 3);

    private Multivalued(int arrowWidth, int arrowHeight) {
        this.arrowWidth = arrowWidth;
        this.arrowHeight = arrowHeight;
    }

    public static Multivalued getInstance() { return DEFAULT_INSTANCE; }

    @SuppressWarnings("unused")
    public static Multivalued getInstance(int arrowWidth, int arrowHeight) {
        return new Multivalued(arrowWidth, arrowHeight);
    }

    @Override
    public Cardinality getOpposite() {
        return Univalued.getInstance();
    }

    @Override
    public boolean generatesDerivation() { return true; }

    @Override
    public void draw(Graphics2D g2, int x, int y) {
        g2.drawLine(x - this.arrowWidth, y + this.arrowHeight, x, y);
        g2.drawLine(x - this.arrowWidth, y - this.arrowHeight, x, y);
    }

    @Override
    public List<Derivation> getDerivations(@NotNull DescAttrEERComp owner, @NotNull Attribute attribute) {

        Derivation derivation = new Derivation(attribute.getIdentifier());

        derivation.addIdentificationElement(new SingleElement(attribute.getIdentifier()));

        derivation.addIdentificationElement(new SingleElement(owner.getIdentifier(),
                new Replacer(ElementDecorator.FOREIGN))
        );

        return List.of(derivation);
    }
}

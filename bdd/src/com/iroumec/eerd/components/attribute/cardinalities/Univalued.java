package com.iroumec.eerd.components.attribute.cardinalities;

import com.iroumec.eerd.components.attribute.Attribute;
import com.iroumec.eerd.components.attribute.DescAttrEERComp;
import com.iroumec.derivation.Derivation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Univalued implements Cardinality {

    private final static Univalued INSTANCE = new Univalued();

    private Univalued() {}

    public static Univalued getInstance() {
        return INSTANCE;
    }

    @Override
    public Cardinality getOpposite() {
        return Multivalued.getInstance();
    }

    @Override
    public boolean generatesDerivation() {
        return false;
    }

    @Override
    public List<Derivation> getDerivations(@NotNull DescAttrEERComp owner, @NotNull Attribute attribute) {
        return new ArrayList<>();
    }

    @Override
    public void draw(Graphics2D g2, int x, int y) {
        // Empty on purpose.
    }
}

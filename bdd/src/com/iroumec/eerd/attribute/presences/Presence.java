package com.iroumec.eerd.attribute.presences;

import com.iroumec.derivation.elements.Element;
import com.iroumec.derivation.elements.containers.Holder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface Presence {

    Holder getHolder();

    void addDecoration(@NotNull Element element);

    Presence getOpposite();

    void draw(Graphics2D g2, int x1, int y1, int x2, int y2);
}

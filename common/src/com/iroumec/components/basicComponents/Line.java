package com.iroumec.components.basicComponents;

import com.iroumec.components.Component;
import com.iroumec.components.basicComponents.line.lineMultiplicity.LineMultiplicity;
import com.iroumec.components.basicComponents.line.lineMultiplicity.SingleLine;
import com.iroumec.components.basicComponents.line.lineShape.DirectLine;
import com.iroumec.components.basicComponents.line.lineShape.LineShape;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public final class Line extends Component {

    private final Component firstComponent;
    private final Component secondComponent;

    /**
     * The interface {@code Stroke} is not used here because it's not serializable.
     */
    private final int strokeWidth;
    private final LineShape lineShape;
    private final LineMultiplicity lineMultiplicity;

    Line(Builder builder) {
        super();
        this.firstComponent = builder.firstComponent;
        this.secondComponent = builder.secondComponent;
        this.lineShape = builder.lineShape;
        this.lineMultiplicity = builder.lineMultiplicity;
        this.strokeWidth = builder.strokeWidth;
        setDrawingPriority(2);
    }

    public Point getCenterPoint() {

        return this.lineShape.getCenterPoint(firstComponent.getX(), firstComponent.getY(), secondComponent.getX(), secondComponent.getY());

    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                               Overridden Methods                                               */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    protected JPopupMenu getPopupMenu() {
        return new JPopupMenu();
    }

    @Override
    public void draw(Graphics2D g2) {

        int x1 = firstComponent.getX();
        int y1 = firstComponent.getY();
        int x2 = secondComponent.getX();
        int y2 = secondComponent.getY();

        Stroke currentStroke = g2.getStroke();

        g2.setStroke(new BasicStroke(this.strokeWidth));
        this.lineMultiplicity.draw(g2, lineShape, x1, y1, x2, y2);
        g2.setStroke(currentStroke);
    }

    @Override
    public void notifyRemovingOf(Component component) {

        if (component.equals(this.firstComponent) || component.equals(this.secondComponent)) {
            this.setForDelete();
        }
    }

    @Override
    public Rectangle getBounds() {

        Point centerPoint = this.getCenterPoint();

        return new Rectangle(centerPoint.x, centerPoint.y, 0, 0);

    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                                     Builder                                                    */
    /* -------------------------------------------------------------------------------------------------------------- */

    public static class Builder {

        // Required parameters
        private final Component firstComponent;
        private final Component secondComponent;

        // OptionalPresence parameters - initialized to default values
        private LineShape lineShape = new DirectLine();
        private LineMultiplicity lineMultiplicity = new SingleLine();
        private int strokeWidth = 1;

        public Builder(Component firstComponent, Component secondComponent) {
            this.firstComponent = firstComponent;
            this.secondComponent = secondComponent;
        }

        public Builder lineShape(LineShape lineShape) {
            this.lineShape = lineShape;
            return this;
        }

        public Builder lineMultiplicity(LineMultiplicity lineMultiplicity) {
            this.lineMultiplicity = lineMultiplicity;
            return this;
        }

        public Builder strokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}

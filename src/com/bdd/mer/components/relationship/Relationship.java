package com.bdd.mer.components.relationship;

import com.bdd.GUI.userPreferences.LanguageManager;
import com.bdd.mer.EERDiagram;
import com.bdd.mer.components.AttributableEERComponent;
import com.bdd.GUI.Component;
import com.bdd.mer.components.attribute.Attribute;
import com.bdd.mer.components.entity.EntityWrapper;
import com.bdd.GUI.components.line.Line;
import com.bdd.GUI.components.line.guard.cardinality.Cardinality;
import com.bdd.GUI.components.line.guard.cardinality.StaticCardinality;
import com.bdd.GUI.components.line.lineMultiplicity.DoubleLine;
import com.bdd.GUI.components.line.lineShape.SquaredLine;
import com.bdd.mer.components.relationship.relatable.Relatable;
import com.bdd.mer.derivation.Derivable;
import com.bdd.mer.derivation.derivationObjects.DerivationObject;
import com.bdd.mer.derivation.derivationObjects.PluralDerivation;
import com.bdd.GUI.Diagram;
import com.bdd.GUI.structures.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class Relationship extends AttributableEERComponent {

    /**
     * Participant of the relationship.
     */
    private final Map<Relatable, List<Line>> participants;
    private int horizontalDiagonal, verticalDiagonal; // Posición del centro del rombo
    private final Polygon shape;
    private Association association;

    /**
     * Constructs a {@code Relationship}.
     *
     * @param text Name of the relationship.
     * @param x X coordinate of the relationship.
     * @param y Y coordinate of the relationship.
     * @param diagram {@code Diagram} in which the relationship lives.
     */
    private Relationship(String text, int x, int y, Diagram diagram) {

        super(text, x, y, diagram);

        this.participants = new HashMap<>();
        this.shape = new Polygon();
        setDrawingPriority(6);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private void addParticipant(Relatable relatableComponent, Line line) {

        List<Line> lines = this.participants.get(relatableComponent);

        // The participant doesn't exist.
        if (lines == null) {
            lines = new ArrayList<>();
            relatableComponent.addRelationship(this);
        }

        lines.add(line);

        this.participants.put(relatableComponent, lines);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public Set<Component> getRelatedComponents() {

        Set<Component> out = new HashSet<>(this.getAttributes());

        for (Map.Entry<Relatable, List<Line>> participant : this.participants.entrySet()) {

            out.add((Component) participant.getKey());

            if (participant.getKey() instanceof AttributableEERComponent attributableEERComponent) {
                out.addAll(attributableEERComponent.getAttributes());
            }

            out.addAll(participant.getValue());
        }

        return out;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    /**
     * This method let you know the number of participants in the relationship.
     *
     * @return The number of participants in the relationship.
     */
    public int getNumberOfParticipants() {
        return this.participants.size();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public List<Relatable> getParticipants() {
        return new ArrayList<>(this.participants.keySet());
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private void updateDiagonals(int textWidth, int textHeight, int margin) {
        horizontalDiagonal = textWidth + 2 * margin; // Diagonal horizontal basada en el ancho del texto
        verticalDiagonal = textHeight + 2 * margin; // Diagonal vertical basada en el alto del texto
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public void setAssociation(Association association) {
        this.association = association;
        this.resetPopupMenu();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public boolean allMaxCardinalitiesAreN() {

        List<Line> lines = this.getLines();

        for (Line line : lines) {

            // I know all the guarded lines will have a cardinality guard.
            // Maybe it's not a bad idea to use a generic type to make sure...
            Pair<String, String> cardinality = Cardinality.removeFormat(line.getText());

            String maxCardinality = cardinality.second();

            if (!maxCardinality.matches("[a-zA-Z]")) {
                // If it's a number...
                return false;
            }
        }

        return true;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    private List<Line> getLines() {

        List<Line> out = new ArrayList<>();

        for (Map.Entry<Relatable, List<Line>> participant : this.participants.entrySet()) {
            out.addAll(participant.getValue());
        }

        return out;
    }

    /**
     * Add an association to the diagram.
     */
    // There must be selected at least an entity and a relationship (unary relationship)
    private void createAssociation() {

        if (this.allMaxCardinalitiesAreN()) {

            Association association = new Association(this, this.diagram);

            this.diagram.addComponent(association);
            this.diagram.repaint();
        } else {

            JOptionPane.showMessageDialog(this.diagram, "An association can only be created for N:N or N:N:N relationships.");
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                               Overridden Methods                                               */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public void draw(Graphics2D g2) {

        FontMetrics fm = g2.getFontMetrics();

        int anchoTexto = fm.stringWidth(this.getText());
        int altoTexto = fm.getHeight();

        int xTexto = getX() - anchoTexto / 2;
        int yTexto = getY() + altoTexto / 4; // It's divided by four to compensate the text baseline.

        g2.setStroke(new BasicStroke(1));

        int margin = 15; // Margin around the text.

        // It is not necessary to do this all the time. Only if the text is changed.
        this.updateDiagonals(anchoTexto, altoTexto, margin);

        shape.reset();
        shape.addPoint(getX(), getY() - verticalDiagonal / 2); // Upper point
        shape.addPoint(getX() + horizontalDiagonal / 2, getY()); // Right point
        shape.addPoint(getX(), getY() + verticalDiagonal / 2); // Lower point
        shape.addPoint(getX() - horizontalDiagonal / 2, getY()); // Left point

        g2.setColor(Color.WHITE);
        g2.fillPolygon(shape);

        g2.setColor(Color.BLACK);
        g2.drawString(this.getText(), xTexto, yTexto);

        if (this.isSelected()) {
            this.setSelectionOptions(g2);
        }

        g2.drawPolygon(shape);
        this.setShape(shape);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    protected JPopupMenu getPopupMenu() {

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem actionItem = new JMenuItem("action.addAttribute");
        actionItem.addActionListener(_ -> this.addAttribute());
        popupMenu.add(actionItem);

        if (association == null) {

            actionItem = new JMenuItem("action.addAssociation");
            actionItem.addActionListener(_ -> this.createAssociation());
            popupMenu.add(actionItem);
        }

        //noinspection DuplicatedCode
        actionItem = new JMenuItem("action.rename");
        actionItem.addActionListener(_ -> this.rename());
        popupMenu.add(actionItem);

        actionItem = new JMenuItem("action.setForDelete");
        actionItem.addActionListener(_ -> this.deleteWithConfirmation());
        popupMenu.add(actionItem);

        return popupMenu;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    protected void cleanReferencesTo(Component component) {

        super.cleanReferencesTo(component);

        if (component instanceof Relatable relatable) {

            // If component is a participant, it is assumed that its lines were removed in the
            // notifyRemovingOf() method.
            this.participants.remove(relatable);
        }

    }

    @Override
    protected void notifyRemovingOf(Component component) {

        if (component instanceof Relatable relatable && this.participants.containsKey(relatable)) {

            if (this.participants.size() <= 2) {

                this.setForDelete();
            } else {

                List<Line> lines = this.participants.get(relatable);

                for (Line line : lines) {
                    line.setForDelete();
                }
            }
        }

    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public List<Component> getComponentsForRemoval() {

        List<Component> out = super.getComponentsForRemoval();

        for (Map.Entry<Relatable, List<Line>> participant : this.participants.entrySet()) {

            List<Line> lines = participant.getValue();

            for (Line line : lines) {
                out.addAll(line.getComponentsForRemoval());
                out.add(line);
            }
        }

        if (this.association != null) {
            out.addAll(this.association.getComponentsForRemoval());
            out.add(this.association);
        }

        return out;
    }

    @Override
    public void drawStartLineToAttribute(Graphics2D g2, Point textPosition) {

        Rectangle bounds = this.getBounds();

        // Vertical line that comes from inside the relationship (in entities is not visible).
        int x = ((int) bounds.getCenterX() + (int) bounds.getMaxX()) / 2;
        int y = (int) bounds.getCenterY();
        g2.drawLine(x, y, x, textPosition.y);

        // Horizontal line that comes from inside the attributable component.
        g2.drawLine(x, textPosition.y, textPosition.x, textPosition.y);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public List<DerivationObject> getDerivationObjects() {

        List<DerivationObject> out = new ArrayList<>();

        PluralDerivation derivation = new PluralDerivation(this.getIdentifier());

        for (Attribute attribute : this.getAttributes(1)) {
            derivation.addAttribute(this, attribute);
        }

        for (Map.Entry<Relatable, List<Line>> participant : this.participants.entrySet()) {

            List<Line> lines = participant.getValue();

            for (Line line : lines) {

                try {
                    Pair<String, String> cardinalities = Cardinality.removeFormat(line.getText());

                    derivation.addMember(new PluralDerivation.Member(
                            ((Derivable) participant.getKey()).getIdentifier(),
                            cardinalities.first(),
                            cardinalities.second()
                    ));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        out.add(derivation);

        return out;
    }

    @Override
    public String getIdentifier() {
        return this.getText();
    }

    /* ---------------------------------------------------------------------------------------------------------- */
    /*                                           Add Relationship                                                 */
    /* ---------------------------------------------------------------------------------------------------------- */

    /**
     * Adds a new <Code>Relationship</Code> to the <Code>this</Code>.
     * <p></p>
     * Between one and three entities (strong or weak) or associations must be selected.
     */
    public static void addRelationship(EERDiagram diagram, List<Component> components) {

        List<Relatable> relatableComponents = components.stream()
                .filter(c -> c instanceof EntityWrapper || c instanceof Association)
                .map(c -> (Relatable) c)
                .toList();

        int numberOfComponents = relatableComponents.size();

        // Not all the components are relatable.
        if (numberOfComponents != components.size() || numberOfComponents < 1 || numberOfComponents > 3) {
            JOptionPane.showMessageDialog(diagram, LanguageManager.getMessage("warning.relationshipCreation"));
            return;
        }

        String name = getValidName(diagram);

        if (name == null) {
            return;
        }

        Relationship newRelationship;

        List<Component> newComponents = new ArrayList<>();

        if (numberOfComponents >= 2) { // Number of components in equal to two or three.

            Point center = diagram.getCenterOfComponents(components);

            newRelationship = new Relationship(name, center.x, center.y, diagram);

            for (Relatable relatable : relatableComponents) {

                Line line = new Line.Builder(
                        diagram,
                        (Component) relatable,
                        newRelationship).build();
                newComponents.add(line);

                Cardinality cardinality;

                if (numberOfComponents == 2) {
                    cardinality = new Cardinality("1", "N", line, diagram);
                } else {
                    cardinality = new Cardinality("0", "N", line, diagram);
                }
                newComponents.add(cardinality);

                // This must be improved later.
                // If an association is related, the line cannot wait until then the association is drawn.
                // It must be drawn first.
                if (relatable instanceof Association) {
                    line.setDrawingPriority(0);
                }

                newRelationship.addParticipant(relatable, line);
            }

            newComponents.add(newRelationship);

        } else { // Number of components is equals to one.

            addReflexiveRelationship(diagram, relatableComponents.getFirst());
        }

        for (Component newComponent : newComponents) {
            diagram.addComponent(newComponent);
        }
    }

    public static void addReflexiveRelationship(EERDiagram diagram, Relatable relatable) {

        List<Component> newComponents = new ArrayList<>();

        String name = getValidName(diagram);

        if (name == null) {
            return;
        }

        Relationship newRelationship = new Relationship(
                name,
                diagram.getMouseX() + 90,
                diagram.getMouseY() - 90,
                diagram
        );

        Line firstLine = new Line.Builder(
                diagram,
                (Component) relatable,
                newRelationship).lineShape(new SquaredLine()).build();
        newComponents.add(firstLine);

        Line secondLine = new Line.Builder(
                diagram,
                newRelationship,
                (Component) relatable).lineShape(new SquaredLine()).build();
        newComponents.add(secondLine);

        Cardinality firstCardinality = new Cardinality("1", "N", firstLine, diagram);
        Cardinality secondCardinality = new Cardinality("1", "N", secondLine, diagram);

        newComponents.add(firstCardinality);
        newComponents.add(secondCardinality);

        newRelationship.addParticipant(relatable, firstLine);
        newRelationship.addParticipant(relatable, secondLine);

        newComponents.add(newRelationship);

        for (Component newComponent : newComponents) {
            diagram.addComponent(newComponent);
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                           Add Dependency                                                       */
    /* -------------------------------------------------------------------------------------------------------------- */

    /**
     * Adds a new <Code>Dependency</Code> to the <Code>this</Code>.
     * <p></p>
     * Two strong entities must be selected.
     */
    public static void addDependency(EERDiagram diagram, List<Component> components) {

        List<EntityWrapper> entities = components.stream()
                .filter(c -> c instanceof EntityWrapper)
                .map(c -> (EntityWrapper) c)
                .toList();

        // entities.size() != components.length when all at least one of the components passed is not an instance
        // of entity wrapper.
        if (entities.size() != components.size() || entities.size() != 2) {
            JOptionPane.showMessageDialog(diagram, LanguageManager.getMessage("warning.dependencyCreation"));
            return;
        }

        String name = getValidName(diagram);

        Point center = diagram.getCenterOfComponents(components);

        Relationship newRelationship = new Relationship(name, center.x, center.y, diagram);

        EntityWrapper entitySelected = selectWeakEntity(entities, diagram);

        if (entitySelected != null) {

            entitySelected.setWeakVersion(newRelationship);

            Cardinality cardinality = null, staticCardinality = null;
            Line strongLine = null, weakLine = null;

            for (EntityWrapper entity : entities) {

                if (entity.equals(entitySelected)) {

                    strongLine = new Line.Builder(
                            diagram,
                            entity,
                            newRelationship
                    ).lineMultiplicity(new DoubleLine(3)).build();

                    cardinality = new Cardinality("1", "N", strongLine, diagram);

                    newRelationship.addParticipant(entity, strongLine);

                } else {

                    weakLine = new Line.Builder(
                            diagram,
                            entity,
                            newRelationship
                    ).build();

                    // A weak entity can only be related to a strong entity if the latter has a 1:1 cardinality.
                    staticCardinality = new StaticCardinality("1", "1", weakLine, diagram);

                    newRelationship.addParticipant(entity, weakLine);
                }
            }

            // These checks are only added so the IDE don't tell me they can be null.

            if (weakLine != null) {
                diagram.addComponent(weakLine);
            }

            if (strongLine != null) {
                diagram.addComponent(strongLine);
            }

            if (cardinality != null) {
                diagram.addComponent(cardinality);
            }

            if (staticCardinality != null) {
                diagram.addComponent(staticCardinality);
            }

            diagram.addComponent(newRelationship);
        }
    }

    /**
     * From the list of selected entities, allows the user to select the weak entity.
     *
     * @return {@code Entity} to be the weak entity of the dependency.
     */
    private static EntityWrapper selectWeakEntity(List<EntityWrapper> entities, EERDiagram diagram) {

        Object[] opciones = {entities.getFirst().getText(),
                entities.getLast().getText()};

        // THe JOptionPane with buttons is shown.
        int selection = JOptionPane.showOptionDialog(
                diagram,
                LanguageManager.getMessage("input.weakEntity"),
                LanguageManager.getMessage("input.option"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        return switch (selection) {
            case 0 -> (entities.getFirst());
            case 1 -> (entities.getLast());
            default -> {
                JOptionPane.showMessageDialog(diagram, LanguageManager.getMessage("input.weakEntity"));
                yield selectWeakEntity(entities, diagram);
            }
        };
    }
}
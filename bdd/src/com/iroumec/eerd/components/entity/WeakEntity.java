package com.iroumec.eerd.components.entity;

import com.iroumec.derivation.Derivable;
import com.iroumec.derivation.Derivation;
import com.iroumec.derivation.elements.SingleElement;
import com.iroumec.eerd.components.relationship.Relationship;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Weak entity. This is an entity whose identification or existence depends on another one.
 */
final class WeakEntity implements Entity {

    final EntityWrapper entityWrapper;

    /**
     * Relationship where the entity is weak.
     */
    private final Relationship relationship;

    /**
     * Constructs a {@code WeakEntity}.
     *
     * @param entityWrapper Wrapper of the entity.
     * @param relationship {@code Relationship} where the entity is weak.
     */
    WeakEntity(EntityWrapper entityWrapper, Relationship relationship) {

        this.entityWrapper = entityWrapper;
        this.relationship = relationship;
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                               Overridden Methods                                               */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public void fillShape(Graphics2D graphics2D, RoundRectangle2D rectangle2D) {

        Rectangle bounds = rectangle2D.getBounds();
        RoundRectangle2D shape = new RoundRectangle2D.Float(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6, 10, 10); // The number of the left must be half the number on the right // The number of the left must be half the number on the right

        graphics2D.setColor(Color.WHITE);
        graphics2D.fill(shape);

        this.entityWrapper.setShape(shape);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public boolean relationshipCanBeManipulated(Relationship relationship) {
        return !this.relationship.equals(relationship);
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public String getIdentifier() {
        return this.entityWrapper.getIdentifier();
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public List<Derivation> getDerivations() {

        List<Derivation> out = new ArrayList<>();

        // TODO: improve this.
        List<Derivable> participants = this.relationship.getParticipants().stream()
                .filter(r -> r instanceof Derivable)
                .map(r -> (Derivable) r)
                .toList();

        if (participants.size() != 2) {
            throw new IllegalArgumentException("Expected 2 participants but got " + participants.size() + ".");
        }

        Derivation derivation = new Derivation(this.getIdentifier());

        for (Derivable participant : participants) {

            if (!participant.equals(this.entityWrapper)) {

                // The strong entity of the relationship has been found.
                derivation.addIdentificationElement(new SingleElement(participant.getIdentifier()));
            }
        }

        return out;
    }
}

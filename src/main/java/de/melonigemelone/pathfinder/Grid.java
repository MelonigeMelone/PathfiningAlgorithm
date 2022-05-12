package de.melonigemelone.pathfinder;

import java.awt.*;
import java.util.Arrays;

/**
 * Grid
 *
 * Grid, das auf Rechtecken basiert
 *
 * @author MelonigeMelone | Tobias
 */
public class Grid {

    /**
     * - Größe eines Rechtecks
     * - Weite des Grids
     * - Höhe des Grids
     */
    private final int size, width, height;

    /**
     * Felder im Grid, die ein Hindernis haben
     */
    private final Vector2D[] blockedFields;

    /**
     * Standard Konstruktor mit automatischen generierten Hindernissen
     *
     * @param size Größe eines Rechtecks
     * @param width Weite des Grids
     * @param height Höhe des Grids
     * @param count Anzahl den gewünschten Hindernissen
     */
    public Grid(int size, int width, int height, int count) {
        this.size = size;
        this.width = width;
        this.height = height;
        this.blockedFields = generateBlockedFields(count);
    }

    /**
     * Standard Konstruktor mit benutzerdefinierten Hindernissen
     *
     * @param size Größe eines Rechtecks
     * @param width Weite des Grids
     * @param height Höhe des Grids
     */
    public Grid(int size, int width, int height, Vector2D[] blockedFields) {
        this.size = size;
        this.width = width;
        this.height = height;
        this.blockedFields = blockedFields;
    }

    /**
     * Generiert automatisch zufällige Hindernisse im Grid
     *
     * @param count Anzahl der Hindernisse
     * @return Liste der Hindernisse
     */
    private Vector2D[] generateBlockedFields(int count) {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
        Vector2D[] blockedFields = new Vector2D[count];
        for(int i = 0; i<count; i++) {
            int randomX = randomNumberGenerator.generate(0, width);
            int randomY = randomNumberGenerator.generate(0, height);
            blockedFields[i] = new Vector2D(randomX, randomY);
        }
        return blockedFields;
    }

    /**
     * Zeichnet die Hindernisse
     *
     * @param graphics Zeichenfläche
     */
    public void drawBlockedFields(Graphics graphics) {
       for(Vector2D blockedField : blockedFields) {
           fillGridPosition(graphics, blockedField, Color.BLACK);
        }
    }

    /**
     * Zeichnet ein Rechteck an einer bestimmten Position im Grid
     *
     * @param graphics Zeichenfläche
     * @param vector2D Position
     * @param color Farbe
     */
    public void fillGridPosition(Graphics graphics, Vector2D vector2D, Color color) {
        graphics.setColor(color);
        graphics.fillRect((int) vector2D.getX() * size, (int) vector2D.getY() * size, size, size);
    }

    /**
     * Ob ein Feld von einem Hindernisse belegt ist
     *
     * @param vector2D Position
     * @return Blockiert
     */
    public boolean isFieldBlocked(Vector2D vector2D) {
        for(Vector2D vector : blockedFields) {
            if(vector2D.equals(vector)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt das Feld an Hindernissen zurück
     *
     * @return eld an Hindernissen
     */
    public Vector2D[] getBlockedFields() {
        return blockedFields;
    }
}

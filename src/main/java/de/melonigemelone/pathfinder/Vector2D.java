package de.melonigemelone.pathfinder;

public class Vector2D {

    private final int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Überprüf, ob ein Objekt diesem Vektor entspricht
     * @param object Objekt, dass mit diesem hier verglichen wird
     * @return Ob die Objekte gleich sind
     */
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Vector2D vector2D)) {
            return false;
        }
        return vector2D.getX() == this.x && vector2D.getY() == this.y;
    }
}

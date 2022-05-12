package de.melonigemelone.pathfinder;

public class Node {

    private final Vector2D vector2D;

    /**
     * gCost Kosten vom Startpunkt
     * hCost Kosten zum Endpunkt
     * fCost beide addiert
     */
    private int fCost, startCost = 0;

    private Node parent;

    public Node(Vector2D vector2D,int fCost) {
        this.vector2D = vector2D;

        this.fCost = fCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public Vector2D getVector2D() {
        return vector2D;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getStartCost() {
        return startCost;
    }

    public void setStartCost(int startCost) {
        this.startCost = startCost;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Node node)) {
            return false;
        }
        return this.getVector2D().equals(node.getVector2D());
    }
}

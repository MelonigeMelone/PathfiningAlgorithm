package de.melonigemelone.pathfinder;


import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pathfinder {

    /**
     * Alle möglichen Nachbar Positionen
     */
    private final Vector2D[] neighbourPositionsWithDiagonal = new Vector2D[] {
            new Vector2D(1,0),
            new Vector2D(0,1),
            new Vector2D(1,1),
            new Vector2D(-1,0),
            new Vector2D(0,-1),
            new Vector2D(-1,-1),
            new Vector2D(1,-1),
            new Vector2D(-1,1),
    };

    /**
     * Aktuell offene Nodes
     */
    private final CopyOnWriteArrayList<Node> openNodes = new CopyOnWriteArrayList<>();

    /**
     * Aktuell geschlossene Nodes
     */
    private final CopyOnWriteArrayList<Node> closedNodes = new CopyOnWriteArrayList<>();

    /**
     * Hindernisse
     */
    private final List<Vector2D> blockedNodes;

    /**
     * Ziel-Node
     */
    private Node finalNode;

    /**
     * Standard Konstruktor
     *
     * @param blockedNodes Hindernisse
     */
    public Pathfinder(List<Vector2D> blockedNodes) {
        this.blockedNodes = blockedNodes;
    }

    /**
     * Sucht einen Weg
     *
     * @param start Startpunkt
     * @param target Zielpunkt
     * @param grid Grid
     * @param graphics Zeichenfläche
     */
    public void findPath(Node start, Node target, Grid grid, Graphics graphics) {
        openNodes.add(start);

        while(true) {
            Node currentNode = getOpenNodeWithLowestFCosts();
            openNodes.remove(currentNode);
            closedNodes.add(currentNode);
            grid.fillGridPosition(graphics, currentNode.getVector2D(), Color.PINK);

            //Pfad gefunden
            if(currentNode.equals(target)) {
                finalNode = currentNode;
                drawFinalPath(grid, graphics);
                return;
            }

            Vector2D[] neighbours = getNeighboursNodesPositions(currentNode.getVector2D());
            for(Vector2D vector2D : neighbours) {
                if(!checkIfNodeIsBlocked(vector2D) && !checkIfNodeIsInClosedList(vector2D)) {
                    if(checkIfNodeIsInOpenList(vector2D)) {
                        Node node = getNodeFromPosition(vector2D);
                        int newFCosts = calculateNodeCosts(start, target, Objects.requireNonNull(node));
                        if(newFCosts < node.getFCost()) {
                            node.setParent(currentNode);
                            node.setFCost(newFCosts);
                        }
                    } else {
                        Node node = new Node(vector2D, 0);
                        node.setParent(currentNode);
                        int newFCosts = calculateNodeCosts(start, target, node);
                        node.setFCost(newFCosts);
                        openNodes.add(node);
                        grid.fillGridPosition(graphics, node.getVector2D(), Color.YELLOW);
                    }
                }
            }
        }
    }

    /**
     * Zeichnet den finalen Pfad
     *
     * @param grid Grid
     * @param graphics Zeichenfläche
     */
    private void drawFinalPath(Grid grid, Graphics graphics) {
        Node currentNode = finalNode;
        while(currentNode.getParent() != null) {
            grid.fillGridPosition(graphics, currentNode.getVector2D(), Color.BLUE);
            System.out.println(currentNode.getFCost());
            currentNode = currentNode.getParent();
        }
    }

    /**
     * Gibt die Node mit den kleinsten Kosten zurück, die noch offen ist
     *
     * @return Node mit den kleinsten Kosten
     */
    private Node getOpenNodeWithLowestFCosts() {
        Node tempNode = openNodes.get(0);

        for (Node openNode : openNodes) {
            if (openNode.getFCost() < tempNode.getFCost()) {
                tempNode = openNode;
            }
        }
        return tempNode;
    }

    /**
     * Gibt alle Nachbar-Positionen einer Position zurück
     *
     * @param currentPosition aktuelle Position
     * @return Feld an Nachbar-Positionen
     */
    private Vector2D[] getNeighboursNodesPositions(Vector2D currentPosition) {
        Vector2D[] positions = new Vector2D[8];
        for(int i = 0; i< neighbourPositionsWithDiagonal.length; i++) {
            positions[i] = new Vector2D(currentPosition.getX() + neighbourPositionsWithDiagonal[i].getX(), currentPosition.getY() + neighbourPositionsWithDiagonal[i].getY());
        }
        return positions;
    }

    /**
     * Überprüft, ob die Position geblockt ist
     *
     * @param vector2D Position
     * @return geblockt
     */
    private boolean checkIfNodeIsBlocked(Vector2D vector2D) {
        for(Vector2D vector2D1 : blockedNodes) {
            if(vector2D1.equals(vector2D)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft, ob die Position bereits geschlossen ist
     *
     * @param vector2D Position
     * @return geschlossen
     */
    private boolean checkIfNodeIsInClosedList(Vector2D vector2D) {
        for(Node node : closedNodes) {
            if(node.getVector2D().equals(vector2D)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft, ob die Position offen ist
     *
     * @param vector2D Position
     * @return offen
     */
    private boolean checkIfNodeIsInOpenList(Vector2D vector2D) {
        for(Node node : openNodes) {
            if(node.getVector2D().equals(vector2D)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt die Node einer gewissen Position zurück
     *
     * @param vector2D Position
     * @return Node
     */
    private Node getNodeFromPosition(Vector2D vector2D) {
        for(Node node : openNodes) {
            if(node.getVector2D().equals(vector2D)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Berechnet die Kosten einer Node
     *
     * @param start Startpunkt
     * @param target Zielpunkt
     * @param node aktuelle Node
     * @return Kosten
     */
    private int calculateNodeCosts(Node start, Node target, Node node) {
        Node tempNode = node, tempParent = node.getParent();

        int parentCosts = tempParent.getStartCost();
        int nodeCosts = calculateCostsBetweenNodes(tempNode, tempParent);
        int gCost = parentCosts + nodeCosts;
        node.setStartCost(gCost);

        /*
        Berechnet die Startkosten

        Hierfür wird von der aktuellen Node der Parent genommen und die Distanz berechnet, diese dann zu den Gesamtkosten addiert.
        Danach vom Parent der Parent solange bis man wieder am Start angekommen ist.
         */
     /*   while(!end) {
            if(tempNode.getVector2D().equals(start.getVector2D())) {
                end = true;
            } else {
                int tempCosts = calculateCostsBetweenNodes(tempNode, tempParent);
                gCost += tempCosts;
                tempNode = tempParent;
                tempParent = tempNode.getParent();
            }
        }*/

        //Berechnung der Kosten zum Zielpunkt
        int hCost = calculateCostsBetweenNodes(node, target);
        System.out.println(gCost);
        return gCost + hCost;
    }

    /**
     * Berechnet die Kosten zweier Nodes
     *
     * @param one erste Node
     * @param two zweite Node
     * @return Kosten
     */
    private int calculateCostsBetweenNodes(Node one, Node two) {

        //Differenz als Betrag
        Vector2D difference = new Vector2D(Math.abs(two.getVector2D().getX() - one.getVector2D().getX()), Math.abs(two.getVector2D().getY() - one.getVector2D().getY()));

        /*
        Nun wird der kleinere Wert ermittelt von X und Y. Für die Anzahl des kleineren Weges wird dann die Stecke diagonal abgelaufen und dabei für X und Y sowohl 1 Einheit pro
        Durchlauf abgezogen. Sowie die entsprechende Länge von 14 addiert. Danach werden die übrigen Schritte des größeren Wertes gerade abgearbeitet und als Distanz dafür 10
        addiert.
         */
        int costs = 0;
        int min = difference.getX();
        int max = difference.getY();

        if(difference.getY() < difference.getX()) {
            min = difference.getY();
            max = difference.getX();
        }

        for(int i = 0; i<min; i++) {
            costs += 14;
        }

        max -= min;


        for(int i = 0; i<max; i++) {
            costs += 10;
        }

        return costs;
    }
}

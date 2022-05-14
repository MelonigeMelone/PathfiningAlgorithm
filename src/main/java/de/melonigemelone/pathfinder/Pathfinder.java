package de.melonigemelone.pathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pathfinder {

    /**
     * Alle möglichen Nachbar Positionen
     */
    private static final Vector2D[] neighbourPositionsWithDiagonal = new Vector2D[] {
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
     * Weite und Höhe des Grids
     */
    private static int gridWidth, gridHeight;

    /**
     * Hindernisse
     */
    private static List<Vector2D> blockedNodes;

    /**
     * Aktuell offene Nodes
     */
    private static final CopyOnWriteArrayList<Node> openNodes = new CopyOnWriteArrayList<>();

    /**
     * Aktuell geschlossene Nodes
     */
    private static final CopyOnWriteArrayList<Node> closedNodes = new CopyOnWriteArrayList<>();

    /**
     * Ziel-Node
     */
    private static Node finalNode;

    /**
     * Initialisiert den Pathfinder
     *
     * @param gridWidth Weite des Grids
     * @param gridHeight Höhe des Grids
     * @param blockedPositions Geblockte Positionen
     */
    public static void init(int gridWidth, int gridHeight, List<Vector2D> blockedPositions) {
        Pathfinder.gridWidth = gridWidth;
        Pathfinder.gridHeight = gridHeight;
        Pathfinder.blockedNodes = blockedPositions;
    }

    /**
     * Sucht einen Weg
     *
     * @param startPos Startpunkt
     * @param targetPos Zielpunkt
     * @param size Größe eines Objekts
     */
    public static Vector2D[] findPath(Vector2D startPos, Vector2D targetPos, double size) {
        Node start = new Node(startPos, 0);
        Node target = new Node(targetPos, 0);
        openNodes.add(start);

        while(true) {
            Node currentNode = getOpenNodeWithLowestFCosts();
            openNodes.remove(currentNode);
            closedNodes.add(currentNode);

            //Pfad gefunden
            if(currentNode.equals(target)) {
                finalNode = currentNode;
                return getWaypoints();
            }

            Vector2D[] neighbours = getNeighboursNodesPositions(currentNode.getVector2D());
            for(Vector2D vector2D : neighbours) {
                if(!checkIfNodeIsBlocked(vector2D) && !checkIfNodeIsInClosedList(vector2D)) {
                    if (checkSize(vector2D, size)) {
                        if (checkIfNodeIsInOpenList(vector2D)) {
                            Node node = getNodeFromPosition(vector2D);
                            int newFCosts = calculateNodeCosts(target, Objects.requireNonNull(node));
                            if (newFCosts < node.getFCost()) {
                                node.setParent(currentNode);
                                node.setFCost(newFCosts);
                            }
                        } else {
                            Node node = new Node(vector2D, 0);
                            node.setParent(currentNode);
                            int newFCosts = calculateNodeCosts(target, node);
                            node.setFCost(newFCosts);
                        }
                    }
                }
            }
        }
    }

    /**
     * Überprüft, ob die Umgebung um die Position herum groß genug für das Objekt ist
     *
     * @param pos momentane Position
     * @param size Größe des Objektes
     */
    private static boolean checkSize(Vector2D pos, double size) {
        int loop = (int) Math.round(size / 2d);

        for(int i = 0; i<loop; i++) {
            for(int x = -loop; x<=i; x++) {
                for(int y = -loop; y<=i; y++) {
                    Vector2D vector2D = new Vector2D(pos.getX()+x, pos.getY()+y);
                    if(vector2D.getX() < gridWidth && vector2D.getX() > 0 && vector2D.getY() < gridHeight && vector2D.getY() > 0) {
                        if (checkIfNodeIsBlocked(vector2D)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Gibt alle Wegpunkte zurück
     *
     * @return Liste aller Wegpunkte
     */
    private static Vector2D[] getWaypoints() {
        List<Vector2D> list = new ArrayList<>();

        Node currentNode = finalNode;
        while(currentNode.getParent() != null) {
            list.add(currentNode.getVector2D());
            currentNode = currentNode.getParent();
        }

        Vector2D[] waypoints = new Vector2D[list.size()];
        int counter = list.size()-1;
        for(Vector2D vector2D : list) {
            waypoints[counter] = vector2D;
            counter--;
        }

        return waypoints;
    }

    /**
     * Gibt die Node mit den kleinsten Kosten zurück, die noch offen ist
     *
     * @return Node mit den kleinsten Kosten
     */
    private static Node getOpenNodeWithLowestFCosts() {
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
    private static Vector2D[] getNeighboursNodesPositions(Vector2D currentPosition) {
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
    private static boolean checkIfNodeIsBlocked(Vector2D vector2D) {
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
    private static boolean checkIfNodeIsInClosedList(Vector2D vector2D) {
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
    private static boolean checkIfNodeIsInOpenList(Vector2D vector2D) {
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
    private static Node getNodeFromPosition(Vector2D vector2D) {
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
     * @param target Zielpunkt
     * @param node aktuelle Node
     * @return Kosten
     */
    private static int calculateNodeCosts(Node target, Node node) {
        Node tempParent = node.getParent();

        int parentCosts = tempParent.getStartCost();
        int nodeCosts = calculateCostsBetweenNodes(node, tempParent);
        int gCost = parentCosts + nodeCosts;
        node.setStartCost(gCost);

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
    private static int calculateCostsBetweenNodes(Node one, Node two) {

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

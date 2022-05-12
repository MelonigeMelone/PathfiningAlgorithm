package de.melonigemelone.pathfinder;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Window extends JFrame {

    private final Grid grid;

    private Pathfinder pathfinder;
    private final Vector2D start = new Vector2D(15,15), finish = new Vector2D(1500,800);

    public Window() {

        this.grid = new Grid(1,1600,900, 500000);
      /*  this.grid = new Grid(10,80,80, new Vector2D[] {
                new Vector2D(12,12),
                new Vector2D(13,12),
                new Vector2D(14,12),
                new Vector2D(15,12),
                new Vector2D(16,11),
                new Vector2D(16,12),
                new Vector2D(16,10),
                new Vector2D(12,9),
                new Vector2D(13,9),
                new Vector2D(14,9),
                new Vector2D(15,9),

        });*/
       // this.pathFinder = new PathFinder(grid,start, finish, true );
        pathfinder = new Pathfinder(Arrays.asList(grid.getBlockedFields()));


        setTitle("Tets");
        setSize(new Dimension(1600,900));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        pathfinder.findPath(new Node(start, 0), new Node(finish, 0), grid, getGraphics());

    }

    @Override
    public void paint(Graphics graphics) {
       // grid.drawGrid(graphics);
        grid.drawBlockedFields(graphics);
        grid.fillGridPosition(graphics, start, Color.GREEN);
        grid.fillGridPosition(graphics, finish, Color.RED);


       // pathFinder.findPath(graphics,  start, true, Color.YELLOW);

    }
}

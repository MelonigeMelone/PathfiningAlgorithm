package de.melonigemelone.pathfinder;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Arrays;

public class Window extends JFrame {

    private final Grid grid;

    private Pathfinder pathfinder;
    private final Vector2D start = new Vector2D(15,15), finish = new Vector2D(50,15);

    public Window() {

     //   this.grid = new Grid(10,160,90, 200);
       this.grid = new Grid(10,80,80, new Vector2D[] {
                new Vector2D(20,13),
                new Vector2D(20,14),
                new Vector2D(20,17),
                new Vector2D(20,18)

        });
       // this.pathFinder = new PathFinder(grid,start, finish, true );


        setTitle("Tets");
        setSize(new Dimension(1600,900));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

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

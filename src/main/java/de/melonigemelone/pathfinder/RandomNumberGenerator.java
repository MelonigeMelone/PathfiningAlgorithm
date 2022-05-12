package de.melonigemelone.pathfinder;

/**
 * Generiert eine zufällige Zahl
 */
public class RandomNumberGenerator {

    /**
     * Erstellt eine zufällige Zahl zwischen zwei Werten
     * @param min Minimum
     * @param max Maximum (diese Zahl kommt nicht mehr vor)
     * @return Zufällig generierte Zahl
     */
    public int generate(int min, int max) {
        return (int) (min + (Math.random() * max));
    }
}

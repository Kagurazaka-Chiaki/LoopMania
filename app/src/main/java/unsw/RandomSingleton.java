package unsw;

import java.util.Random;

/**
 * <p>
 * Class {@code RandomSingleton}
 * </p>
 * <p>
 * singleton pattern for Random
 *
 * @see unsw.RandomSingleton
 * @since 1.0
 **/
public class RandomSingleton {
    private static RandomSingleton randomSingleton = null;

    private final Random random;

    private RandomSingleton() {
        random = new Random();
    }

    /**
     * Method that ensure that only construct one random
     *
     * @return //
     */
    public static synchronized RandomSingleton getInstance() {
        if (randomSingleton == null) {

            randomSingleton = new RandomSingleton();
        }
        return randomSingleton;
    }

    /**
     * Getter that get random
     *
     * @return //
     */
    public Random getRandom() {
        return random;
    }
}

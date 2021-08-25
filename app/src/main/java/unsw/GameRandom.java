package unsw;

import java.util.Random;

/**
 * <p>
 * Class {@code GameRandom}
 * </p>
 * <p>
 * Random
 * @see unsw.GameRandom
 * @since 1.0
 **/
public class GameRandom {
    public static Random random = RandomSingleton.getInstance().getRandom();
}

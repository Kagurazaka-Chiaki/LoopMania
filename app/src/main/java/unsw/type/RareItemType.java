package unsw.type;

import unsw.GameRandom;

public enum RareItemType {
    TheOneRing, TreeStump, Anduril;

    public static RareItemType getRandomItemType() {
        return values()[GameRandom.random.nextInt(values().length)];
    }
}

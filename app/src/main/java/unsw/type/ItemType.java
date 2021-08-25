package unsw.type;

import unsw.GameRandom;

public enum ItemType {
    Armour, Helmet, Shield, Sword, Stake, Staff;

    public static ItemType getRandomItemType() {
        return values()[GameRandom.random.nextInt(values().length)];
    }
}

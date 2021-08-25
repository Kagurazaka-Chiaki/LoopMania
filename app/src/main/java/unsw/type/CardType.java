package unsw.type;

import unsw.GameRandom;

public enum CardType {
    VampireCastleCard, ZombiePitCard, TowerCard, VillageCard, BarracksCard, TrapCard, CampfireCard, MagicShopCard;

    public static CardType getRandomCardType() {
        int possibility = GameRandom.random.nextInt(100);
        if (possibility >= 95) {
            return MagicShopCard;
        } else {
            return values()[GameRandom.random.nextInt(values().length - 1)];
        }
    }
}

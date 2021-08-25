package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.*;

public class BuildingSystem {
    private final List<Building> buildingEntities;
    private final List<BarracksBuilding> barracks;
    private final List<TowerBuilding> towers;
    private final List<TrapBuilding> traps;
    private final List<MagicShopBuilding> shops;
    private final LoopManiaWorld world;

    public BuildingSystem(LoopManiaWorld world) {
        this.buildingEntities = new ArrayList<>();
        this.barracks = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.shops = new ArrayList<>();
        this.world = world;
    }

    /**
     * remove a card by its x, y coordinates
     *
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates( //
        int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        Card card = null;
        for (Card c : world.getCardEntities()) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        // Pair<Integer, Integer> cardPosition = Pair.with(cardEntities.size(), 0);

        // if something wrong
        if (card == null) { System.err.println("no such card type!!!"); }
        assert card != null;

        // now spawn building
        String cardName = card.getCardName();
        boolean permissionBuild = checkValidPos(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);

        Building newBuilding = null;
        // newBuilding = card.convertToBuilding(Pair.with(buildingNodeX, buildingNodeY));

        if (permissionBuild) {
            newBuilding = card.convertToBuilding(Pair.with(buildingNodeX, buildingNodeY));

            switch (cardName) {
                case "BarracksCard":
                    barracks.add((BarracksBuilding) newBuilding);
                    break;
                case "TowerCard":
                    towers.add((TowerBuilding) newBuilding);
                    break;
                case "TrapCard":
                    traps.add((TrapBuilding) newBuilding);
                    break;
                case "MagicShopCard":
                    shops.add((MagicShopBuilding) newBuilding);
                    break;
                default:
                    break;
            }
            buildingEntities.add(newBuilding);

            card.destroy();
            world.getCardEntities().remove(card);
            world.shiftCardsDownFromXCoordinate(cardNodeX);
        }

        return newBuilding;
    }

    /**
     * chekc whether is a valid position
     *
     * @param sourceX //
     * @param sourceY //
     * @param targetX //
     * @param targetY //
     **/
    public boolean checkValidPos(Integer sourceX, Integer sourceY, Integer targetX, Integer targetY) {
        // start by getting card
        Card card = null;
        for (Card c : world.getCardEntities()) {
            if ((c.getX() == sourceX) && (c.getY() == sourceY)) {
                card = c;
                break;
            }
        }

        if (card == null) {
            System.out.println("No Such Card");
            return false;
        }
        // assert card != null;

        // cannot build on heroCastle position
        if (targetX == world.getHeroCastle().getX() && targetY == world.getHeroCastle().getY()) { return false; }
        //if target position has been already built a building
        for (Building b : buildingEntities) {
            if (b.getX() == targetX && b.getY() == targetY) { return false; }
        }

        boolean onNearPath = world.getPosition().isNearby(Pair.with(targetX, targetY));
        boolean onPath = world.getPosition().isPath(Pair.with(targetX, targetY));
        boolean onWild = !onNearPath && !onPath;

        boolean permissionBuild = false;
        if (onNearPath && card.getNearPath()) {
            permissionBuild = true;
        } else if (onPath && card.getPath()) {
            permissionBuild = true;
        } else if (onWild && card.getWild()) { permissionBuild = true; }
        return permissionBuild;
    }

    /**
     * Set buildiing buff
     **/
    public void setBuff() {
        int campNum = 0;
        for (Building b : getBuildingEntities()) {
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            if (!(b instanceof CampFireBuilding)) { continue; }
            if (Math.pow((world.getCharacter().getX() - b.getX()), 2) + Math.pow(
                (world.getCharacter().getY() - b.getY()), 2) < 9) {
                world.getCharacter().IfBuff().set(true);
                // character.strength().set(character.getStrength() * 2);
                campNum++;
                break;
            }
        }

        if (campNum == 0) { world.getCharacter().IfBuff().set(false); }

    }

    /**
     * check whether character is passing a village
     **/
    public void checkCharacterPassVillage() {
        for (Building b : getBuildingEntities()) {
            if (!(b instanceof VillageBuilding)) { continue; }
            // if character is in the village
            if (world.getCharacter().getX() == b.getX() && world.getCharacter().getY() == b.getY()) {
                if (world.getCharacter().getStatus().getCurrentHealthValue() < world.getCharacter().getStatus()
                                                                                    .getHealth() * 0.9) {
                    world.getCharacter().getStatus().setCurrentHealth(
                        world.getCharacter().getStatus().getCurrentHealthValue() + world.getCharacter().getStatus()
                                                                                        .getHealth() * 0.1);
                } else {
                    world.getCharacter().getStatus().setCurrentHealth(world.getCharacter().getStatus().getHealth());
                }
            }
        }
    }

    /**
     * check whether character is passing a MagicShop
     **/
    public boolean checkCharacterPassMagicShop() {
        for (Building b : getBuildingEntities()) {
            if (!(b instanceof MagicShopBuilding)) { continue; }
            // if character is in the village
            if (world.getCharacter().getX() == b.getX() && world.getCharacter().getY() == b.getY()) {
                removeBuilding(b);
                return true;
            }
        }
        return false;
    }

    /**
     * Method that remove shop after used
     *
     * @param b //
     */
    public void removeBuilding(Building b) {
        if (world.getBuildingSystem().getShops().contains(b)) {
            world.getBuildingSystem().getShops().remove(b);
            world.getBuildingSystem().getBuildingEntities().remove(b);
            b.destroy();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter that get the list of building
     *
     * @return //
     */
    public List<Building> getBuildingEntities() {
        return this.buildingEntities;
    }

    /**
     * Getter that get the list of barracks
     *
     * @return //
     */
    public List<BarracksBuilding> getBarracks() {
        return barracks;
    }

    /**
     * Getter that get the list of tower
     *
     * @return //
     */
    public List<TowerBuilding> getTowers() {
        return towers;
    }

    /**
     * Getter that get the list of traps
     *
     * @return //
     */
    public List<TrapBuilding> getTraps() {
        return traps;
    }

    /**
     * Getter that get the list of shops
     *
     * @return //
     */
    public List<MagicShopBuilding> getShops() {
        return shops;
    }
}

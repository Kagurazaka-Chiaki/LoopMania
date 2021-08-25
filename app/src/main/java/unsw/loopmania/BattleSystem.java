package unsw.loopmania;

import org.javatuples.Pair;
import unsw.GameRandom;
import unsw.entity.BasicEnemy;
import unsw.entity.Building;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.item.Anduril;
import unsw.entity.building.TowerBuilding;
import unsw.entity.building.TrapBuilding;
import unsw.entity.enemy.*;
import unsw.type.BuildingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Class {@code BattleSystem}
 * </p>
 *
 * <p>
 * System of battle
 * </p>
 *
 * @see unsw.loopmania.BattleSystem
 * @since 1.0
 **/
public class BattleSystem {

    private final LoopManiaWorld world;
    private final List<BasicEnemy> enemies;
    private final List<AlliedSoldier> soldiers;
    private Character character;
    private StringBuilder battleInfo;
    private List<AlliedSoldier> alliedSoldiers;
    private final List<BasicEnemy> battleEnemies;
    private final List<BasicEnemy> trancedEnemies;
    private final List<AlliedSoldier> tempSoldiers;
    private final List<BasicEnemy> infectedSoldier;

    /**
     * <p>
     * Constructor {@code BattleSystem}
     * </p>
     * <p>
     * Initialises the new BattleSystem with the given parameters
     *
     * @param world //
     **/
    public BattleSystem(LoopManiaWorld world) {
        this.world = world;
        this.enemies = new ArrayList<>();
        this.soldiers = new ArrayList<>();
        this.battleInfo = new StringBuilder();

        battleEnemies = new ArrayList<>();
        alliedSoldiers = new ArrayList<>();
        trancedEnemies = new ArrayList<>();
        tempSoldiers = new ArrayList<>();
        infectedSoldier = new ArrayList<>();
    }

    /**
     * move all enemies
     */
    public void moveBasicEnemies() {
        for (BasicEnemy e : enemies) {
            e.move();
        }
    }

    /**
     * remove Soldier at a particular index of Soldiers (position in gridpane of Soldiers)
     *
     * @param index the index of the card, from 0 to 4
     */
    public void removeSoldier(int index) {
        AlliedSoldier a = soldiers.get(index);
        int x = a.getX();
        a.destroy();
        soldiers.remove(index);
        character.getAlliedSoldiers().remove(index);
        shiftSoldiersDownFromXCoordinate(x);
    }

    /**
     * shift card coordinates down starting from x coordinate
     *
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftSoldiersDownFromXCoordinate(int x) {
        int i;
        for (AlliedSoldier c : soldiers) {
            if (c.getX() >= x) {
                i = c.getX() - 1;
                c.x().set(i);
                c.setName("Soldier " + (i + 1));
            }
        }
    }

    /**
     * spawn a soldier in the world and return the soldier entity
     *
     * @return a soldier to be spawned in the controller as a JavaFX node
     * Max number of soldiers will be 5
     */
    public AlliedSoldier loadSoldier() {
        // if adding more cards than have, remove the first card...
        if (soldiers.size() >= 5) { removeSoldier(0); }

        AlliedSoldier newSoldier;
        Pair<Integer, Integer> soldierPosition = Pair.with(soldiers.size(), 0);
        newSoldier = new AlliedSoldier(soldierPosition, world);
        soldiers.add(newSoldier);
        int num = soldiers.size();
        String name = "Soldier " + num;
        newSoldier.setName(name);
        character.getAlliedSoldiers().add(newSoldier);
        return newSoldier;
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     *
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies() {
        int choice = GameRandom.random.nextInt(2);
        Pair<Integer, Integer> pos = world.possiblySpawnPosition(choice, enemies.size(), 3);
        // Pair<Integer, Integer> pos = possiblyGetBasicEnemySpawnPosition();
        List<BasicEnemy> spawningEnemies = new ArrayList<>();
        if (pos != null) {
            int indexInPath = world.getOrderedPath().indexOf(pos);
            PathPosition enemyPosition = new PathPosition(indexInPath, world.getOrderedPath());
            int possiblySpawnThief = GameRandom.random.nextInt(100);
            BasicEnemy enemy = null;
            if (world.getCycle().get() >= 10 && possiblySpawnThief >= 50) {
                enemy = new Thief(enemyPosition, world);
            } else {
                enemy = new Slug(enemyPosition, world);
            }
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }
        return spawningEnemies;
    }

    /**
     * generate boss
     *
     * @return list of the boss
     */
    public List<BasicEnemy> generateBoss() {
        List<BasicEnemy> boss = new ArrayList<>();
        Pair<Integer, Integer> pos = world.possilyPositionForBoss();
        int indexInPath = world.getOrderedPath().indexOf(pos);
        PathPosition enemyPosition = new PathPosition(indexInPath, world.getOrderedPath());
        if (world.getCycle().get() % 3 == 0 && world.getCycle().get() >= 21) {
            Doggie doggie = new Doggie(enemyPosition, world);
            boss.add(doggie);
            enemies.add(doggie);
        }

        if (world.getCycle().get() % 10 == 1 && world.getCycle().get() >= 41 && character.getExpValue() >= 10000) {
            Pair<Integer, Integer> pos2 = world.possilyPositionForBoss();
            int indexInPath2 = world.getOrderedPath().indexOf(pos2);
            PathPosition enemyPosition2 = new PathPosition(indexInPath2, world.getOrderedPath());
            ElanMuske elan = new ElanMuske(enemyPosition2, world);
            boss.add(elan);
            enemies.add(elan);
        }
        return boss;
    }

    /**
     * generate strong enemy near building
     **/
    public List<BasicEnemy> gengrateStrongEnemies() {
        List<BasicEnemy> strongEnemies = new ArrayList<>();
        for (Building b : world.getBuildingSystem().getBuildingEntities()) {
            if (b.getBuildingType() == BuildingType.ZombiePitBuilding && world.getCycle().get() >= 4) {
                BasicEnemy newEnemy = generateEnemyNearBuilding(b);
                strongEnemies.add(newEnemy);

            } else if ((b.getBuildingType() == BuildingType.VampireCastleBuilding) && ((world.getCycle()
                                                                                             .get()) % 5 == 0)) {
                BasicEnemy newEnemy = generateEnemyNearBuilding(b);
                strongEnemies.add(newEnemy);
            }
        }

        return strongEnemies;
    }

    /**
     * generate enemy near building
     *
     * @param building //
     **/
    private BasicEnemy generateEnemyNearBuilding(Building building) {
        int x = building.getX();
        int y = building.getY();
        List<Pair<Integer, Integer>> nearby = Arrays.asList( //
            world.getPosition().getN(x, y), world.getPosition().getS(x, y), //
            world.getPosition().getW(x, y), world.getPosition().getE(x, y), //
            world.getPosition().getSE(x, y), world.getPosition().getSW(x, y), //
            world.getPosition().getNE(x, y), world.getPosition().getNW(x, y) //
        );

        List<Pair<Integer, Integer>> temp = new ArrayList<>();
        for (Pair<Integer, Integer> n : nearby) {
            if (world.getPosition().isPath(n)) { temp.add(n); }
        }

        int size = temp.size();
        Pair<Integer, Integer> generatePosition = temp.get(GameRandom.random.nextInt(size));
        int indexInPath = world.getOrderedPath().indexOf(generatePosition);
        PathPosition enemyPosition = new PathPosition(indexInPath, world.getOrderedPath());

        BuildingType buildingType = building.getBuildingType();

        switch (buildingType) {
            case ZombiePitBuilding:
                Zombie zombie = new Zombie(enemyPosition, world);
                enemies.add(zombie);
                return zombie;
            case VampireCastleBuilding:
                Vampire vampire = new Vampire(enemyPosition, world);
                enemies.add(vampire);
                return vampire;
            default:
                return null;
        }
    }

    /**
     * kill an enemy
     *
     * @param enemy enemy to be killed
     */
    private void killEnemy(BasicEnemy enemy) {
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * check whether is in battle in range
     *
     * @param character //
     * @param enemy     //
     **/
    private Boolean inBattleRange(Character character, BasicEnemy enemy) {
        return ((Math.pow((character.getX() - enemy.getX()), 2) + Math.pow((character.getY() - enemy.getY()),
            2)) <= Math.pow(enemy.getBattleRadius(), 2));
    }

    /**
     * add support enemies
     *
     * @param enemy         //
     * @param battleEnemies //
     **/
    private void addSupportEnemies(BasicEnemy enemy, List<BasicEnemy> battleEnemies) {
        for (BasicEnemy e : enemies) {
            if (inSupportRange(character, e) && (e != enemy)) {
                if (Objects.equals(e.getType(), "ElanMuske")) {
                    int random = GameRandom.random.nextInt(2);
                    if (random == 1) {
                        battleEnemies.add(e);
                    }
                } else {
                    battleEnemies.add(e);
                }
            }
        }
    }

    /**
     * check whether is in support range
     *
     * @param character //
     * @param enemy     //
     **/
    private Boolean inSupportRange(Character character, BasicEnemy enemy) {
        return ((Math.pow((character.getX() - enemy.getX()), 2) + Math.pow((character.getY() - enemy.getY()),
            2)) <= Math.pow(enemy.getSupportRadius(), 2));
    }

    /**
     * generate enemy information
     */
    public String enemyInfo(BasicEnemy enemy) {
        StringBuilder result;
        result = new StringBuilder();
        result.append("Enemy: ").append(enemy.getType()).append("\n");
        result.append("Enemy Defence: ").append(mathRound(enemy.getStatus().getDefense(), 2)).append("\n");
        result.append("Enemy Strength: ").append(mathRound(enemy.getStatus().getStrength(), 2)).append("\n");
        result.append("Enemy HP: ").append(mathRound(enemy.getStatus().getHealth(), 2)).append("\n");
        return result.toString();
    }

    /**
     * run the expected battles in the world, based on current world state
     *
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles() {
        // currently the character automatically wins all battles without any damage!

        alliedSoldiers = character.getAlliedSoldiers();
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();

        for (BasicEnemy e : enemies) {
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            if (inBattleRange(character, e)) {
                if (Objects.equals(e.getType(), "ElanMuske")) {
                    int random = GameRandom.random.nextInt(2);
                    if (random == 1) {
                        battleEnemies.add(e);
                        addSupportEnemies(e, battleEnemies);
                        break;
                    }
                } else {
                    battleEnemies.add(e);
                    addSupportEnemies(e, battleEnemies);
                    break;
                }
            }
        }

        if (battleEnemies.size() > 0) {
            getBattleInfo().append("***************************** New Battle *****************************\n");
            for (; ; ) {
                // print information to the text area
                BasicEnemy enemyTarget = character.chooseAttack();

                if (!enemyTarget.getStatus().isAlive() && !trancedEnemies.contains(enemyTarget)) {
                    battleEnemies.remove(enemyTarget);
                    defeatedEnemies.add(enemyTarget);
                    battleInfo.append("Character ---- kill ---->  Enemy: ").append(enemyTarget.getType()).append("\n");
                }

                processAlliedAttack(defeatedEnemies, alliedSoldiers);
                processAlliedAttack(defeatedEnemies, tempSoldiers);

                processEnemyAttack();

                battleEnemies.addAll(infectedSoldier);
                infectedSoldier.clear();
                if (world.checkGameIsOver()) { break; }
                if (battleEnemies.size() == 0) { break; }
            }
        }

        processDefeatedEnemy(defeatedEnemies);

        for (BasicEnemy e : trancedEnemies) {
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from
            // the enemies list
            // if we killEnemy in prior loop, we get
            // java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            // battle(character, e);
            killEnemy(e);
            battleInfo.append(" battle is over, ").append(e.getType()).append(" is killed as it is still tranced\n");
            defeatedEnemies.add(e);
        }

        world.setInfo(battleInfo.toString());
        battleInfo.setLength(0);
        character.gainEXP(defeatedEnemies);
        trancedEnemies.clear();
        tempSoldiers.clear();
        return defeatedEnemies;
    }

    /**
     * Processing allied soldier attack
     **/
    private void processAlliedAttack(List<BasicEnemy> defeatedEnemies, List<AlliedSoldier> soldiers) {
        for (AlliedSoldier s : soldiers) {
            if (battleEnemies.size() > 0) {
                BasicEnemy soldierTarget = s.chooseAttack(battleEnemies);
                if (soldierTarget.getStatus().isAlive()) {
                    battleInfo.append("AlliedSoldier: ").append(s.getName()).append(" ---- attack ---->")
                              .append(soldierTarget.getType()).append(" Current Health:") //
                              .append(mathRound(soldierTarget.getStatus().getCurrentHealthValue(), 2)) //
                              .append("\n");
                } else {
                    battleEnemies.remove(soldierTarget);
                    defeatedEnemies.add(soldierTarget);
                    battleInfo.append("AlliedSoldier: ").append(s.getName()).append("---- kill ---->  Enemy: ")
                              .append(soldierTarget.getType()).append("\n");
                }
            }
        }
    }

    /**
     * Processing enemy attack
     **/
    private void processEnemyAttack() {
        for (BasicEnemy e : battleEnemies) {
            int sequence = GameRandom.random.nextInt(alliedSoldiers.size() + 1);
            if (sequence < alliedSoldiers.size()) {
                AlliedSoldier soldierTarget = alliedSoldiers.get(sequence);
                e.ChooseAttack(soldierTarget, sequence);
            } else { // Attack the character
                double curStrength = character.getStatus().getStrength();
                double curdefense = character.getStatus().getDefense();
                Item weapon = character.getWeapon();
                boolean abilityTrigger = false;

                if (character.getShield() != null) {
                    abilityTrigger = character.getShield().useAbility(character, e);
                }
                if (!abilityTrigger && weapon != null && Anduril.class.equals(weapon.getClass())) {
                    character.getWeapon().useAbility(character, e);
                }
                e.ChooseAttack(character);

                character.getStatus().setDefense(curdefense);
                character.getStatus().setStrength(curStrength);

                if (!character.getStatus().isAlive() && !reviveCharacter()) {
                    world.setGameOver(true);
                    break;
                }
            }
        }
    }

    /**
     * revive character
     *
     * @return boolean //
     */
    private boolean reviveCharacter() {
        for (Item item : world.getItemSystem().getUnequippedInventory()) {
            if (item.revive(character)) {
                world.getItemSystem().getUnequippedInventory().remove(item);
                battleInfo.append("\n ---------------- Character Respawn ----------------- \n\n");
                return true;
            }
        }

        for (Item item : world.getItemSystem().getEquippedItem()) {
            if (item.revive(character)) {
                if (item.getValidPosX() == 0) { character.setWeapon(null); }
                if (item.getValidPosX() == 3) { character.setShield(null); }
                character.attributeLossByItem(item);
                world.getItemSystem().getEquippedItem().remove(item);
                battleInfo.append("\n ---------------- Character Respawn ----------------- \n\n");
                return true;
            }
        }
        return false;
    }

    /**
     * process tower battle
     *
     * @return defeatedByTower //
     */
    public List<BasicEnemy> towerBattle() {
        List<BasicEnemy> defeatedByTower = new ArrayList<>();
        for (TowerBuilding tower : world.getBuildingSystem().getTowers()) {
            List<Pair<Integer, Integer>> pathsInRange = tower.attackPos(world.getOrderedPath());

            for (BasicEnemy enemy : enemies) {
                if (pathsInRange.contains(enemy.getPos())) {
                    tower.attackEnemy(enemy);
                    if (enemy.getStatus().getCurrentHealthValue() <= 0) {
                        defeatedByTower.add(enemy);
                    }
                }

            }
            processDefeatedEnemy(defeatedByTower);

        }
        System.out.println(battleInfo);
        world.setInfo(battleInfo.toString());
        battleInfo.setLength(0);
        return defeatedByTower;
    }

    /**
     * cause damage by trap
     **/
    public List<BasicEnemy> trapDamage() {
        List<BasicEnemy> defeatedByTrap = new ArrayList<>();
        List<TrapBuilding> usedTrap = new ArrayList<>();
        for (TrapBuilding trap : world.getBuildingSystem().getTraps()) {
            int posX = trap.getX();
            int posY = trap.getY();
            for (BasicEnemy enemy : enemies) {
                int eposX = enemy.getX();
                int eposY = enemy.getY();
                if (posX == eposX && posY == eposY) {
                    trap.damageEnemy(enemy);
                    trap.destroy();
                    usedTrap.add(trap);
                    if (Double.compare(enemy.getStatus().getCurrentHealthValue(), 0) == 0 || Double.compare(
                        enemy.getStatus().getCurrentHealthValue(), 0) == -1) {
                        defeatedByTrap.add(enemy);
                    }
                }
            }
            processDefeatedEnemy(defeatedByTrap);
        }
        for (TrapBuilding t : usedTrap) {
            if (world.getBuildingSystem().getTraps().contains(t)) {
                world.getBuildingSystem().getTraps().remove(t);
                world.getBuildingSystem().getBuildingEntities().remove(t);
            }
        }
        return defeatedByTrap;
    }

    /**
     * process defeated enemy
     *
     * @param defeatedEnemies //
     */
    private void processDefeatedEnemy(List<BasicEnemy> defeatedEnemies) {
        for (BasicEnemy e : defeatedEnemies) {
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from
            // the enemies list
            // if we killEnemy in prior loop, we get
            // java.util.ConcurrentModificatieonException
            // due to mutating list we're iterating over
            // battle(character, e);
            if (enemies.contains(e)) {
                if (Objects.equals(e.getType(), "Doggie")) {
                    character.addDoggieCoin();
                    world.addDoggieKilled();
                }

                if (Objects.equals(e.getType(), "ElanMuske")) { world.addElanMuskeKilled(); }
                killEnemy(e);
            }
        }

    }

    /**
     * Help function for math round
     *
     * @param value     //
     * @param precision //
     **/
    private static double mathRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    public LoopManiaWorld getWorld() {
        return world;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public StringBuilder getBattleInfo() {
        return this.battleInfo;
    }

    public void setBattleInfo(StringBuilder battleInfo) {
        this.battleInfo = battleInfo;
    }

    public List<BasicEnemy> getEnemies() {
        return this.enemies;
    }

    public List<AlliedSoldier> getSoldiers() {
        return this.soldiers;
    }

    public List<BasicEnemy> getBattleEnemies() {
        return battleEnemies;
    }

    public List<BasicEnemy> getTrancedEnemies() {
        return this.trancedEnemies;
    }

    public List<AlliedSoldier> getTempSoldiers() {
        return this.tempSoldiers;
    }

    public List<BasicEnemy> getInfectedSoldiers() {
        return this.infectedSoldier;
    }

}

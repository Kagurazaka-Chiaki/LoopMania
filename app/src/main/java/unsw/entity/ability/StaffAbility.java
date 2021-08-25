package unsw.entity.ability;

import java.util.List;

import unsw.GameRandom;
import unsw.loopmania.BattleSystem;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.behaviour.AttackBehaviour;
import unsw.entity.enemy.AlliedSoldier;
import unsw.entity.enemy.Doggie;
import unsw.entity.enemy.ElanMuske;

import org.javatuples.Pair;

public class StaffAbility implements ItemAbility {
    private BattleSystem battleSystem;

    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }

    private List<BasicEnemy> getTrancedEnemies() {
        return battleSystem.getTrancedEnemies();
    }

    private List<AlliedSoldier> getTempSoldiers() {
        return battleSystem.getTempSoldiers();
    }

    public boolean ability(Character character, BasicEnemy enemy) {
        this.battleSystem = character.getBattleSystem();
        List<BasicEnemy> battleEnemies = battleSystem.getBattleEnemies();

        double damage = character.getStatus().getStrength();

        if (character.getIfBuff()) {
            damage *= 2;
        }

        AttackBehaviour normalAttack = character.getStatus().getAttackBehaviour().get(0);
        normalAttack.attack(damage, enemy);
    
        if (enemy.getStatus().isAlive()) {
            int chance = GameRandom.random.nextInt(4);
            if (chance < 1 && !(ElanMuske.class.equals(enemy.getClass()) || Doggie.class.equals(enemy.getClass()))) {
                enemy.getStatus().setCurrentHealth(0);
                battleEnemies.remove(enemy);
                getTrancedEnemies().add(enemy);
                getBattleInfo().append("----------- Character triggers special attack --------\n");
                getBattleInfo().append("-----------").append(enemy).append("  is tranced -----------\n");
                AlliedSoldier tempSoldier =
                    new AlliedSoldier(Pair.with(0, getTempSoldiers().size() + 1), battleSystem.getWorld());
                getTempSoldiers().add(tempSoldier);
                getBattleInfo().append("----------- New temp soldier join the battle -------\n");
                return true;
            }
        }
        return false;
    }
}

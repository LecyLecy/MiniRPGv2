package miniRPG.battle;

import miniRPG.character.Player;
import miniRPG.monster.Monster;

public class BattleSystem {

    private final Player player;
    private final Monster monster;
    private final BattleLog battleLog;

    public BattleSystem(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.battleLog = new BattleLog();
        battleLog.add("A wild " + monster.getName() + " appears!");
    }

    public void playerAttack() {
        if (!player.isAlive() || !monster.isAlive()) return;

        int dmg = Math.max(1, player.getAttack() - monster.getDefense());
        monster.receiveDamage(dmg);

        battleLog.add(player.getName() + " attacks for " + dmg + " damage!");

        if (!monster.isAlive()) {
            battleLog.add(monster.getName() + " was defeated!");
        }
    }

    public void monsterAttack() {
        if (!player.isAlive() || !monster.isAlive()) return;

        int dmg = Math.max(1, monster.getAttack() - player.getDefense());
        player.receiveDamage(dmg);

        battleLog.add(monster.getName() + " attacks for " + dmg + " damage!");

        if (!player.isAlive()) {
            battleLog.add(player.getName() + " has been defeated...");
        }
    }

    public boolean isOver() {
        return !player.isAlive() || !monster.isAlive();
    }

    public boolean playerWon() {
        return player.isAlive() && !monster.isAlive();
    }

    public int getExpReward() {
        return monster.getExpReward();
    }

    public int getGoldReward() {
        return monster.getGoldReward();
    }

    public BattleLog getBattleLog() {
        return battleLog;
    }

    public Player getPlayer() {
        return player;
    }

    public Monster getMonster() {
        return monster;
    }
}

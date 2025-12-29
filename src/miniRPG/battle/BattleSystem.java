package miniRPG.battle;

import miniRPG.character.Character;
import miniRPG.monster.Monster;
import miniRPG.ui.screen.AppFrame;

public class BattleSystem {

    private final AppFrame frame;         // NEW
    private final Character player;
    private final Monster monster;
    private final BattleLog battleLog;

    public BattleSystem(AppFrame frame, Character player, Monster monster) {
        this.frame = frame;
        this.player = player;
        this.monster = monster;
        this.battleLog = new BattleLog();
    }

    public BattleResult startBattle() {

        battleLog.add("A wild " + monster.getName() + " appears!");

        while (player.isAlive() && monster.isAlive()) {

            player.takeTurn(monster);

            if (!monster.isAlive()) {
                battleLog.add(monster.getName() + " was defeated!");
                grantRewards();
                return BattleResult.PLAYER_WIN;
            }

            monster.takeTurn(player);

            if (!player.isAlive()) {
                battleLog.add(player.getName() + " has been defeated...");
                return BattleResult.PLAYER_LOSE;
            }

            player.endTurn();
            monster.endTurn();
        }

        return BattleResult.PLAYER_LOSE;
    }

    private void grantRewards() {
        int exp = monster.getExpReward();
        int gold = monster.getGoldReward();

        // UI/session state only (saved to CSV on exit)
        if (frame != null) {
            frame.addExp(exp);
            frame.addCoin(gold);
        }

        battleLog.add(player.getName() + " gained " + exp + " EXP!");
        battleLog.add(player.getName() + " gained " + gold + " gold!");
    }

    public BattleLog getBattleLog() {
        return battleLog;
    }
}

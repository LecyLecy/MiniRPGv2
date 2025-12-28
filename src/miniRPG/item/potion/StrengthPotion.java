package miniRPG.item.potion;

import miniRPG.character.Player;

public class StrengthPotion extends Potion {

    public StrengthPotion() {
        super("Strength Potion", 100);
    }

    @Override
    public void use(Player player) {
        int atkBoost = (int) (player.getAttack() * 0.10);
        int defBoost = (int) (player.getDefense() * 0.10);
        int hpBoost  = (int) (player.getMaxHP() * 0.10);

        player.increaseAttack(atkBoost);
        player.increaseDefense(defBoost);
        player.increaseMaxHP(hpBoost);
    }
}

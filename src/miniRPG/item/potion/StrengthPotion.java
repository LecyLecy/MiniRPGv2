package miniRPG.item.potion;

import miniRPG.character.Player;

public class StrengthPotion extends Potion {

    public StrengthPotion() {
        super("Strength Potion", 100);
    }

    @Override
    public void use(Player player) {
        int healAmount = Math.max(1, (int) (player.getMaxHP() * 0.10));
        player.heal(healAmount);
    }
}

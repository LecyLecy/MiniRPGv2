package miniRPG.item.potion;

import miniRPG.character.Player;

public class StrengthPotion extends Potion {

    public StrengthPotion() {
        super("Strength Potion", 100);
    }

    @Override
    public void use(Player player) {
        // TEMPORARY FIX (compatible with derived stats):
        // treat as a strong heal for now (10% of max HP)
        int healAmount = Math.max(1, (int) (player.getMaxHP() * 0.10));
        player.heal(healAmount);
    }
}

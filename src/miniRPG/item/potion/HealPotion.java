package miniRPG.item.potion;

import miniRPG.character.Player;
import miniRPG.item.Item;

public class HealPotion extends Item {

    private int healAmount;

    public HealPotion() {
        super("Heal Potion", 50);
        this.healAmount = 50;
    }

    @Override
    public void use(Player player) {
        player.heal(healAmount);
    }
}

package miniRPG.item.potion;

import miniRPG.character.Player;
import miniRPG.item.Item;

public abstract class Potion extends Item {

    public Potion(String name, int price) {
        super(name, price);
    }

    @Override
    public abstract void use(Player player);
}

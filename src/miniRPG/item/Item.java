package miniRPG.item;

import miniRPG.character.Player;

public abstract class Item {

    protected String name;
    protected int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public abstract void use(Player player);

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

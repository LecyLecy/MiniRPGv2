package miniRPG.shop;

import miniRPG.character.Player;
import miniRPG.data.Currency;
import miniRPG.item.Item;
import miniRPG.item.potion.HealPotion;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private final List<Item> stock;

    public Shop() {
        stock = new ArrayList<>();
        stock.add(new HealPotion());
    }

    public List<Item> getStock() {
        return stock;
    }

    public boolean buyItem(int index, Player player, Currency currency) {
        if (index < 0 || index >= stock.size()) return false;

        Item item = stock.get(index);

        if (currency.spend(item.getPrice())) {
            player.getInventory().addItem(item);
            return true;
        }

        return false;
    }
}

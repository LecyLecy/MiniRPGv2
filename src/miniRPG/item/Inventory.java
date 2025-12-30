package miniRPG.item;

import miniRPG.character.Player;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<miniRPG.item.Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }


    public void addItem(miniRPG.item.Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void removeItem(miniRPG.item.Item item) {
        items.remove(item);
    }

    public miniRPG.item.Item getItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public List<miniRPG.item.Item> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }


    public void useItem(int index, Player player) {
        miniRPG.item.Item item = getItem(index);
        if (item == null) return;

        item.use(player);
        removeItem(item);
    }

    public int size() {
        return items.size();
    }
}

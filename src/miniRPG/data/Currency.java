package miniRPG.data;

public class Currency {

    private int gold;

    public Currency(int startingGold) {
        this.gold = Math.max(0, startingGold);
    }

    public int getGold() {
        return gold;
    }

    public boolean spend(int amount) {
        if (amount <= 0) return false;
        if (amount > gold) return false;
        gold -= amount;
        return true;
    }

    public void earn(int amount) {
        if (amount <= 0) return;
        gold += amount;
    }
}

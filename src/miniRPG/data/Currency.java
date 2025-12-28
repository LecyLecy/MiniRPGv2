package miniRPG.data;

public class Currency {

    private int gold;

    public Currency(int startingGold) {
        this.gold = startingGold;
    }

    public int getGold() {
        return gold;
    }

    public boolean spend(int amount) {
        if (amount > gold || amount <= 0) return false;
        gold -= amount;
        return true;
    }

    public void earn(int amount) {
        if (amount > 0) {
            gold += amount;
        }
    }
}

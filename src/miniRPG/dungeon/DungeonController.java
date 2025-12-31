package miniRPG.dungeon;

import miniRPG.character.Player;
import miniRPG.monster.Monster;
import miniRPG.monster.MonsterFactory;

public class DungeonController {

    public interface Session {
        Player getPlayer();
        int getCurrentExp();
        void addExp(int delta);
        void addCoin(int delta);
    }

    public enum Result {
        OK,
        REQUEST_MONSTER_TURN,
        REQUEST_AFTER_MONSTER_TURN,
        REQUEST_NEXT_FLOOR,
        PLAYER_DEAD
    }

    private final Session session;

    private int floor = 1;
    private Monster monster;

    private boolean playerTurn = true;
    private boolean busy = false;

    private final BattleContext ctx = new BattleContext();

    private boolean skillUsedThisFloor = false;

    public DungeonController(Session session) {
        this.session = session;
    }

    public void startNewRun() {
        floor = 1;
        playerTurn = true;
        busy = false;
        setupFloor();
    }

    public int getFloor() {
        return floor;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean canAttack() {
        return !busy && playerTurn;
    }

    public boolean canUseSkill() {
        Player p = session.getPlayer();
        if (p == null) return false;
        if (busy) return false;
        if (skillUsedThisFloor) return false;
        return p.getPrimarySkill() != null;
    }

    public String getSkillButtonText() {
        Player p = session.getPlayer();
        if (p == null) return "Skill:";
        String n = p.getPrimarySkillName();
        if (n.isEmpty()) return "Skill:";
        if (skillUsedThisFloor) return n + " Skill Has Been Used";
        return "Skill: " + n;
    }

    private void setupFloor() {
        Player p = session.getPlayer();
        if (p != null) {
            p.setExp(session.getCurrentExp());
            p.restoreFull();
        }

        monster = MonsterFactory.createMonster(floor);
        monster.restoreFull();

        playerTurn = true;
        busy = false;

        skillUsedThisFloor = false;
        ctx.resetFloorEffects();
    }

    public Result useSkill() {
        if (!canUseSkill()) return Result.OK;

        Player p = session.getPlayer();
        if (p == null || monster == null) return Result.OK;

        p.usePrimarySkill(ctx, monster);
        skillUsedThisFloor = true;

        if (!monster.isAlive()) {
            grantRewardsAndPrepareNext();
            return Result.REQUEST_NEXT_FLOOR;
        }

        return Result.OK;
    }

    public Result attack() {
        if (!canAttack()) return Result.OK;

        Player p = session.getPlayer();
        if (p == null || monster == null) return Result.OK;

        busy = true;
        playerTurn = false;

        int dmg = Math.max(1, p.getAttack() - monster.getDefense());
        monster.receiveDamage(dmg);

        if (!monster.isAlive()) {
            grantRewardsAndPrepareNext();
            return Result.REQUEST_NEXT_FLOOR;
        }

        return Result.REQUEST_MONSTER_TURN;
    }

    public Result stepMonsterTurn() {
        Player p = session.getPlayer();
        if (p == null || monster == null) return Result.OK;

        int raw = Math.max(1, monster.getAttack() - p.getDefense());
        int mod1 = ctx.modifyMonsterAttackDamage(raw);
        int mod2 = ctx.modifyIncomingDamageToPlayer(mod1);

        p.receiveDamage(mod2);

        if (!p.isAlive()) {
            return Result.PLAYER_DEAD;
        }

        return Result.REQUEST_AFTER_MONSTER_TURN;
    }

    public Result stepAfterMonsterTurn() {
        busy = false;
        playerTurn = true;
        return Result.OK;
    }

    private void grantRewardsAndPrepareNext() {
        int goldGain = 10 * floor;

        int expGain = monster.getExpReward();
        if (floor == 5 || floor == 10 || floor == 15 || floor == 20 || floor == 25 || floor == 30) {
            expGain += 40;
        }

        session.addCoin(goldGain);
        session.addExp(expGain);

        Player p = session.getPlayer();
        if (p != null) p.setExp(session.getCurrentExp());
    }

    public void stepNextFloor() {
        floor++;
        setupFloor();
    }
}

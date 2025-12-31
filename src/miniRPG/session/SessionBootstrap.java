package miniRPG.session;

import miniRPG.character.Player;
import miniRPG.ui.screen.AppFrame;

public final class SessionBootstrap {

    private SessionBootstrap() {}

    public static void apply(AppFrame frame, String username, String role, int coin, int exp) {
        if (frame == null) return;

        frame.setCurrentUsername(username);
        frame.setCurrentCoin(coin);
        frame.setCurrentExp(exp);

        Player p = frame.buildPlayerFromRole(username, role);
        frame.setCurrentPlayer(p);

        frame.showScreen("map");
    }
}

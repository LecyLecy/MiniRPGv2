package miniRPG.session;

import miniRPG.character.Player;
import miniRPG.ui.screen.AppFrame;

public final class SessionService {

    private SessionService() {}

    public static void apply(AppFrame frame, GameSession session) {
        if (frame == null || session == null) return;

        frame.setCurrentUsername(session.getUsername());
        frame.setCurrentCoin(session.getCoin());
        frame.setCurrentExp(session.getExp());

        Player p = frame.buildPlayerFromRole(session.getUsername(), session.getRole());
        frame.setCurrentPlayer(p);

        frame.showScreen("map");
    }

    public static void apply(AppFrame frame, String username, String role, int coin, int exp) {
        apply(frame, new GameSession(username, role, coin, exp));
    }
}

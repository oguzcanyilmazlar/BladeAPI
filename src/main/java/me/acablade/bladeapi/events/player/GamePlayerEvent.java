package me.acablade.bladeapi.events.player;

import me.acablade.bladeapi.IGame;
import me.acablade.bladeapi.events.GameEvent;
import org.bukkit.entity.Player;

public abstract class GamePlayerEvent extends GameEvent {

    protected final Player player;

    public GamePlayerEvent(Player player, IGame abstractGame) {
        super(abstractGame);
        this.player = player;
    }

    public final Player getPlayer() {
        return player;
    }
}

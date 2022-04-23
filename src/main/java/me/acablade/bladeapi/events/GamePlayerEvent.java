package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import org.bukkit.entity.Player;

/**
 * @author Acablade/oz
 */
public abstract class GamePlayerEvent extends GameEvent{

    protected final Player player;

    public GamePlayerEvent(Player player,AbstractGame abstractGame) {
        super(abstractGame);
        this.player = player;
    }

    public final Player getPlayer() {
        return player;
    }
}

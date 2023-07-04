package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 * Gets fired when the game is finished.
 */
public class GameFinishEvent extends GameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GameFinishEvent(AbstractGame game) {
        super(game);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}

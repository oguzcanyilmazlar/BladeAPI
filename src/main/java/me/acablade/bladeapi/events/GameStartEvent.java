package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 */
public class GameStartEvent extends GameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GameStartEvent(AbstractGame game) {
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
package me.acablade.bladeapi.events;

import me.acablade.bladeapi.IGame;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends GameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GameStartEvent(IGame game) {
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
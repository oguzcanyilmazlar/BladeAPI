package me.acablade.bladeapi.events;

import me.acablade.bladeapi.IGame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerJoinGameEvent extends GamePlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerJoinGameEvent(Player who, IGame game) {
        super(who,game);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}

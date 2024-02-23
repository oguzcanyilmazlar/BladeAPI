package me.acablade.bladeapi.events;

import me.acablade.bladeapi.IGame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLeaveGameEvent extends GamePlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerLeaveGameEvent(Player who, IGame game) {
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
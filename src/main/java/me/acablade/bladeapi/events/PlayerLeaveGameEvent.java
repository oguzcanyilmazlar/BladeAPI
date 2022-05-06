package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 * Gets fired when the player leaves the minigame.
 */
public class PlayerLeaveGameEvent extends GamePlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerLeaveGameEvent(Player who, AbstractGame game) {
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
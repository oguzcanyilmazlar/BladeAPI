package me.acablade.bladeapi.events;

import lombok.Getter;
import me.acablade.bladeapi.AbstractGame;
import me.acablade.bladeapi.objects.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 * Gets fired when a player gets assigned to a team
 */
public class TeamChooseEvent extends GamePlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();


    private boolean cancelled = false;

    @Getter
    private final Team team;

    /**
     *
     * @param who Player
     * @param game Player's game
     * @param team Team that the player gets assigned to
     */
    public TeamChooseEvent(Player who, AbstractGame game, Team team) {
        super(who,game);
        this.team = team;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}

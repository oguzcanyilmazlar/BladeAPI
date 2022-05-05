package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import me.acablade.bladeapi.AbstractPhase;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 */
public class GamePhaseChangeEvent extends GameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractPhase prevPhase;
    private final AbstractPhase nextPhase;

    private boolean cancelled;

    public GamePhaseChangeEvent(AbstractGame game, AbstractPhase prevPhase, AbstractPhase nextPhase) {
        super(game);
        this.prevPhase = prevPhase;
        this.nextPhase = nextPhase;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AbstractPhase getNextPhase() {
        return nextPhase;
    }

    public AbstractPhase getPrevPhase() {
        return prevPhase;
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

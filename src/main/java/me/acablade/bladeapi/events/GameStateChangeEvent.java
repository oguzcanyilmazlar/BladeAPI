package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import me.acablade.bladeapi.AbstractPhase;
import org.bukkit.event.HandlerList;

/**
 * @author Acablade/oz
 */
public class GameStateChangeEvent extends GameEvent{

    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractPhase prevPhase;
    private final AbstractPhase nextPhase;

    public GameStateChangeEvent(AbstractGame game, AbstractPhase prevPhase, AbstractPhase nextPhase) {
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
}

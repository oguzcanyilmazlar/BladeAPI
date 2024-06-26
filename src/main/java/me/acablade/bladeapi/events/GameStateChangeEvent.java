package me.acablade.bladeapi.events;

import lombok.Getter;
import me.acablade.bladeapi.IGame;
import me.acablade.bladeapi.IState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends GameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final IState prevPhase;
    @Getter
    private final IState nextPhase;

    private boolean cancelled;

    public GameStateChangeEvent(IGame game, IState prevPhase, IState nextPhase) {
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}

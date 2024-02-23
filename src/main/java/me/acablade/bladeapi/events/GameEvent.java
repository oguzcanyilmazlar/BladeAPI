package me.acablade.bladeapi.events;

import me.acablade.bladeapi.IGame;
import org.bukkit.event.Event;

public abstract class GameEvent extends Event {

    protected final IGame game;

    public GameEvent(IGame abstractGame){
        this.game = abstractGame;
    }

    public final IGame getGame() {
        return game;
    }
}
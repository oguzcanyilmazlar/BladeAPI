package me.acablade.bladeapi.events;

import me.acablade.bladeapi.AbstractGame;
import org.bukkit.event.Event;

/**
 * @author Acablade/oz
 */
public abstract class GameEvent extends Event {

    protected final AbstractGame game;

    public GameEvent(AbstractGame abstractGame){
        this.game = abstractGame;
    }

    public final AbstractGame getGame() {
        return game;
    }
}
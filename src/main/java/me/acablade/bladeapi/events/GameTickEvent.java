package me.acablade.bladeapi.events;

import org.bukkit.event.HandlerList;

import me.acablade.bladeapi.IGame;

public class GameTickEvent extends GameEvent{
	
	private static final HandlerList HANDLERS = new HandlerList();

	public GameTickEvent(IGame abstractGame) {
		super(abstractGame);
		// TODO Auto-generated constructor stub
	}

	@Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	
	
}

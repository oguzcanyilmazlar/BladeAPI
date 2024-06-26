package me.acablade.bladeapi;

import org.bukkit.event.Listener;

import me.acablade.bladeapi.objects.IGameData;

public interface IGame extends Listener {

    public void onEnable();
    public void onDisable();
    public void onTick();

    public void enable(long delay, long period);
    public void disable();


    public IGameData getGameData();


    public void addPhaseNext(IState phase);
    public void removeNextPhase();
    public void addPhase(IState phase);
    public void removeLastPhase();



}

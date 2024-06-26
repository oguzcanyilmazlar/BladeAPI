package me.acablade.bladeapi;

import me.acablade.bladeapi.objects.IGameData;

public interface  IGame {

    public void onEnable();
    public void onDisable();
    public void onTick();

    public void enable(long delay, long period);
    public void disable();


    IGameData getGameData();


    public void addPhaseNext(IPhase phase);
    public void removeNextPhase();
    public void addPhase(IPhase phase);
    public void removeLastPhase();



}

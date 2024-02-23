package me.acablade.bladeapi;

public interface  IGame {

    public void onEnable();
    public void onDisable();
    public void onTick();

    public void enable(long delay, long period);
    public void disable();


    public void addPhaseNext(IPhase phase);
    public void removeNextPhase();
    public void addPhase(IPhase phase);
    public void removeLastPhase();



}

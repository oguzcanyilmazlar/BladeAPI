package me.acablade.bladeapi;


public interface IGame {

    void onEnable();
    void onDisable();
    void onTick();

    IState getCurrentState();

    void enable(long delay, long period);
    void disable();

    void addState(IState state);

}

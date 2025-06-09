package me.acablade.bladeapi;

import java.time.Duration;

import org.bukkit.event.Listener;


public interface IState extends Listener {

    void onEnable();
    void onDisable();
    void onTick();


    void enable();
    void disable();
    void tick();


    Duration timeLeft();
    Duration duration();

    default void onEnd(Runnable runnable) {}


}

package me.acablade.bladeapi;

import java.time.Duration;

import org.bukkit.event.Listener;


public interface IState extends Listener {

    public void onEnable();
    public void onDisable();
    public void onTick();


    public void enable();
    public void disable();
    public void tick();


    public Duration timeLeft();
    public Duration duration();

}

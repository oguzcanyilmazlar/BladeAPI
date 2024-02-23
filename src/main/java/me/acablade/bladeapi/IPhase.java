package me.acablade.bladeapi;

import me.acablade.bladeapi.features.IFeature;

import java.time.Duration;

public interface IPhase {

    public void onEnable();

    public void onDisable();

    public void onTick();


    public void enable();

    public void disable();

    public void tick();



    public Duration timeLeft();

    public Duration duration();


    public <R extends IFeature> R getFeature(Class<R> clazz);

    public void addFeature(IFeature feature);

}

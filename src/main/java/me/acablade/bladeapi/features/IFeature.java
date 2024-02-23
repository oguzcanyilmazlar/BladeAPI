package me.acablade.bladeapi.features;

public interface IFeature {

    public void onEnable();
    public void onDisable();
    public void onTick();


    public void enable();

    public void disable();

}

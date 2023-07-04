package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.features.AbstractFeature;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractPhase<T extends AbstractGame> {

    @Getter
    private final T game;

    @Getter
    private Instant startInstant = Instant.now();

    /**
     * All the features
     */
    private final Map<Class<? extends AbstractFeature<T>>,AbstractFeature<T>> featureMap = new LinkedHashMap<>();


    /**
     * Resets the timer.
     */
    public void resetTimer(){
        this.startInstant = Instant.now();
    }

    /**
     * Phase duration
     * @return Phase duration
     */
    public abstract Duration duration();

    /**
     * Gets called when the phase gets enabled
     */
    public void onEnable(){}

    /**
     * Gets called when the phase gets disabled
     */
    public void onDisable(){}

    /**
     * Gets called every tick
     */
    public void onTick(){}

    /**
     * Adds feature to the phase
     * @param feature Specified feature
     */
    public void addFeature(AbstractFeature<T> feature){
        featureMap.put((Class<? extends AbstractFeature<T>>) feature.getClass(), feature);
    }

    /**
     * Enables the phase
     */
    public final void enable(){
        this.startInstant = Instant.now(); // not using resetTimer() in case of override
        onEnable();
        featureMap.values().forEach(AbstractFeature::enable);
    }

    /**
     * Disables the phase
     */
    public final void disable(){
        onDisable();
        featureMap.values().forEach(AbstractFeature::disable);
    }

    /**
     * Ticks the phase
     */
    public final void tick(){
        onTick();
        featureMap.values().forEach(AbstractFeature::onTick);
    }

    /**
     *
     * @return Current time left on the phase
     */
    public Duration timeLeft(){
        Duration sinceStart = Duration.between(startInstant, Instant.now());
        Duration remaining = duration().minus(sinceStart);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    /**
     * Gets the specified feature (can return null)
     * @param clazz Specified feature's class
     * @param <R> Specified feature
     * @return Specified feature
     */
    public <R extends AbstractFeature<T>> R getFeature(Class<R> clazz){
        return (R) featureMap.get(clazz);
    }


}

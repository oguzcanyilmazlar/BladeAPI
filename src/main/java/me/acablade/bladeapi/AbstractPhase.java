package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.features.AbstractFeature;
import me.acablade.bladeapi.features.IFeature;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractPhase implements IPhase {

    @Getter
    private final IGame game;

    @Getter
    private Instant startInstant = Instant.now();

    private final Map<Class<? extends IFeature>,IFeature> featureMap = new HashMap<>();


    public final void resetTimer(){
        this.startInstant = Instant.now();
    }

    public void addFeature(IFeature feature){
        featureMap.put(feature.getClass(), feature);
    }

    public final void enable(){
        resetTimer();
        onEnable();
        featureMap.values().forEach(IFeature::enable);
    }

    public final void disable(){
        onDisable();
        featureMap.values().forEach(IFeature::disable);
    }

    public final void tick(){
        onTick();
        featureMap.values().forEach(IFeature::onTick);
    }

    public Duration timeLeft(){
        Duration sinceStart = Duration.between(startInstant, Instant.now());
        Duration remaining = duration().minus(sinceStart);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public <R extends IFeature> R getFeature(Class<R> clazz){
        return (R) featureMap.get(clazz);
    }


}

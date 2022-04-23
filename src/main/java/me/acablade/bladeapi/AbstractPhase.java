package me.acablade.bladeapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.acablade.bladeapi.features.AbstractFeature;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
@Getter
public abstract class AbstractPhase {

    private final AbstractGame game;
    private final Instant startInstant = Instant.now();

    private final Map<Class<? extends AbstractFeature>,AbstractFeature> featureMap = new HashMap<>();

    public abstract Duration duration();

    public void onEnable(){
        featureMap.values().forEach(AbstractFeature::onEnable);
    }

    public void onDisable(){
        featureMap.values().forEach(AbstractFeature::onDisable);
    }

    public void onTick(){
        // empty for now
    }

    public void addFeature(AbstractFeature feature){
        featureMap.put(feature.getClass(), feature);
    }

    public final void enable(){
        onEnable();
    }

    public final void disable(){
        onDisable();
    }

    public final void tick(){
        onTick();
        featureMap.values().forEach(AbstractFeature::onTick);
    }

    public Duration timeLeft(){
        Duration sinceStart = Duration.between(startInstant, Instant.now());
        Duration remaining = duration().minus(sinceStart);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public <T extends AbstractFeature> T getFeature(Class<T> clazz){
        return (T) featureMap.get(clazz);
    }


}

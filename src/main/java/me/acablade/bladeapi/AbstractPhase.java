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
public abstract class AbstractPhase {

    @Getter
    private final AbstractGame game;
    private final Instant startInstant = Instant.now();

    private final Map<Class<? extends AbstractFeature>,AbstractFeature> featureMap = new HashMap<>();

    public abstract Duration duration();

    public void onEnable(){}

    public void onDisable(){}

    public void onTick(){}

    public void addFeature(AbstractFeature feature){
        featureMap.put(feature.getClass(), feature);
    }

    public final void enable(){
        onEnable();
        featureMap.values().forEach(AbstractFeature::enable);
    }

    public final void disable(){
        onDisable();
        featureMap.values().forEach(AbstractFeature::disable);
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

package me.acablade.bladeapi;

import java.time.Duration;
import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Acablade/oz
 */

@RequiredArgsConstructor
public abstract class AbstractState implements IState {

    @Getter
    private final IGame game;

    @Getter
    private Instant startInstant = Instant.now();


    public void resetTimer(){
        this.startInstant = Instant.now();
    }


    public void enable(){
        resetTimer();
        onEnable();
    }

    public void disable(){
        onDisable();
    }

    public void tick(){
        onTick();
    }

    public Duration timeLeft(){
        Duration sinceStart = Duration.between(startInstant, Instant.now());
        Duration remaining = duration().minus(sinceStart);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

}

package me.acablade.bladeapi;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class AbstractState implements IState {

    private final List<Runnable> endCallbacks = new ArrayList<>();
    private final IGame game;

    @Getter
    private Instant startInstant = Instant.now();

    public AbstractState(IGame game) {
        this.game = game;
    }

    public IGame getGame() {
        return game;
    }

    public void resetTimer(){
        this.startInstant = Instant.now();
    }


    public void enable(){
        resetTimer();
        onEnable();
    }


    @Override
    public void onEnd(Runnable runnable) {
        endCallbacks.add(runnable);
    }


    public void disable(){
        for (Runnable r : endCallbacks) r.run();
        endCallbacks.clear();
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

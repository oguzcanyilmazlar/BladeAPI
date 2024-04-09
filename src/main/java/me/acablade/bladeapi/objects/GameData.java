package me.acablade.bladeapi.objects;

import java.util.Set;

public class GameData implements IGameData{



    @Override
    public <T extends IActor> Set<T> getActors() {
        return null;
    }

    @Override
    public void addActor(IActor actor) {

    }

    @Override
    public void removeActor(IActor actor) {

    }

    @Override
    public boolean contains(IActor actor) {
        return false;
    }
}

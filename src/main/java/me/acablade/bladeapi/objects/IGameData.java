package me.acablade.bladeapi.objects;

import java.util.Set;

public interface IGameData {

    <T extends IActor> Set<T> getActors();

    void addActor(IActor actor);
    void removeActor(IActor actor);
    boolean contains(IActor actor);


}

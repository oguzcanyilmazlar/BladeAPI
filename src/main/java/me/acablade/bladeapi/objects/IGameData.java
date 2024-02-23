package me.acablade.bladeapi.objects;

import java.util.Set;

public interface IGameData {

    public <T extends IActor> Set<T> getPlayers();
    public <T extends IActor> Set<T> getSpectators();

}

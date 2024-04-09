package me.acablade.test;

import me.acablade.test.game.TestGame;
import org.bukkit.plugin.java.JavaPlugin;

public class MiniGamePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        TestGame testGame = new TestGame(this);


    }
}

package me.acablade.bladeapi.objects;

import lombok.Getter;
import me.acablade.bladeapi.IGame;
import me.acablade.bladeapi.IState;
import me.acablade.bladeapi.data.IGameData;
import me.acablade.bladeapi.data.IPlayerData;
import me.acablade.bladeapi.data.internal.PlayerDataEventAdapter;
import me.acablade.bladeapi.eventrouter.GameEventRouter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class GameContext {

    private static final Logger LOGGER = Logger.getLogger("BladeAPI-GameContext");


    private final IGame game;
    private final Map<Class<? extends IGameData>, IGameData> gameDataMap = new HashMap<>();
    private final GameEventRouter eventRouter;
    private final JavaPlugin plugin;


    public GameContext(JavaPlugin plugin, IGame game) {
        this.plugin = plugin;
        this.game = game;
        this.eventRouter = new GameEventRouter(plugin, this);
    }

    public IGame getGame() {
        return game;
    }

    public GameEventRouter getEventRouter() {
        if (game.getCurrentState() != null) {
            LOGGER.warning("You should use withScopedRouter during active states.");
        }
        return eventRouter;
    }

    public void withScopedRouter(Consumer<GameEventRouter> consumer) {
        GameEventRouter scopedRouter = new GameEventRouter(plugin, this);
        consumer.accept(scopedRouter);

        IState currentState = game.getCurrentState();
        if (currentState == null) {
            throw new IllegalStateException("No active game state to bind scoped router.");
        }

        currentState.onEnd(scopedRouter::clearAll);
    }


    public <T extends IGameData> void registerData(Class<T> key, T data) {
        if (data instanceof IPlayerData && !(data instanceof PlayerDataEventAdapter)) {
            IPlayerData wrapped = new PlayerDataEventAdapter(game, (IPlayerData) data);
            LOGGER.fine("Wrapping IPlayerData with PlayerDataEventAdapter for game: " + game);
            gameDataMap.put(IPlayerData.class, wrapped);
        } else {
            gameDataMap.put(key, data);
        }
    }

    public <T extends IGameData> Optional<T> getData(Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(gameDataMap.get(clazz)));
    }

    public IPlayerData getPlayerData() {
        return getData(IPlayerData.class)
                .orElseThrow(() -> new IllegalStateException("IPlayerData not registered in GameContext!"));
    }

}

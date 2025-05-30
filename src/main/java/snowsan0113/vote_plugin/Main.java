package snowsan0113.vote_plugin;

import org.bukkit.plugin.java.JavaPlugin;
import snowsan0113.vote_plugin.commands.VoteCommand;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        //コマンド
        getCommand("start_vote").setExecutor(new VoteCommand());
        getCommand("stop_vote").setExecutor(new VoteCommand());
        getCommand("status_vote").setExecutor(new VoteCommand());

        getLogger().info("[VotePlugin] プラグインが有効になりました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

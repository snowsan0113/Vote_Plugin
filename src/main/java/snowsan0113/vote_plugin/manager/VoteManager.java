package snowsan0113.vote_plugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import snowsan0113.vote_plugin.Main;

import java.util.*;

public class VoteManager {

    //投票リスト
    private static final List<Vote> vote_list = new ArrayList<>();

    public static void startVote(String name, String display_name, int time, List<String> detail_list) {
        Vote vote = new Vote(name, display_name, time, detail_list);
        vote_list.add(vote);
    }

    public static void stopVote(String name) {
        vote_list.stream().filter(vote -> vote.getName().equalsIgnoreCase(name)).findFirst().orElse(null).stopVote();
    }

    public static List<Vote> getVoteList() {
        return vote_list;
    }

}

package snowsan0113.vote_plugin.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import snowsan0113.vote_plugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vote {

    //投票の中身
    private final String name;
    private final String display_name;
    private final Map<String, List<OfflinePlayer>> vote_player_map;
    private final BukkitTask task;

    public Vote(String name, String display_name, int time, List<String> detail_list) {
        this.name = name;
        this.vote_player_map = new HashMap<>();
        this.display_name = display_name;
        for (String detail : detail_list) {
            this.vote_player_map.put(detail, new ArrayList<>());
        }
        this.task = new VoteTask(this, time).runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }

    public int addVote(CommandSender sender, String detail) {
        if (!isVoted((OfflinePlayer) sender)) {
            if (vote_player_map.get(detail) != null) {
                vote_player_map.get(detail).add((OfflinePlayer) sender);
                return 0;
            }
            else {
                return 1;
            }
        }
        else {
            return 2;
        }
    }

    public void removeVote(CommandSender sender) {
        String voted_name = getVotedDetail((OfflinePlayer) sender);
        if (isVoted((OfflinePlayer) sender) && voted_name != null) {
            List<OfflinePlayer> vote_player_list = vote_player_map.get(voted_name);
            if (vote_player_list != null) {
                vote_player_list.remove(sender);
            }
        }
    }

    public String getVotedDetail(OfflinePlayer player) {
        if (isVoted(player)) {
            for (Map.Entry<String, List<OfflinePlayer>> entry : vote_player_map.entrySet()) {
                String detail = entry.getKey();
                List<OfflinePlayer> vote_player_list = entry.getValue();

                if (vote_player_list.contains(player)) {
                    return detail;
                }
            }
        }
        return null;
    }

    public boolean isVoted(OfflinePlayer player) {
        for (Map.Entry<String, List<OfflinePlayer>> entry : vote_player_map.entrySet()) {
            List<OfflinePlayer> map_vote_player = entry.getValue();

            if (map_vote_player.contains((OfflinePlayer) player)) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return display_name;
    }

    public Map<String, List<OfflinePlayer>> getVoteMap() {
        return new HashMap<>(vote_player_map);
    }

    public void stopVote() {
        task.cancel();
        VoteManager.getVoteList().remove(this);
    }


}

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
    private static final List<VoteManager> vote_list = new ArrayList<>();

    //投票の中身
    private final String name;
    private final String display_name;
    private int time;
    private final Map<String, List<OfflinePlayer>> vote_player_map;
    private final BukkitTask task;

    public VoteManager(String name, String display_name, int time, List<String> detail_list) {
        this.name = name;
        this.vote_player_map = new HashMap<>();
        this.display_name = display_name;
        for (String detail : detail_list) {
            this.vote_player_map.put(detail, new ArrayList<>());
        }
        this.time = time;

        final int[] time_copy = {time};
        Map<String, Integer> vote_size_map = new HashMap<>();
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (time_copy[0] <= 0) {
                    int max_vote_size = 0;
                    List<String> max_vote_detail_list = new ArrayList<>();

                    for (Map.Entry<String, List<OfflinePlayer>> entry : vote_player_map.entrySet()) {
                        String detail = entry.getKey();
                        List<OfflinePlayer> vote_player_list = entry.getValue();
                        vote_size_map.put(detail, vote_player_list.size());

                        max_vote_size = Collections.max(new ArrayList<>(vote_size_map.values()));
                        if (vote_player_list.size() == max_vote_size) {
                            max_vote_detail_list.add(detail);
                        }
                    }

                    if (max_vote_detail_list.size() == 1) {
                        String first_detail = max_vote_detail_list.get(0);
                        Bukkit.broadcastMessage("投票の中で一番多かったのは" + first_detail + "でした。" + ChatColor.GRAY + "（" + max_vote_size + "票）");
                    }
                    else {
                        StringBuilder builder = new StringBuilder();
                        for (String detail : max_vote_detail_list) {
                            builder.append("「" + detail + "」");
                        }

                        Bukkit.broadcastMessage("投票の中で多かったのは" + builder + "でした。" + ChatColor.GRAY + "（" + max_vote_size + "票）");
                    }

                    this.cancel();
                }
                else {
                    time_copy[0]--;
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);

        vote_list.add(this);
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
        vote_list.remove(this);
    }

    public static List<VoteManager> getVoteList() {
        return vote_list;
    }

    public BukkitTask getTask() {
        return task;
    }
}

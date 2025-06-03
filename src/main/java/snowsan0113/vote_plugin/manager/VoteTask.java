package snowsan0113.vote_plugin.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VoteTask extends BukkitRunnable {

    private Vote vote;
    private int time;

    public VoteTask(Vote vote, int time) {
        this.time = time;
    }

    @Override
    public void run() {
        if (time <= 0) {
            Map<String, Integer> vote_size_map = new HashMap<>();
            int max_vote_size = 0;
            List<String> max_vote_detail_list = new ArrayList<>();
            Map<String, List<OfflinePlayer>> vote_player_map = vote.getVoteMap();

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
            time--;
        }
    }

    public Vote getVote() {
        return vote;
    }

}

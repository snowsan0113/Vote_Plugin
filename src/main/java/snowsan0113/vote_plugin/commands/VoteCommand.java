package snowsan0113.vote_plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import snowsan0113.vote_plugin.manager.Vote;
import snowsan0113.vote_plugin.manager.VoteManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("start_vote")) {
            int arg_size = args.length;

            List<String> vote_list = new ArrayList<>(Arrays.asList(args).subList(3, arg_size));
            VoteManager.startVote(args[0], args[1], Integer.parseInt(args[2]), vote_list);

            Bukkit.broadcastMessage("投票を開始しました。" + vote_list);
        }
        else if (cmd.getName().equalsIgnoreCase("stop_vote")) {

        }
        else if (cmd.getName().equalsIgnoreCase("status_vote")) {
            List<Vote> vote_list = VoteManager.getVoteList();
            if (args.length == 0) {
                StringBuilder builder = new StringBuilder();
                for (Vote voteManager : vote_list) {
                    builder.append("「" + voteManager.getDisplayName() + "」");
                }
                send.sendMessage("現在行われてる投票: " + builder);
            }
            else {
                Vote vote = VoteManager.getVoteList().stream()
                        .filter(manager -> manager.getName().equalsIgnoreCase(args[0]))
                        .findFirst()
                        .orElse(null);

                if (vote != null) {
                    StringBuilder choices_builder = new StringBuilder();
                    for (String choices : vote.getVoteMap().keySet()) {
                        choices_builder.append("「" + choices + "」");
                    }
                    StringBuilder vote_builder = new StringBuilder();
                    for (Map.Entry<String, List<OfflinePlayer>> entry : vote.getVoteMap().entrySet()) {
                        String choices = entry.getKey();
                        List<OfflinePlayer> vote_player_list = entry.getValue();
                        vote_builder.append("・" + choices + "（" + vote_player_list.size() + "名）" + "\n");
                        for (OfflinePlayer player : vote_player_list) {
                            vote_builder.append(player + ",");
                        }
                    }

                    send.sendMessage("==" + vote.getDisplayName() + "（" + vote.getName() + ")==" + "\n" +
                            "選択肢：" + choices_builder + "\n" +
                            "投票した人：" +
                            vote_builder);
                }
                else {
                    send.sendMessage("その投票は見つかりませんでした。");
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("vote")) {
            Vote vote = VoteManager.getVoteList().stream()
                    .filter(manager -> manager.getName().equalsIgnoreCase(args[0]))
                    .findFirst()
                    .orElse(null);
            if (vote != null) {
                int add_vote = vote.addVote(send, args[1]);
                if (add_vote == 0) {
                    vote.addVote(send, args[1]);
                    send.sendMessage("投票することができました。");
                }
                else if (add_vote == 1) {
                    send.sendMessage("その選択肢は見つかりませんでした。");
                }
                else if (add_vote == 2) {
                    vote.removeVote(send);
                    vote.addVote(send, args[1]);
                    send.sendMessage("投票することができました。");
                }
            }
            else {
                send.sendMessage("その投票は見つかりませんでした。");
            }

        }
        return false;
    }

}

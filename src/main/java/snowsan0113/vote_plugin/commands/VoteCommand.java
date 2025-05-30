package snowsan0113.vote_plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import snowsan0113.vote_plugin.manager.VoteManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("start_vote")) {
            int arg_size = args.length;

            List<String> vote_list = new ArrayList<>(Arrays.asList(args).subList(2, arg_size));
            new VoteManager(args[0], Integer.parseInt(args[1]), vote_list);

            Bukkit.broadcastMessage("投票を開始しました。" + vote_list);
        }
        else if (cmd.getName().equalsIgnoreCase("stop_vote")) {

        }
        else if (cmd.getName().equalsIgnoreCase("status_vote")) {

        }
        return false;
    }

}

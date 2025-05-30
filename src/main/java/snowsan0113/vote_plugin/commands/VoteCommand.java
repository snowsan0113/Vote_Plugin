package snowsan0113.vote_plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("start_vote")) {

        }
        else if (cmd.getName().equalsIgnoreCase("stop_vote")) {

        }
        else if (cmd.getName().equalsIgnoreCase("status_vote")) {

        }
        return false;
    }

}

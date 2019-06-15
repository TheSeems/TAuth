package me.theseems.tauth.commands;

import me.theseems.tauth.Checker;
import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class LogoutCommand extends Command {
    public LogoutCommand() {
        super("logout");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = Main.getServer().getPlayer(commandSender.getName());
        UUID uuid = player.getUniqueId();
        LoginResponse response = TAuth.getManager().logout(uuid);

        if (response == LoginResponse.OK) {
            ServerInfo auth = Main.getServer().getServerInfo(TAuth.getAuthBalancer().getServer(uuid));
            if (!auth.getName().equals(player.getServer().getInfo().getName()))
                player.connect(auth);
            Checker.display(uuid, TAuth.getManager().isAutheticated(uuid));
        }
    }
}

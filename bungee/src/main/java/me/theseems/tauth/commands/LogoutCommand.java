package me.theseems.tauth.commands;

import me.theseems.tauth.LoginResponse;
import me.theseems.tauth.Main;
import me.theseems.tauth.TAuth;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class LogoutCommand extends Command {
    public LogoutCommand() {
        super("logout");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        UUID player = Main.getServer().getPlayer(commandSender.getName()).getUniqueId();
        LoginResponse response = TAuth.getAuthManager().logout(player);
        if (response == LoginResponse.OK) {
            Main.getServer().getPlayer(player).disconnect(new TextComponent("Logged out"));
        }
    }
}

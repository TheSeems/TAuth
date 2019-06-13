package me.theseems.tauth.commands;

import me.theseems.tauth.*;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class LoginCommand extends Command {
    public LoginCommand() {
        super("login");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        UUID player = Main.getServer().getPlayer(commandSender.getName()).getUniqueId();
        LoginResponse response = TAuth.getAuthManager().isAutheticated(player);
        if (response == LoginResponse.OK || response == LoginResponse.UNREGISTERED) {
            Checker.display(player, response);
            return;
        }

        if (strings.length < 1) {
            commandSender.sendMessage(new TextComponent("Enter password.. /login <pass>"));
        } else {
            String hash = strings[0];
            hash = TAuth.getHasher().hash(hash);
            response = TAuth.getAuthManager().login(player, hash);
            Checker.display(player, response);

            if (response == LoginResponse.OK) {
                Main.getServer()
                        .getPluginManager()
                        .callEvent(new TLoginEvent(
                                Main.getServer().getPlayer(player)
                        ));
            }
        }
    }
}

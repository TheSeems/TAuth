package me.theseems.tauth.commands;

import me.theseems.tauth.*;
import me.theseems.tauth.events.TLoginEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        UUID player = Main.getServer().getPlayer(commandSender.getName()).getUniqueId();
        LoginResponse response = TAuth.getManager().isAutheticated(player);
        if (response != LoginResponse.UNREGISTERED) {
            Checker.display(player, response);
            return;
        }

        if (strings.length < 2) {
            commandSender.sendMessage(new TextComponent(
                    Main.getBungeeSettings().getMessage("register.usage")
            ));
        } else if (!strings[0].equals(strings[1])) {
            commandSender.sendMessage(new TextComponent(
                    Main.getBungeeSettings().getMessage("register.no_match")
            ));
        } else {
            String hash = strings[0];
            hash = TAuth.getHasher().hash(hash);
            RegisterResponse registerResponse = TAuth.getManager().register(player, hash);

            if (registerResponse == RegisterResponse.OK) {
                Main.getServer()
                        .getPluginManager()
                        .callEvent(new TLoginEvent(
                                Main.getServer().getPlayer(player), false
                        ));
                Checker.display(player, registerResponse);
            }

        }
    }
}

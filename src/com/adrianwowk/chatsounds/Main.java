package com.adrianwowk.chatsounds;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getConsoleSender().sendMessage("§aChat Sounds Enabled Successfully");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§aChat Sounds Disabled Successfully");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("chatsounds"))
            return false;
        if (!sender.hasPermission("chatsounds.reload")){
            sender.sendMessage(getPrefix() + "§cYou do not have permission to use this command");
            return true;
        }

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")){
            sender.sendMessage(getPrefix() + "§eUsage: /chatsounds reload");
            return true;
        }

        reloadConfig();
        sender.sendMessage(getPrefix() + "§aReloaded Config");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("chatsounds") && sender.hasPermission("chatsounds.reload")){
            if (args.length <= 1)
                list.add("reload");
        }

        return list;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String msg = event.getMessage();

        if (!player.hasPermission("chatsounds.speak"))
            return;

        for (Player p : Bukkit.getOnlinePlayers()){
            p.playSound(player.getLocation(), soundFromMsg(msg), 1f, 1f);
        }
    }

    private String getPrefix(){
        return "§7[§bChatSounds§7] ";
    }

    private String soundFromMsg(String msg) {

        String sound = "custom.chat.";

        if (msg.length() < getConfig().getInt("short"))
            sound += "short";
        else if (msg.length() < getConfig().getInt("medium"))
            sound += "medium";
        else if (msg.length() < getConfig().getInt("long"))
            sound += "long";
        else
            sound += "very_long";

        return sound;
    }
}

package com.jay.chatty;

import com.jay.chatty.command.ChattyCommands;
import com.jay.chatty.command.MuteCommandExecutor;
import com.jay.chatty.event.ChattyEventListener;
import com.jay.chatty.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Chatty plugin Main Class
 * @author Jay
 */
public final class ChattyPlugin extends JavaPlugin {

    private final MuteManager muteManager = new MuteManager();
    private final ChattyEventListener eventListener = new ChattyEventListener(muteManager);
    private final MuteCommandExecutor muteCommandExecutor = new MuteCommandExecutor(muteManager);

    @Override
    public void onEnable() {
        //register commands
        getCommand(ChattyCommands.MUTE_ALL).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.UN_MUTE_ALL).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.MUTE).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.UN_MUTE).setExecutor(muteCommandExecutor);

        //register event listeners
        Bukkit.getPluginManager().registerEvents(eventListener, this);

        getLogger().info("Chatty 加载完成");
    }

    @Override
    public void onDisable() {
        getLogger().info("Chatty 已禁用");
    }
}

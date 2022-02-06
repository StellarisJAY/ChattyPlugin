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
        // 加载持久化数据
        int loadedMutedPlayer = muteManager.loadPersistenceData();
        getLogger().info("禁言玩家列表加载完成，共加载 " + loadedMutedPlayer + " 名被禁言玩家");

        // 注册命令
        getCommand(ChattyCommands.MUTE_ALL).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.UN_MUTE_ALL).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.MUTE).setExecutor(muteCommandExecutor);
        getCommand(ChattyCommands.UN_MUTE).setExecutor(muteCommandExecutor);

        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(eventListener, this);

        getLogger().info("Chatty 加载完成");
    }

    @Override
    public void onDisable() {
        // 持久化被禁言玩家集合
        muteManager.persistence();
        getLogger().info("Chatty 已停用");
    }
}

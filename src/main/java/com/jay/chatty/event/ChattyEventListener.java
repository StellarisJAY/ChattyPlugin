package com.jay.chatty.event;

import com.jay.chatty.mute.MuteManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * <p>
 *  Chatty Event Listener
 * </p>
 *
 * @author Jay
 * @date 2022/02/03 19:35
 */
public class ChattyEventListener implements Listener {

    private final MuteManager muteManager;

    public ChattyEventListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        // 玩家加入游戏，加载玩家的屏蔽列表

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(AsyncChatEvent event){
        if(muteManager.getMuteAllSwitch()){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.AQUA + "无法发送消息，服务器处于禁言状态");
        }
    }
}

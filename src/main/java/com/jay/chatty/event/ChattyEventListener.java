package com.jay.chatty.event;

import com.jay.chatty.mute.MuteManager;
import com.jay.chatty.mute.MutedPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldSaveEvent;

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
        Player player = event.getPlayer();
        String playerName = player.getName();
        // 检查新加入玩家是否被禁言
        MutedPlayer muted = muteManager.getPlayer(playerName);
        if(muted != null){
            // 发送提示
            player.sendMessage(ChatColor.YELLOW + (muted.getMuteEnd() == -1 ? "您的账号处于永久禁言状态，请联系管理员解除" : "您的账号处于禁言状态，剩余时长：" + muted.getMuteTime() / 60000 + " 分钟") );
        }
        // 玩家加入游戏，加载玩家的屏蔽列表
    }

    @EventHandler
    public void onSave(WorldSaveEvent event){
        // 世界保存事件
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(AsyncChatEvent event){
        if(muteManager.getMuteAllSwitch()){
            // 全服禁言已开启
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.AQUA + "无法发送消息，服务器处于禁言状态");
        }
        else if(muteManager.containsPlayer(event.getPlayer().getName())){
            // 玩家被禁言
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.AQUA + "无法发送消息，你已被禁言");
        }
    }
}

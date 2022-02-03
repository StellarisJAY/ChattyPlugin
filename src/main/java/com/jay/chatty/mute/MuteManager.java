package com.jay.chatty.mute;

import com.jay.chatty.ChattyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *
 * </p>
 *
 * @author Jay
 * @date 2022/02/03 19:37
 */
public class MuteManager {
    /**
     * 被禁言玩家集合
     */
    private final ConcurrentHashMap<String, MutedPlayer> mutedPlayers = new ConcurrentHashMap<>();

    private final AtomicBoolean muteAllSwitch = new AtomicBoolean(false);

    public void addPlayer(Player player, long time){
        MutedPlayer mutedPlayer = new MutedPlayer(player.getName(), player, time + System.currentTimeMillis(), time);
        mutedPlayers.put(player.getName(), mutedPlayer);
        if(time != -1){
            Runnable task = ()->{
                MutedPlayer remove = mutedPlayers.remove(player.getName());
                if(remove != null && remove.getMuteEnd() > System.currentTimeMillis()){
                    mutedPlayers.put(player.getName(), remove);
                }else{
                    player.sendMessage(ChatColor.AQUA + "你已被解除禁言，请遵守服务器规则友好交流");
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(ChattyPlugin.getPlugin(ChattyPlugin.class), task);
        }
    }

    public boolean getMuteAllSwitch() {
        return muteAllSwitch.get();
    }

    public boolean setMuteAllSwitch(boolean target){
        return muteAllSwitch.compareAndSet(!target, target);
    }
}

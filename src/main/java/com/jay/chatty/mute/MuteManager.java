package com.jay.chatty.mute;

import com.jay.chatty.util.Scheduler;
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

    /**
     * 全服禁言开关
     */
    private final AtomicBoolean muteAllSwitch = new AtomicBoolean(false);

    public void addPlayer(Player player, long time){
        MutedPlayer mutedPlayer = new MutedPlayer(player.getName(), player, time + System.currentTimeMillis(), time);
        mutedPlayers.put(player.getName(), mutedPlayer);
        if(time != -1){
            // 非永久禁言，定时解除
            Runnable task = ()->{
                MutedPlayer remove = mutedPlayers.remove(player.getName());
                // 检查是否到了禁言结束时间，避免二次禁言后时间改变
                if(remove != null && remove.getMuteEnd() > System.currentTimeMillis()){
                    mutedPlayers.put(player.getName(), remove);
                }else if(remove != null){
                    // 解除成功
                    player.sendMessage(ChatColor.AQUA + "你已被解除禁言，请遵守服务器规则友好交流");
                }
            };
            // 提交定时任务
            Scheduler.schedule(task, time);
        }
    }

    public boolean containsPlayer(String name){
        return mutedPlayers.containsKey(name);
    }

    public MutedPlayer removePlayer(String name){
        return mutedPlayers.remove(name);
    }

    public boolean getMuteAllSwitch() {
        return muteAllSwitch.get();
    }

    public boolean setMuteAllSwitch(boolean target){
        return muteAllSwitch.compareAndSet(!target, target);
    }
}

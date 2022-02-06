package com.jay.chatty.mute;

import com.jay.chatty.constant.ChattyConstants;
import com.jay.chatty.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
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
        MutedPlayer mutedPlayer = new MutedPlayer(player.getName(), player, time == -1 ? -1 : time + System.currentTimeMillis(), time);
        mutedPlayers.put(player.getName(), mutedPlayer);
        if(time != -1){
            // 非永久禁言，定时解除
            MuteScheduledTask task = new MuteScheduledTask(player.getName());
            // 提交定时任务
            Scheduler.schedule(task, time);
        }
    }

    public void persistence(){
        try(FileOutputStream inputStream = new FileOutputStream(ChattyConstants.MUTED_PLAYER_DATA_FILE);
            FileChannel channel = inputStream.getChannel();){
            Set<Map.Entry<String, MutedPlayer>> entries = mutedPlayers.entrySet();
            for (Map.Entry<String, MutedPlayer> entry : entries) {
                MutedPlayer mutedPlayer = entry.getValue();
                String name = mutedPlayer.getName();
                byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
                long muteEnd = mutedPlayer.getMuteEnd();
                // 创建buffer
                ByteBuffer buffer = ByteBuffer.allocate(nameBytes.length + 8 + 1);
                // 写入数据长度
                buffer.put((byte)nameBytes.length);
                // 写入玩家名
                buffer.put(nameBytes);
                // 写入禁言结束时间
                buffer.putLong(muteEnd);

                buffer.rewind();
                channel.write(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    public int loadPersistenceData(){
        int count = 0;
        try(FileInputStream inputStream = new FileInputStream(ChattyConstants.MUTED_PLAYER_DATA_FILE);
            FileChannel channel = inputStream.getChannel()){
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            buffer.rewind();
            while(buffer.hasRemaining()){
                // 读取 name 和 时间
                byte length = buffer.get();
                byte[] nameBytes = new byte[length];
                buffer.get(nameBytes);
                long muteEnd = buffer.getLong();

                String playerName = new String(nameBytes, StandardCharsets.UTF_8);

                // 检查禁言是否已经结束
                if(muteEnd == -1 || muteEnd > System.currentTimeMillis()){
                    // 添加到禁言玩家集合
                    MutedPlayer mutedPlayer = new MutedPlayer(playerName, null, muteEnd, muteEnd == -1 ? -1 : muteEnd - System.currentTimeMillis());
                    mutedPlayers.put(playerName, mutedPlayer);
                    if(muteEnd != -1){
                        // 非永久禁言，创建定时解除任务
                        MuteScheduledTask task = new MuteScheduledTask(playerName);
                        Scheduler.schedule(task, muteEnd - System.currentTimeMillis());
                    }
                    count++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
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

    public MutedPlayer getPlayer(String name){
        return mutedPlayers.get(name);
    }

    class MuteScheduledTask implements Runnable{
        private final String playerName;

        public MuteScheduledTask(String playerName) {
            this.playerName = playerName;
        }
        @Override
        public void run() {
            MutedPlayer remove = mutedPlayers.remove(playerName);
            // 检查是否到了禁言结束时间，避免二次禁言后时间改变
            if(remove != null && remove.getMuteEnd() > System.currentTimeMillis()){
                mutedPlayers.put(playerName, remove);
            }else if(remove != null){
                String name = remove.getName();
                Player player = Bukkit.getPlayer(name);
                if(player != null){
                    player.sendMessage(ChatColor.AQUA + "你已被解除禁言，请遵守服务器规则友好交流");
                }
            }
        }
    }
}

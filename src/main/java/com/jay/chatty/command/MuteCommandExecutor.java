package com.jay.chatty.command;

import com.jay.chatty.mute.MuteManager;
import com.jay.chatty.mute.MutedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 *  禁言命令处理器
 * </p>
 *
 * @author Jay
 * @date 2022/02/03 19:33
 */
public class MuteCommandExecutor implements CommandExecutor {

    private final MuteManager muteManager;

    public MuteCommandExecutor(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandName = command.getName();

        if(commandName.equals(ChattyCommands.MUTE_ALL)){
            if(muteManager.setMuteAllSwitch(true)){
                Bukkit.broadcastMessage(ChatColor.RED + "! ! ! 全服禁言开启 ! ! ! ");
            }else{
                sender.sendMessage("重复命令，服务器已经处于禁言状态。。。");
            }
        }
        else if(commandName.equals(ChattyCommands.UN_MUTE_ALL)){
            if(muteManager.setMuteAllSwitch(false)){
                Bukkit.broadcastMessage(ChatColor.RED + "! ! ! 全服禁言已解除 ! ! ! ");
            }else{
                sender.sendMessage("无效命令，服务器位未于禁言状态。。。");
            }
        }
        else if(commandName.equals(ChattyCommands.MUTE)){
            handleMute(sender, command, label, args);
        }
        else if(commandName.equals(ChattyCommands.UN_MUTE)){
            handleUnMute(sender, command, label, args);
        }
        return true;
    }

    private void handleMute(CommandSender sender, Command command, String label, String[] args){
        if(args.length < 1){
            sender.sendMessage("mute 缺少参数：玩家名、禁言时长");
        }
        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if(player == null){
            sender.sendMessage("没有找到玩家：" + playerName);
        }
        else{
            String arg1 = null;
            if(args.length > 1){
                arg1 = args[1];
            }
            try{
                long time = arg1 == null ? -1 : Long.parseLong(arg1);
                muteManager.addPlayer(player, time * 60 * 1000);
                player.sendMessage(ChatColor.AQUA + "你已被禁言 " + time + " 分钟");
            }catch (Exception e){
                sender.sendMessage("禁言时长无效");
            }
        }
    }

    private void handleUnMute(CommandSender sender, Command command, String label, String[] args){
        if(args.length < 1){
            sender.sendMessage("mute 缺少参数：玩家名、禁言时长");
        }
        String playerName = args[0];
        MutedPlayer mutedPlayer = muteManager.removePlayer(playerName);
        if(mutedPlayer == null){
            sender.sendMessage("无法解除禁言，玩家没有被禁言");
        }else{
            Player player = Bukkit.getPlayer(playerName);
            if(player != null){
                player.sendMessage(ChatColor.AQUA + "你已被解除禁言，请遵守服务器规则友好交流");
            }
        }
    }
}

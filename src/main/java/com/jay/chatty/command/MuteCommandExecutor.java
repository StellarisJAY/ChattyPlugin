package com.jay.chatty.command;

import com.jay.chatty.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        return true;
    }
}

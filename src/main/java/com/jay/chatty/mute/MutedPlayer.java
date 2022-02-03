package com.jay.chatty.mute;

import org.bukkit.entity.Player;

/**
 * <p>
 *  被禁言用户信息
 * </p>
 *
 * @author Jay
 * @date 2022/02/03 19:38
 */
public class MutedPlayer {
    private String name;
    /**
     * player instance
     */
    private Player player;
    /**
     * mute end timestamp
     */
    private long muteEnd;
    /**
     * total mute time, -1 if eternally
     */
    private long muteTime;

    public MutedPlayer(String name, Player player, long muteEnd, long muteTime) {
        this.name = name;
        this.player = player;
        this.muteEnd = muteEnd;
        this.muteTime = muteTime;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public long getMuteEnd() {
        return muteEnd;
    }

    public long getMuteTime() {
        return muteTime;
    }
}

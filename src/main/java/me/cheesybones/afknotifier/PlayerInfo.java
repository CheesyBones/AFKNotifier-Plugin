package me.cheesybones.afknotifier;

public class PlayerInfo {
    public String username;
    public long lastMoveTime;
    public long timeSinceLastMove;
    public boolean isAfk;
    public boolean lastAfkCheck;

    public PlayerInfo(String username, long lastMoveTime, boolean isAfk){
        this.username = username;
        this.lastMoveTime = lastMoveTime;
        this.isAfk = isAfk;

        this.timeSinceLastMove = 0;
        this.lastAfkCheck = this.isAfk;
    }
}

package me.cheesybones.afknotifier;

public class PlayerInfo {
    public String username;
    public int lastMoveTime;
    public int timeSinceLastMove;
    public boolean isAfk;
    public boolean lastAfkCheck;

    public PlayerInfo(String aUsername, int aLastMoveTime, boolean aIsAfk){
        this.username = aUsername;
        this.lastMoveTime = aLastMoveTime;
        this.isAfk = aIsAfk;

        this.timeSinceLastMove = 0;
        this.lastAfkCheck = this.isAfk;
    }
}

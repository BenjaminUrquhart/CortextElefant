package com.noahhendrickson.elefant.infractions;

import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class Infraction {

    private final long id;
    private final String created;
    private final InfractionType type;
    private final String userTag;
    private final String moderatorTag;
    private final long userId;
    private final long moderatorId;
    private final String reason;

    public Infraction(long id, OffsetDateTime created, InfractionType type, User user, User moderator, String reason) {
        this.id = id;
        this.created = created.toString();
        this.type = type;
        this.userTag = user.getAsTag();
        this.moderatorTag = moderator.getAsTag();
        this.userId = user.getIdLong();
        this.moderatorId = moderator.getIdLong();
        this.reason = reason;
    }

    public Infraction(long id, OffsetDateTime created, InfractionType type, User user, User moderator) {
        this(id, created, type, user, moderator, "No Reason");
    }

    public Infraction(InfractionType type, User user, User moderator) {
        this(new InfractionsManager(user).getNextId(), OffsetDateTime.now(), type, user, moderator);
    }

    public long getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public InfractionType getType() {
        return type;
    }

    public String getUserTag() {
        return userTag;
    }

    public String getModeratorTag() {
        return moderatorTag;
    }

    public long getUserId() {
        return userId;
    }

    public long getModeratorId() {
        return moderatorId;
    }

    public String getReason() {
        return reason;
    }
}

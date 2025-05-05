package mp4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Message extends ObjectPlus4 {
    String content;
    User author;

    private final LocalDateTime timestamp;

    public final static int MIN_ACCOUNT_AGE_TO_POST = 5;       //seconds

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm");

    public static final String roleMessageAuthor = "posted by";
    public static final String roleAuthorMessage = "author of";

    public Message(User author, String content) throws Mp4Exception {
        super();

        if (author == null) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Author cannot be null.");
        }


        if (content == null || content.isEmpty()) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Content cannot be null or empty.");
        }

        addLink(roleMessageAuthor, roleAuthorMessage, author);
        this.author = author;


        this.content = content;

        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.SECONDS.between(author.getRegistrationTime(), now) < MIN_ACCOUNT_AGE_TO_POST) {
            throw new Mp4Exception(String.format("Author's account age (%s) has to be at least %s seconds old to post", author.getRegistrationTime(), MIN_ACCOUNT_AGE_TO_POST));
        }

        this.timestamp = LocalDateTime.now();
    }

    public void addToChannel(Channel channel) {
        if (channel == null) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        this.addLink(Channel.roleMessageChannel, Channel.roleChannelMessage, channel);
    }

    public void addToChannelPinned(Channel channel) {
        if (channel == null) {
            removeFromExtent(this);
        }

        try {
            this.addSubsetLink(Channel.rolePinnedChannel, Channel.roleChannelPinned, Channel.roleMessageChannel, Channel.roleChannelMessage, channel);
        } catch (Mp4Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFromChannel(Channel channel) throws Exception {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        Channel[] channels = (Channel[]) this.getLinks(Channel.roleMessageChannel);

        Channel foundChannel = null;

        for (Channel c : channels) {
            if (c.equals(channel)) {
                foundChannel = c;
                break;
            }
        }

        if (foundChannel == null) {
            throw new Mp4Exception(String.format("No link between message %s and channel %s.", this, channel));
        }

        this.removeLink(Channel.roleMessageChannel, Channel.roleChannelMessage, foundChannel);

//        remove subset
        this.removeLink(Channel.rolePinnedChannel, Channel.roleChannelPinned, foundChannel);
    }

    public void removeFromChannelPinned(Channel channel) throws Exception {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        Channel[] channels = (Channel[]) this.getLinks(Channel.rolePinnedChannel);

        Channel foundChannel = null;

        for (Channel c : channels) {
            if (c.equals(channel)) {
                foundChannel = c;
                break;
            }
        }

        if (foundChannel == null) {
            throw new Mp4Exception(String.format("No  link between pinned message %s and channel %s.", this, channel));
        }

        this.removeLink(Channel.rolePinnedChannel, Channel.roleChannelPinned, foundChannel);
    }

    public Channel getChannel() {
        try {
            return (Channel) this.getLinks(Channel.roleMessageChannel)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Channel getChannelPinned() {
        try {
            return (Channel) this.getLinks(Channel.rolePinnedChannel)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DATE_TIME_FORMATTER);
    }

    @Override
    public String toString() {
        return String.format("%s as %s: %s", getFormattedTimestamp(), author.getLogin(), content);
    }
}

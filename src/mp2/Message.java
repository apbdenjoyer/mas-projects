package mp2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message extends ObjectPlus implements Serializable {
    private final String contents;
    private final User author;
    private final LocalDateTime timestamp;

    //    whole
    private Channel channel;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm");

    private Message(Channel channel, User author, String contents) {
        super();

        this.channel = channel;
        this.author = author;
        this.contents = contents;
        this.timestamp = LocalDateTime.now();
    }

    public static Message createMessage(Channel channel, User author,
                                        String contents) throws Exception {

        if (channel == null) {
            throw new IllegalArgumentException("Channel is null.");
        }

        if (author == null) {
            throw new IllegalArgumentException("Author is null.");
        }

        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is null or empty.");
        }

        Message message = new Message(channel, author, contents);

        channel.addPart("contains", "posted in", message);

        return message;
    }

    public String getContents() {
        return contents;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DATE_TIME_FORMATTER);
    }


    @Override
    public String toString() {
        return String.format("%s as %s: %s", getFormattedTimestamp(), getAuthor().getLogin(), getContents());
    }
}

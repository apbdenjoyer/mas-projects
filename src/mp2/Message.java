package mp2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message extends ObjectPlus implements Serializable {
    private final String contents;
    private final User author;
    private final LocalDateTime timestamp;

    //    whole
    private Server server;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm");

    private Message(Server server, User author, String contents) {
        super();

        this.server = server;
        this.author = author;
        this.contents = contents;
        this.timestamp = LocalDateTime.now();
    }

    public static Message createMessage(Server server, User author, String contents) throws Exception {

        if (server == null) {
            throw new IllegalArgumentException("Server is null.");
        }

        if (author == null) {
            throw new IllegalArgumentException("Author is null.");
        }

        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is null or empty.");
        }

        Message message = new Message(server, author, contents);

        server.addPart("contains", "posted in", message);

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

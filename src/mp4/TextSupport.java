package mp4;

public class TextSupport extends ObjectPlus4 {
    private String textStandard;

    public TextSupport(String textStandard) {
        super();

        if (textStandard == null || textStandard.isEmpty()) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Text standard cannot be null or empty");
        }
        this.textStandard = textStandard;
    }

    public void addToChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        try {
            this.addLinkXor(Channel.roleTextChannel, Channel.roleChannelText, channel);
        } catch (Mp4Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Channel getChannel() {
        try {
            return (Channel) this.getLinks(Channel.roleTextChannel)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTextStandard() {
        return textStandard;
    }

    public void setTextStandard(String textStandard) {
        if (textStandard == null || textStandard.isEmpty()) {
            throw new IllegalArgumentException("Text standard cannot be null or empty");
        }

        this.textStandard = textStandard;
    }

    @Override
    public String toString() {
        return String.format("(Text support: %s)", textStandard);
    }
}

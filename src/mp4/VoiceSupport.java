package mp4;

public class VoiceSupport extends ObjectPlus4 {
    private int bitrate;

    public VoiceSupport(int bitrate) {
        super();

        if (bitrate < 0) {
            removeFromExtent(this);
            throw new IllegalArgumentException("Bitrate cannot be negative");
        }
        this.bitrate = bitrate;
    }

    public void addToChannel(Channel channel) throws Mp4Exception {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null.");
        }

        this.addLinkXor(Channel.roleVoiceChannel, Channel.roleChannelVoice, channel);
    }


    public Channel getChannel() {
        try {
            return (Channel) this.getLinks(Channel.roleVoiceChannel)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        if (bitrate < 0) {
            throw new IllegalArgumentException("Bitrate cannot be negative");
        }

        this.bitrate = bitrate;
    }

    @Override
    public String toString() {
        return String.format("(Voice support: %d kbps)", bitrate);
    }
}

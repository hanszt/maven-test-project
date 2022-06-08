package hzt.http.model;

import java.util.Objects;

public final class Transcript {
    private String id;
    private String audioUrl;

    public Transcript() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Transcript) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.audioUrl, that.audioUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, audioUrl);
    }

    @Override
    public String toString() {
        return "Transcript[" +
                "id=" + id + ", " +
                "audio_url=" + audioUrl + ']';
    }

}

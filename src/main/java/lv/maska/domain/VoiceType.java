package lv.maska.domain;

public enum VoiceType {
    SOPRANO_1("Soprāns 1"),
    SOPRANO_2("Soprāns 2"),
    ALTO_1("Alts 1"),
    ALTO_2("Alts 2"),
    TENOR_1("Tenors 1"),
    TENOR_2("Tenors 2"),
    BARITONE("Baritons"),
    BASS("Bass");

    String displayName;
    VoiceType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}

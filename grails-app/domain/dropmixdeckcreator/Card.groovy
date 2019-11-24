package dropmixdeckcreator

class Card {
    Color color
    Level level
    Integer number
    Boolean promotional = false
    Series series = null
    Integer pack = null
    Integer season
    List<Instrument> instruments

    static belongsTo = [playlist: Playlist, song: Song]

    static mapping = {
        promotional defaultValue: '0'

        version false
    }

    static constraints = {
        color enumType: "identity"
        level enumType: "identity"
        number unique: ["season", "promotional"]
        season nullable: true
        series nullable: true
        pack nullable: true
    }

    boolean isWildcard() {
        color == Color.ALL
    }

    boolean isFx() {
        color == Color.WHITE
    }

    String getFullSeasonId() {
        "${this.promotional ? "P" : "S"}${season >= 10 ? season : "0$season"}"
    }

    String getFullCardId() {
        String number = "$number"

        "C${"0" * (3 - number.length())}$number"
    }

    String getFullId() {
        "${this.fullSeasonId}${this.fullCardId}"
    }
}

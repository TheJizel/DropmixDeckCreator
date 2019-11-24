package dropmixdeckcreator

class Song {
    Integer bpm
    Key key
    String name

    static belongsTo = [artist: Artist]

    static hasMany = [cards: Card]

    static mapping = {
        key column: '`key`'

        version false
    }

    static constraints = {
        key enumType: "identity"
    }
}

package dropmixdeckcreator

class Playlist {
    String name
    boolean released = true
    boolean inSeries = false

    static hasMany = [cards: Card]

    static mapping = {
        version false
    }

    static constraints = {
        name unique: true
    }

    boolean isPromotional() {
        cards && !cards.empty && cards.findAll { it.promotional }.size() == cards.size()
    }
}

package dropmixdeckcreator

class Artist {
    String name

    static hasMany = [songs: Song]

    static mapping = {
        version false
    }
}


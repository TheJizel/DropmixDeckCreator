package dropmixdeckcreator

enum Instrument {
    HORNS(1),
    KEYS(2),
    GUITAR(3),
    SAMPLER(4),
    STRINGS(5),
    VOCALS(6),
    DRUMS(7)

    int id

    private Instrument(int id) {
        this.id = id
    }

    String toString() {
        char[] instrument = this.name().toLowerCase().toCharArray()
        instrument[0] = instrument[0].toUpperCase()
        instrument.join('')
    }
}
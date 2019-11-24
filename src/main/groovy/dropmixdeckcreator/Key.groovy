package dropmixdeckcreator

enum Key {
    NA(0),
    C(1),
    Cm(2),
    Db(3),
    Dbm(4),
    D(5),
    Dm(6),
    Eb(7),
    Ebm(8),
    E(9),
    Em(10),
    F(11),
    Fm(12),
    Gb(13),
    Gbm(14),
    G(15),
    Gm(16),
    Ab(17),
    Abm(18),
    A(19),
    Am(20),
    Bb(21),
    Bbm(22),
    B(23),
    Bm(24)

    int id

    private Key(int id) {
        this.id = id
    }

    static Key get(int id) {
        values().find { it.id == id }
    }

    boolean isMajor() {
        !this.id ? false : !isMinor()
    }

    boolean isMinor() {
        !this.id ? false : this.id % 2 == 0
    }

    String toString() {
        if (!this.id) {
            return "n/a"
        }

        this.name().replace('m', '') + (isMinor() ? ' Minor' : ' Major')
    }

    static Key findByName(String name) {
        for (Key key : values()) {
            if ("$key" == name) {
                return key
            }
        }

        return null
    }

    Key getSubDominant() {
        if (!this.id) {
            return this
        }

        int potentialId = this.id + 10

        get(potentialId <= 24 ? potentialId : potentialId % 24)
    }

    Key getDominant() {
        if (!this.id) {
            return this
        }

        int potentialId = this.id + 14

        get(potentialId <= 24 ? potentialId : potentialId % 24)
    }

    Key getRelative() {
        if (!this.id) {
            return this
        }

        int potentialId = this.id + (this.major ? 19 : 5)

        get(potentialId <= 24 ? potentialId : potentialId % 24)
    }

    Key getParallel() {
        if (!this.id) {
            return this
        }

        isMajor() ? get(this.id + 1) : get(this.id - 1)
    }

    List<Key> getMixSet() {
        [this, this.subDominant, this.dominant, this.relative]
    }
}
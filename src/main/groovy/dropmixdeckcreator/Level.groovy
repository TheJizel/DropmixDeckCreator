package dropmixdeckcreator

enum Level {
    ONE(1),
    TWO(2),
    THREE(3)

    int id

    private Level(int id) {
        this.id = id
    }

    static Level get(int id) {
        values().find { it.id == id }
    }

    String toString() {
        this.id
    }
}
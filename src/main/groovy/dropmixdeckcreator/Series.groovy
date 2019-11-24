package dropmixdeckcreator

enum Series {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4)

    int id

    private Series(int id) {
        this.id = id
    }

    static Series get(int id) {
        values().find { it.id == id }
    }

    String toString() {
        char[] series = this.name().toLowerCase().toCharArray()
        series[0] = series[0].toUpperCase()
        series.join('')
    }
}
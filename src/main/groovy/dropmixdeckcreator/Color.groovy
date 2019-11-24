package dropmixdeckcreator

enum Color {
    ALL(1, "#b4a7d6"),
    BLUE(2, "#9fc5e8"),
    GREEN(3, "#b7e1cd"),
    RED(4, "#e6b8af"),
    WHITE(5, "#f3f3f3"),
    YELLOW(6, "#ffe599")

    int id
    String hex

    private Color(int id, String hex) {
        this.id = id
        this.hex = hex
    }

    static Color get(int id) {
        values().find { it.id == id }
    }

    String toString() {
        char[] color = this.name().toLowerCase().toCharArray()
        color[0] = color[0].toUpperCase()
        color.join('')
    }

    static Color findByName(String name) {
        for (Color color : values()) {
            if ("$color" == name) {
                return color
            }
        }

        return null
    }
}
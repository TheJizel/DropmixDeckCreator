package dropmixdeckcreator

import static Level.*

enum FxTemplate {
    TWO_PLUS_TWO(1, [(ONE): 0, (TWO): 2, (THREE): 0]),
    ONE_PLUS_THREE(2, [(ONE): 1, (TWO): 0, (THREE): 1]),
    TWO_PLUS_THREE(3, [(ONE): 0, (TWO): 1, (THREE): 1]),
    ONE_PLUS_TWO(4, [(ONE): 1, (TWO): 1, (THREE): 0]),
    THREE_PLUS_THREE(5, [(ONE): 0, (TWO): 0, (THREE): 2])

    int id
    Map<Level, Integer> composition

    private FxTemplate(int id, Map<Level, Integer> composition) {
        this.id = id
        this.composition = composition
    }
}
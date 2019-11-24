package dropmixdeckcreator

import static Level.*

enum ColorTemplate {
    BALANCED(1, [(ONE): 1, (TWO): 1, (THREE): 1]),
    OVERLOAD(2, [(ONE): 2, (TWO): 1, (THREE): 1]),
    UNDERPOWERED(3, [(ONE): 0, (TWO): 1, (THREE): 1]),
    STANDARD_WILDCARDS(4, [(ONE): 0, (TWO): 1, (THREE): 0])

    int id
    Map<Level, Integer> composition

    private ColorTemplate(int id, Map<Level, Integer> composition) {
        this.id = id
        this.composition = composition
    }
}
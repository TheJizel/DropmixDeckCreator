package dropmixdeckcreator

class RandomUtil {
    static final Random RANDOM = new Random()

    static <E> E getRandomElement(Collection<E> collection) {
        collection ? collection[RANDOM.nextInt(collection.size())] : null
    }
}

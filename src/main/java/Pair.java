class Pair<First,Second> {

    private final First first;
    private final Second second;

    Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    First getFirst() {
        return first;
    }

    Second getSecond() {
        return second;
    }
}

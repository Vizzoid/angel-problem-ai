package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Difficulty {
    TRIVIAL(0),
    EASY(1),
    MEDIUM(2),
    HARD(3),
    IMPOSSIBLE(4);

    private static final @NotNull List<? extends Difficulty> ORDERED = List.of(TRIVIAL, EASY, MEDIUM, HARD, IMPOSSIBLE); // maintains order regardless of ordinal
    private final int id;

    Difficulty(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static @NotNull List<? extends Difficulty> ordered() { // maintains order regardless of ordinal
        return ORDERED;
    }

    public static @NotNull Difficulty random() {
        return random(TRIVIAL, IMPOSSIBLE);
    }

    /**
     * @param bound inclusive
     */
    public static @NotNull Difficulty random(@NotNull Difficulty bound) {
        return random(TRIVIAL, bound);
    }

    /**
     * @param origin inclusive
     * @param bound  inclusive
     */
    public static @NotNull Difficulty random(@NotNull Difficulty origin, @NotNull Difficulty bound) {
        return ORDERED.get(ThreadLocalRandom.current().nextInt(origin.id, bound.id + 1));
    }

    public static @NotNull Difficulty randomClassic() { // returns classic enum
        return random(EASY, HARD);
    }

}

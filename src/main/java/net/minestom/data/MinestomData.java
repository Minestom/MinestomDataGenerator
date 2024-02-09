package net.minestom.data;

public final class MinestomData {
    private static final String COMMIT = "&COMMIT";
    private static final String BRANCH = "&BRANCH";

    public static String commit() { return COMMIT; }
    public static String branch() { return BRANCH; }

    private MinestomData() {}
}

package aoc2020.days;

public class Day25
{
    private static final int PUBLIC_KEY_CARD = 1965712;
    private static final int PUBLIC_KEY_DOOR = 19072108;

    private static final int MAGIC_DIVIDER = 20201227;

    public long part1()
    {
        int cardLoopSize = calculateLoopSize(7, PUBLIC_KEY_CARD);
        int doorLoopSize = calculateLoopSize(7, PUBLIC_KEY_DOOR);

        long keyCalculatedByCard = calculateEncryptionKey(PUBLIC_KEY_DOOR, cardLoopSize);
        long keyCalculatedByDoor = calculateEncryptionKey(PUBLIC_KEY_CARD, doorLoopSize);
        if (keyCalculatedByCard != keyCalculatedByDoor) {
            return 0L;
        }

        return keyCalculatedByCard;
    }

    private static int calculateLoopSize(int subjectNumber, int publicKey)
    {
        int loopSize;

        int currentPublicKey = 1;
        for (loopSize = 0; currentPublicKey != publicKey; loopSize++) {
            currentPublicKey *= subjectNumber;
            currentPublicKey %= MAGIC_DIVIDER;
        }

        return loopSize;
    }

    private static long calculateEncryptionKey(int subjectNumber, int loopSize)
    {
        long encryptionKey = 1L;
        for (int i = 0; i < loopSize; i++) {
            encryptionKey *= subjectNumber;
            encryptionKey %= MAGIC_DIVIDER;
        }

        return encryptionKey;
    }
}

import java.io.IOException;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Ruoting Li
 * @version (2016.04.12)
 */
public class CharCounter
    implements ICharCounter, IHuffConstants
{
    private int[] array = new int[ALPH_SIZE + 1];


    /**
     * Returns the count associated with specified character.
     *
     * @param ch
     *            is the chunk/character for which count is requested
     * @return count of specified chunk
     * @throws some
     *             kind of exception if ch isn't a valid chunk/character
     */
    public int getCount(int ch)
    {
        return array[ch];
    }


    /**
     * Initialize state by counting bits/chunks in a stream
     *
     * @param stream
     *            is source of data
     * @return count of all chunks/read
     * @throws IOException
     *             if reading fails
     */
    public int countAll(InputStream stream)
        throws IOException
    {
        BitInputStream bits = (BitInputStream)stream;
        int inbits;
        int count = 0;

        while ((inbits = bits.read(BITS_PER_WORD)) != -1)
        {
            System.out.println((char)inbits);
            add(inbits);
            count++;
        }

        return count;
    }


    /**
     * Update state to record one occurrence of specified chunk/character.
     *
     * @param i
     *            is the chunk being recorded
     */
    public void add(int i)
    {
        array[i]++;
    }


    /**
     * Set the value/count associated with a specific character/chunk.
     *
     * @param i
     *            is the chunk/character whose count is specified
     * @param value
     *            is # occurrences of specified chunk
     */
    public void set(int i, int value)
    {
        array[i] = value;
    }


    /**
     * All counts cleared to zero.
     */
    public void clear()
    {
        for (int i = 0; i < array.length; i++)
        {
            set(i, 0);
        }
    }

}

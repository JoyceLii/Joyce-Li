import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Ruoting Li
 *  @version (2016.04.12)
 */
public class MainClass implements IHuffConstants
{
  //add main method
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param args
     */
    public static void main(String[ ] args) {
        CharCounter cc = new CharCounter();

        BitInputStream bit = null;
        try
        {
            bit = new BitInputStream(new FileInputStream("test.txt"));
        }
        catch (FileNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try
        {
            cc.countAll(bit);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int k = 0; k < ALPH_SIZE; k++) {

        int occs = cc.getCount(k);

        if (occs > 0) {

        System.out.println((char)k+ " occurs "+ occs);

        }

        }


    }
}

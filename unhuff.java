import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 * Write a class called unhuff to test the uncompress method
 *
 * @author Ruoting Li
 * @author Camden Fischer
 * @version May 9th 2016
 */
public class unhuff
{
    public static void main(String[] args)
        throws IOException
    {
        BitOutputStream out = new BitOutputStream("test.txt.huff.huff");
        String s = args[0];
        File file = new File(s);
        InputStream h = new BitInputStream(file);
        CountDisplay countDisplay = new CountDisplay();
        countDisplay.uncompress(h, out);

    }
}

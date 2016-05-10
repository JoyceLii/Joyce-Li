import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 * Write a class called Huff to test compressed method of the file
 *
 * @author Ruoting Li
 * @author Camden Fischer
 * @version May 9th 2016
 */
public class Huff
{
    public static void main(String[] args)
        throws IOException
    {
        InputStream input = null;
        String s = args[0];
        File file = new File(s);
        boolean force = false;

        if (args[0].equals("true"))
        {
            force = true;
        }

        CountDisplay countDisplay = new CountDisplay();
        countDisplay.initialize(input);
        countDisplay.showCounts();
        System.out.println("Show codings:");
        countDisplay.showCodings();
        System.out.println("Compressed file: " + s + " to " + s + ".huff");
        countDisplay.write(input, file, force);

    }
}

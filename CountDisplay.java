import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Camden Fischer
 * @version Apr 11, 2016
 */
public class CountDisplay
    implements IHuffModel
{
    /**
     * Creating a CharCounter
     */
    ICharCounter        cc = new CharCounter();
    /**
     * Creating a BitInputStream
     */
    private InputStream bits;

    static MinHeap      Hheap;


    public void initialize(InputStream stream)
    {
        try
        {
            bits = new BitInputStream(
                new FileInputStream(
                    "X:\\Documents\\Spring 2016\\CSE 017\\text1.txt"));
        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }

    }


    public void showCounts()
    {

        try
        {
            cc.countAll(bits);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < 256; i++)
        {
            if (cc.getCount(i) != 0)
            {
                System.out.println((char)i + ": " + cc.getCount(i));
            }
        }

    }


    public void showCodings()
    {
        int count = 0;

        try
        {
            cc.countAll(bits);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < 256; i++)
        {
            if (cc.getCount(i) != 0)
            {
                count++;
            }
        }
        HuffTree[] treeArr = new HuffTree[count];
        int x = 0;
        for (int i = 0; i < 256; i++) // creating an array of trees that have
                                      // non zero counts
        {
            if (cc.getCount(i) != 0)
            {
                treeArr[x] = new HuffTree((char)i, cc.getCount(i));
            }
        }

        String[] s = new String[255];
        Hheap = new MinHeap(treeArr, count, 255);
        for (int i = 0; i < count; i++)
        {
            if (Hheap.isLeaf(i))
            {
                s = traversal(
                    (TreeNode)treeArr[i].root(),
                    treeArr[i].toString(),
                    "");
            }
        }

        for (int i = 0; i < s.length; i++)
        {
            System.out.print(s[i]);
        }
    }


    // ----------------------------------------------------------
    /**
     * create a method called traversal to encode
     *
     * @param root
     *            is a TreeNode type
     * @param s
     *            is a String type
     * @return return the String[] type array
     */
    public String[] traversal(TreeNode root, String letter, String s)
    {
        String tempCode = "";
        if (root.myLeft == null && root.myRight == null)
        {
            if (root != null && root.equals(letter))
                tempCode = s;
        }
        else
        {
            if (root.myLeft != null)
            {
                traversal(root.myLeft, letter, s + "0");
            }
            if (root.myRight != null)
            {
                traversal(root.myRight, letter, s + "1");
            }
        }
        String[] array = new String[tempCode.length()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = String.valueOf(tempCode.charAt(i));
        }
        return array;
    }


    public void write(InputStream stream, File file, boolean force)
    {
        // TODO Auto-generated method stub

    }


    public void uncompress(InputStream in, OutputStream out)
    {
        // TODO Auto-generated method stub

    }


    static HuffTree buildTree()
    {
        HuffTree tmp1, tmp2, tmp3 = null;

        while (Hheap.heapsize() > 1)
        { // While two items left
            tmp1 = (HuffTree)Hheap.removemin();
            tmp2 = (HuffTree)Hheap.removemin();
            tmp3 = new HuffTree(
                tmp1.root(),
                tmp2.root(),
                tmp1.weight() + tmp2.weight());
            Hheap.insert(tmp3); // Return new tree to heap
        }
        return tmp3; // Return the tree
    }

}

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

    private int[]       coding;


    public void initialize(InputStream stream)
    {
        try
        {
            bits = new BitInputStream(new FileInputStream("test.txt"));
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
            bits = new BitInputStream(new FileInputStream("test.txt"));
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
            bits = new BitInputStream(new FileInputStream("test.txt"));
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
                x++;
            }
        }

        Hheap = new MinHeap(treeArr, count, 256);
        HuffTree tree = buildTree(Hheap);
        coding = new int[count];

        traversal(tree.root(), coding, 0);

    }


    // ----------------------------------------------------------
    /**
     * create a method called traversal to encode
     *
     * @param node
     *            is a HuffBaseNode type
     * @param codes
     *            is an integer array type
     * @param top
     *            is an integer type
     */
    public void traversal(HuffBaseNode node, int[] codes, int top)
    {
        if (((HuffInternalNode)node).left() != null)
        {
            codes[top] = 0;
            traversal(((HuffInternalNode)node).left(), codes, top + 1);
        }
        if (((HuffInternalNode)node).right() != null)
        {
            codes[top] = 1;
            traversal(((HuffInternalNode)node).right(), codes, top + 1);
        }
        if (node.isLeaf())
        {
            System.out.printf("%c: ", ((HuffLeafNode)node).element());
            printArray(codes, top);
        }

    }


    // ----------------------------------------------------------
    /**
     * create a method called printArray to print the encoding
     * @param array is an integer array
     * @param n is an integer
     */
    public void printArray(int array[], int n)
    {
        for (int i = 0; i < n; i++)
        {
            System.out.printf("%d", array[i]);
            System.out.println();
        }
    }


    public void write(InputStream stream, File file, boolean force)
    {
        // TODO Auto-generated method stub

    }


    public void uncompress(InputStream in, OutputStream out)
    {
        // TODO Auto-generated method stub

    }


    static HuffTree buildTree(MinHeap Hheap)
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

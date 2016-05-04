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
 * @author Camden Fischer Ruoting Li
 * @version Apr 11, 2016
 */
public class CountDisplay
    implements IHuffModel
{
    /**
     * Creating a CharCounter
     */
    ICharCounter           cc     = new CharCounter();
    /**
     * Creating a BitInputStream
     */
    private InputStream    bits;

    static MinHeap         Hheap;

    private String[]       coding;

    ArrayListStack<String> stack  = new ArrayListStack<String>();

    HuffTree               tree;

    private int            number = 0;
    private int            bit;

    private HuffBaseNode   node;


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
        HuffTree[] treeArr = new HuffTree[count + 1];
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
        // treeArr[count + 1] = new HuffTree((char)PSEUDO_EOF, 1);

        Hheap = new MinHeap(treeArr, count, 256);
        HuffTree tree1 = buildTree(Hheap);
        System.out.println();
        coding = new String[count];
        traversal(tree1.root());
    }


    // ----------------------------------------------------------
    /**
     * create a method called traversal to encode
     *
     * @param node
     *            is a HuffBaseNode type
     */
    public void traversal(HuffBaseNode node)
    {
        if (node == null)
        {
            return;
        }
        if (node.isLeaf())
        {
            String s = new String();
            for (int i = 0; i < stack.size(); i++)
            {
                s += stack.get(i);
            }
            System.out.println(((HuffLeafNode)node).element() + " " + s);
            coding[number] = ((HuffLeafNode)node).element() + " " + s;
            number++;
            stack.pop();
        }

        else
        {
            stack.push("0");
            traversal(((HuffInternalNode)node).left());
            stack.push("1");
            traversal(((HuffInternalNode)node).right());
            if (stack.size() > 0)
            {
                stack.pop();
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * create a method called displayCoding
     */
    public void displayCoding()
    {
        for (int i = 0; i < coding.length; i++)
        {
            System.out.println(coding[i]);
        }
    }


    public void write(InputStream stream, File file, boolean force)
    {
        BitOutputStream out = new BitOutputStream("test.txt");
        out.write(BITS_PER_INT, MAGIC_NUMBER);
        traverse(tree.root(), out);
        String encoding = new String();
        char[] encodingCharArray;

        BitInputStream bits;
        try
        {
            bits = new BitInputStream(new FileInputStream("test.txt"));
            int inbits;
            try
            {
                while ((inbits = bits.read(BITS_PER_WORD)) != -1)
                {
                    for (int i = 0; i < coding.length; i++)
                    {
                        encoding += coding[i];
                    }
                    encodingCharArray = encoding.toCharArray();
                    for (int j = 0; j < encodingCharArray.length; j++)
                    {
                        out.write(1, (int)encodingCharArray[j]);
                    }
                }

            }
            catch (IOException e)
            {

                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e1)
        {

            e1.printStackTrace();
        }

        out.close();
    }


    // ----------------------------------------------------------
    /**
     * Preorder traversal that writes 1s and 0s as needed. part 3 step 2
     *
     * @param root
     *            of hufftree
     * @param out
     *            output stream
     */
    public void traverse(HuffBaseNode root, BitOutputStream out)
    {
        if (root == null)
        {
            return;
        }
        if (!root.isLeaf())
        {
            out.write(1, 0);
        }
        else
        {
            out.write(1, 1);
            out.write(9, ((HuffLeafNode)root).element());
        }
        traverse(((HuffInternalNode)root).left(), out);
    }


    public void uncompress(InputStream in, OutputStream out)
    {
        int inbits;
        int magic;
        try
        {
            magic = ((BitInputStream)in).read(BITS_PER_INT);
            if (magic != MAGIC_NUMBER)
            {
                throw new IOException("magic number not right");
            }
            while (true)
            {
                inbits = ((BitInputStream)in).read(1);
                if (inbits == -1)
                {
                    throw new IOException("unexpected end of input file");
                }
                else
                {
                    if ((inbits & 1) == 0)
                    {
                        traversal(((HuffInternalNode)node).left());
                    }
                    else
                    {
                        traversal(((HuffInternalNode)node).right());
                    }
                    if (node.isLeaf())
                    {
                        if (((HuffLeafNode)node).element() == PSEUDO_EOF)
                        {
                            break;
                        }
                        else
                        {
                            out.write(((HuffLeafNode)node).element());
                        }
                    }
                }
            }
            in.close();
            out.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * create a method called reBuildTree
     *
     * @return a HuffBaseNode type
     * @throws IOException
     */
    public HuffBaseNode reBuildTree()
        throws IOException
    {
        bit = ((BitInputStream)bits).read(1);
        if (bit == 0)
        {
            node = new HuffInternalNode(reBuildTree(), reBuildTree(), 0);
        }
        else if (bit == 1)
        {
            bit = ((BitInputStream)bits).read(9);
            return new HuffLeafNode((char)bit, 0);
        }
        return node;
    }


    // ----------------------------------------------------------
    /**
     * create a method called buildTree
     *
     * @param Hheap
     *            is a MinHeap type
     * @return a HuffTree object
     */
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

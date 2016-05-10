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
 * @author Ruoting Li
 * @version May 9th, 2016
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
    /**
     * Create a MinHeap Hheap
     */
    static MinHeap         Hheap;
    /**
     * Create String[] array called coding to store the encoding
     */
    private String[]       coding;
    /**
     * create a stack to write traversal method
     */
    ArrayListStack<String> stack  = new ArrayListStack<String>();

    /**
     * Create a variable of HuffTree type
     */
    HuffTree               tree;
    /**
     * create a number with initial value 0
     */
    private int            number = 0;
    /**
     * create a bit in integer type
     */
    private int            bit;
    /**
     * create a HuffBaseNode type node used to write reBuildTree
     */
    private HuffBaseNode   node;
    /**
     * create a char array called encodingCharArray
     */
    char[]                 encodingCharArray;


    /**
     * write a method called initialize
     * @param stream is an InputStream
     */
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

    /**
     * write a method called showCounts
     */
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
        System.out.println();
        for (int i = 0; i < 256; i++)
        {
            if (cc.getCount(i) != 0)
            {
                System.out.println((char)i + ": " + cc.getCount(i));
            }
        }

    }

    /**
     * write a method called showCodings
     * show the encoding of the tree
     */
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
        treeArr[count] = new HuffTree((char)PSEUDO_EOF, 1);

        Hheap = new MinHeap(treeArr, count, 256);
        tree = buildTree(Hheap);
        System.out.println();
        coding = new String[count];
        traversal(tree.root());
    }


    // ----------------------------------------------------------
    /**
     * create a method called traversal to encode
     * @param node1 is a HuffBaseNode type
     */
    public void traversal(HuffBaseNode node1)
    {
        if (node1 == null)
        {
            return;
        }
        if (node1.isLeaf())
        {
            String s = new String();
            for (int i = 0; i < stack.size(); i++)
            {
                s += stack.get(i);
            }
            System.out.println(((HuffLeafNode)node1).element() + " " + s);
            coding[number] = ((HuffLeafNode)node1).element() + " " + s;
            number++;
            stack.pop();
        }

        else
        {
            stack.push("0");
            traversal(((HuffInternalNode)node1).left());
            stack.push("1");
            traversal(((HuffInternalNode)node1).right());
            if (stack.size() > 0)
            {
                stack.pop();
            }
        }
    }

    // ----------------------------------------------------------
    /**
     * write a method called write to transfer encoding to a char array
     * @param stream is an InputStream
     * @param file is a File type
     * @param force is a boolean type
     */
    public void write(InputStream stream, File file, boolean force)
    {
        BitOutputStream out = new BitOutputStream("test.txt");
        BitOutputStream out1 = new BitOutputStream("test.txt.huff");
        out.write(BITS_PER_INT, MAGIC_NUMBER);
        // traverse(tree.root(), out);
        String encoding = new String();
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
                        out1.write(1, (int)encodingCharArray[j]);
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
        out1.close();
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
        if (root != null)
        {
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
            traverse(((HuffInternalNode)root).right(), out);
        }
    }


    public void uncompress(InputStream in, OutputStream out)
    {
        int inbits;
        int magic;
        try
        {
            node = reBuildTree(in);
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
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
                        traverse(
                            ((HuffInternalNode)node).left(),
                            (BitOutputStream)out);
                    }
                    else
                    {
                        traverse(
                            ((HuffInternalNode)node).right(),
                            (BitOutputStream)out);
                    }
                    if (tree.root().isLeaf())
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
    public HuffBaseNode reBuildTree(InputStream in)
        throws IOException
    {
        bit = ((BitInputStream)in).read(1);
        if (bit == 0)
        {
            node = new HuffInternalNode(reBuildTree(in), reBuildTree(in), 0);
        }
        else if (bit == 1)
        {
            bit = ((BitInputStream)in).read(9);
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

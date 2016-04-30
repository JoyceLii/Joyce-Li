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

    private String[]       coding;

    ArrayListStack<String> stack = new ArrayListStack<String>(      );

    HuffTree tree;


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
        //treeArr[count + 1] = new HuffTree((char)PSEUDO_EOF, 1);

        Hheap = new MinHeap(treeArr, count, 256);
        HuffTree tree1 = buildTree(Hheap);
        System.out.println();
        traversal(tree1.root(), count, 0);

    }


    // ----------------------------------------------------------
    /**
     * create a method called traversal to encode
     *
     * @param node
     *            is a HuffBaseNode type
     * @param count
     *            is an integer array type
     * @param index
     *            is an integer type
     */
    public void traversal(HuffBaseNode node, int count, int index)
    {
        coding = new String[count];
        if(node == null) {
            return;
        }

        if(node.isLeaf()) {
            String s = new String();
            for(int i = 0; i<stack.size();i++) {
                   s += stack.get(i);
            }
            System.out.println(((HuffLeafNode)node).element()+" "+s);
            coding[index] = s;
            index++;
            stack.pop();
        }

        else {
            stack.push("0");
            traversal(((HuffInternalNode)node).left(),count,index+1);
            stack.push("1");
            traversal(((HuffInternalNode)node).right(),count,index+1);
            if(stack.size() > 0) {
                stack.pop();
            }
        }


    }




    // ----------------------------------------------------------
    /**
     * create a method called printArray to print the encoding
     *
     * @param array
     *            is an integer array
     * @param n
     *            is an integer
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
        BitOutputStream out = new BitOutputStream("test.txt");
        out.write(BITS_PER_INT, MAGIC_NUMBER);
        traverse(tree.root(), out);
        out.write(1, 1);
        out.write(9, PSEUDO_EOF);
        String [] array = new String[257];
        int count = 0;

        BitInputStream bits;
        try
        {
            bits = new BitInputStream(new FileInputStream("test.txt"));
            int inbits;
            try
            {
                while((inbits = bits.read(BITS_PER_WORD)) != -1) {
                    for (int k = 0; k < ALPH_SIZE; k++) {
                          int occs = cc.getCount(k);
                          for(int i = 0;i<array.length;i++) {
                              if (i == occs) {

                              }
                          }
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

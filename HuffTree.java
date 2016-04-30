// -------------------------------------------------------------------------
/**
 * Huffman Coding Tree
 *
 * @author Camden Fischer
 * @version Apr 17, 2016
 */
class HuffTree
    implements Comparable
{
    private HuffBaseNode root;


    // ----------------------------------------------------------
    /**
     * Create a new HuffTree object.
     *
     * @param el
     * @param wt
     */
    HuffTree(char el, int wt)
    {
        root = new HuffLeafNode(el, wt);
    }


    // ----------------------------------------------------------
    /**
     * Create a new HuffTree object.
     *
     * @param l
     * @param r
     * @param wt
     *            weight
     */
    HuffTree(HuffBaseNode l, HuffBaseNode r, int wt)
    {
        root = new HuffInternalNode(l, r, wt);
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @return the root of the tree
     */
    HuffBaseNode root()
    {
        return root;
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @return the weight
     */
    int weight() // Weight of tree is weight of root
    {
        return root.weight();
    }


    public int compareTo(Object t)
    {
        HuffTree that = (HuffTree)t;
        if (root.weight() < that.weight())
            return -1;
        else if (root.weight() == that.weight())
            return 0;
        else
            return 1;
    }
}

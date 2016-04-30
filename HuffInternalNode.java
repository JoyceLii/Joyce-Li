/** Huffman tree node: Internal class */
class HuffInternalNode
    implements HuffBaseNode
{
    private int          weight;
    private HuffBaseNode left;
    private HuffBaseNode right;


    // ----------------------------------------------------------
    /**
     * Create a new HuffInternalNode object.
     *
     * @param l
     * @param r
     * @param wt
     */
    HuffInternalNode(HuffBaseNode l, HuffBaseNode r, int wt)
    {
        left = l;
        right = r;
        weight = wt;
    }


    // ----------------------------------------------------------
    /**
     * getter for left child
     *
     * @return left child
     */
    HuffBaseNode left()
    {
        return left;
    }


    /**
     * getter for right child
     *
     * @return The right child
     */
    HuffBaseNode right()
    {
        return right;
    }


    /**
     * getter for weight
     *
     * @return The weight
     */
    public int weight()
    {
        return weight;
    }


    /**
     * check if it is a leaf
     *
     * @Return false
     */
    public boolean isLeaf()
    {
        return false;
    }
}

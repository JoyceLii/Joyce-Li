/** Huffman tree node: Leaf class */
class HuffLeafNode
    implements HuffBaseNode
{
    private char element; // Element for this node
    private int  weight;  // Weight for this node


    // ----------------------------------------------------------
    /**
     * Create a new HuffLeafNode object.
     *
     * @param el
     * @param wt
     */
    HuffLeafNode(char el, int wt)
    {
        element = el;
        weight = wt;
    }


    /**
     * returns the element
     *
     * @return The element value
     */
    char element()
    {
        return element;
    }


    /**
     * returns weight
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
     * @Return true
     */
    public boolean isLeaf()
    {
        return true;
    }
}

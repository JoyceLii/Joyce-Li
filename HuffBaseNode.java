// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Camden Fischer
 * @version Apr 17, 2016
 */
interface HuffBaseNode
{
    // ----------------------------------------------------------
    /**
     * Check if it a leaf
     *
     * @return boolean
     */
    boolean isLeaf();


    // ----------------------------------------------------------
    /**
     * Get weight
     *
     * @return int weight
     */
    int weight();
}
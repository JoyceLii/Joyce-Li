import java.util.ArrayList;
import java.util.EmptyStackException;

// -------------------------------------------------------------------------
/**
 * An implementation of the stack data type that adapts an ArrayList to store
 * its contents. This is a PARTIAL IMPLEMENTATION that needs completion.
 *
 * @param <T>
 *            the type of elements stored in the stack
 * @author Tony Allevato (authored class skeleton)
 * @author Ruoting Li
 * @version (2016.03.11)
 */
public class ArrayListStack<T>
    implements SimpleStack<T>
{
    // ~ Instance/static variables ............................................

    private final ArrayList<T> list;


    // ----------------------------------------------------------
    /**
     * Create a new ArrayListStack object.
     */
    // ~ Constructors .........................................................

    // ----------------------------------------------------------
    public ArrayListStack()
    {
        list = new ArrayList<T>();
    }


    // ~ Methods ..............................................................

    // ----------------------------------------------------------
    /**
     * override a method called push
     *
     * @param item
     *            is a T type
     */
    public void push(T item)
    {
        list.add(item);
    }


    // ----------------------------------------------------------
    /**
     * override a method called pop
     */
    public void pop()
    {
        if (list.size() != 0)
        {
            list.remove(list.size() - 1);
        }
        else
        {
            throw new EmptyStackException();
        }
    }


    // ----------------------------------------------------------
    /**
     * override a method called top
     *
     * @return return a T type
     */
    public T top()
    {
        if (list.size() != 0)
        {
            return list.get(list.size() - 1);
        }
        else
        {
            throw new EmptyStackException();
        }
    }


    // ----------------------------------------------------------
    /**
     * override a method called size
     *
     * @return return an integer type
     */
    public int size()
    {
        return list.size();
    }


    // ----------------------------------------------------------
    /**
     * override a method called push
     *
     * @return return a boolean type
     */
    public boolean isEmpty()
    {
        return (list.size() == 0);
    }

    public T get(int i) {
        if (list.size() != 0)
        {
            return list.get(i);
        }
        else
        {
            throw new EmptyStackException();
        }
    }
}

package ai.ilikeplaces.jpa;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @param <T>
 * @author Ravindranath Akila
 */
@Local
public interface CrudServiceLocal<T> {

    /**
     *
     * @param t
     * @return
     */
    public T create(T t);

    /**
     *
     * @param type
     * @param id
     * @return
     */
    public T find(Class type, Object id);

    /**
     *
     * @param t
     * @return
     */
    public T update(T t);

    /**
     *
     * @param type
     * @param id
     */
    public void delete(Class type, Object id);

    /**
     *
     * @param queryName
     * @return
     */
    public List findWithNamedQuery(String queryName);

    /**
     *
     * @param queryName
     * @param resultLimit
     * @return
     */
    public List findWithNamedQuery(String queryName, int resultLimit);

    /**
     *
     * @param namedQueryName
     * @param parameters
     * @return
     */
    public List findWithNamedQuery(String namedQueryName, Map parameters);

    /**
     *
     * @param namedQueryName
     * @param parameters
     * @param resultLimit
     * @return
     */
    public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit);

    /**
     *
     * @param sql
     * @param type
     * @return ListF
     */
    public List findByNativeQuery(final String sql, final Class type);
}
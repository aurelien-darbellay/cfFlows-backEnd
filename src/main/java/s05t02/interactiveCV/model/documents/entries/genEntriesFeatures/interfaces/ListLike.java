package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces;

import java.util.Collection;
import java.util.List;

public interface ListLike<T> {
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    boolean add(T t);

    boolean remove(Object o);

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends T> c);

    boolean addAll(int index, Collection<? extends T> c);

    boolean removeAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    void clear();

    T get(int index);

    T set(int index, T element);

    void add(int index, T element);

    T remove(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    List<T> subList(int fromIndex, int toIndex);
}

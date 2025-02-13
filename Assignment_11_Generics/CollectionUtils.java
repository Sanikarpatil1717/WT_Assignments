import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CollectionUtils {
    public static <T> List<T> searchByCriteria(Iterable<T> collection, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : collection) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}

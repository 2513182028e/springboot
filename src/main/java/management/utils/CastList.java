package management.utils;

import java.util.ArrayList;
import java.util.List;

public class CastList {

                                                                     
                                                                     //将强行转为Object类型的List对象，由转为List类型
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {

                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}

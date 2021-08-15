package management.utils;

import management.entity.Student;
import management.entity.ViewClass;

import java.util.Comparator;



public class ViesComparator implements Comparator<ViewClass> {


    @Override
    public int compare(ViewClass o1, ViewClass o2) {
        if(Integer.parseInt(o1.getTeacherID())>Integer.parseInt(o2.getTeacherID()))
        {
            return 1;
        }
        else
        {
            return  0;
        }
    }
}

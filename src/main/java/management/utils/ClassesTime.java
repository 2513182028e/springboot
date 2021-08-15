package management.utils;


public class ClassesTime {

    public static boolean Com(String time1,String time2)
    {
        int count1=(time1.length()-3)/2;
        int count2=(time2.length()-3)/2;
        String time1_head=time1.substring(0,2);
        String time2_head=time2.substring(0,2);
        int time1_i=time1.charAt(2);
        int time2_i=time2.charAt(2);
        boolean flag=Inter(time1_head,time2_head)&&(time1_i+time2_i!=1);
        if(!flag)
        {
            return  false;
        }
        for(int i=1;i<=count1;i++)
        {
            String q=time1.substring(2*i,2*i+2);
            if(time2.contains(q))
            {
                return true;
            }
        }
        return  false;
    }
    public static boolean Inter(String time1,String time2)
    {
        if(time1.charAt(1)<time2.charAt(0))
        {
            return  false;
        }
        else if(time1.charAt(1)==time2.charAt(0))
        {
            return  true;
        }
        else if(time2.charAt(1)<time1.charAt(0))
        {
            return false;
        }
        else
        {
            return  true;
        }
    }
}

package management.utils;


public class ClassesTime {

    public static boolean Com(String time1,String time2)
    {
        int count1=(time1.length()-3)/2;                          //前三位代表开始周，结束周，单双周 ，因此需要减去，从第四为开始，每两位代表课的上课时间  除以二代表几组
        int count2=(time2.length()-3)/2;
        String time1_head=time1.substring(0,2);
        String time2_head=time2.substring(0,2);                                   
        int time1_i=time1.charAt(2);
        int time2_i=time2.charAt(2);
        boolean flag=Inter(time1_head,time2_head)&&(time1_i+time2_i!=1);
        if(!flag)
        {
            return  false;       //两个课不存在时间冲突（两节课在同一周同时上课的情况），故返还false
        }
        for(int i=1;i<=count1;i++)    //（存在该情况）
        {
            String q=time1.substring(2*i,2*i+2);    //判断从第四位开始，在存在时间冲突的情况下，具体上课时间有没有重复  
            if(time2.contains(q))
            { 
                 return true;
            }
        }
        return  false;
    }
    public static boolean Inter(String time1,String time2)     //从前三位代表的周中，判断两个周是否存在交叉冲突的情况
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

package management.utils;

import java.util.Random;

public  class GetSalt {

    static char [] s="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+".toCharArray();
     public static  String  getSalt(int n)
    {
        StringBuilder ss=new StringBuilder();
        for(int i=0;i<n;i++)
        {
            char dq=s[new Random().nextInt(s.length)];
            ss.append(dq);
        }
        return  ss.toString();
    }
}

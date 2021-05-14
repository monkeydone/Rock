package com.bn.pd.javassist;

import android.util.Log;

public class PersonService {

    public static final String TAG = "PersonService";
    int test1 = 0;
    public String testStr1 = "testStr";
    static int test2 = 1;
    static String testStr2 = "staticStr2";
    public void getPerson(){
        System.out.println("get Person");
    }

    public void personFly(){
        System.out.println("oh my god,I can fly");
    }

    public static void testStaticMethod(int paramInt,String paramStr) {
        Log.e(TAG,"testStaticMethod,paramInt="+paramInt+",paramStr:"+paramStr);
    }

    public static String testStaticMethodToStr(int paramInt,String paramStr) {
        Log.e(TAG,"testStaticMethod,paramInt="+paramInt+",paramStr:"+paramStr);
        return paramStr+paramInt;
    }

    public void testMethod(int paramInt,String paramStr) {
        Log.e(TAG,"testStaticMethod,paramInt="+paramInt+",paramStr:"+paramStr);
    }

    public String toastText() {
        return "This is toast text";
    }


}

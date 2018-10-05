package com.example.linyansen.coursedatabase.LogoThread;


/**
 * Created by Lin Yansen on 12/2/2017.
 */

public class LogoThread implements Runnable
{

    @Override
    public void run()
    {
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
            }
        }
        catch (Exception e){}
    }
}

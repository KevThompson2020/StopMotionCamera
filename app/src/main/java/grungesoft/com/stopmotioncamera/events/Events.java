package grungesoft.com.stopmotioncamera.events;


import android.app.Activity;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashSet;

public class Events
{
    public static Bus eventBus = new Bus(ThreadEnforcer.ANY);
    private static HashSet<Thread> eventThreads = new HashSet<Thread>();

    /**
     *
     * @param event
     */
    public static void postToEventBusOnBackgroundThread(Object event)
    {
        Thread bgThread = new Thread(makeEventBusRunnable(event));
        eventThreads.add(bgThread); // place in the eventThreads set so it doesn't get garbage collected
        bgThread.start();
    }

    /**
     *
     * @param activity
     * @param event
     */
    public static void postToEventBusOnUIThread(Activity activity, final Object event)
    {
        try
        {
            activity.runOnUiThread(makeEventBusRunnable(event));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    /**
     *
     * @param event
     * @return
     */
    public static Runnable makeEventBusRunnable(final Object event)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    eventBus.post(event);
                    // then remove this thread from the eventThreads set so it (plus the runnable & event) can be garbage collected
                    eventThreads.remove(Thread.currentThread());
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        };
    }

    /**
     *
     * @param event
     */
    public static void postToEventBusOnCurrentThread(final Object event)
    {
        try
        {
            eventBus.post(event);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

}

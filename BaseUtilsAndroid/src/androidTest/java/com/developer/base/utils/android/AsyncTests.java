package com.developer.base.utils.android;

import android.content.Context;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.developer.base.utils.android.tools.async.BasePromise;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("StatementWithEmptyBody")
@RunWith(AndroidJUnit4.class)
public class AsyncTests {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.developer.base.utils.android.test", appContext.getPackageName());
    }

    @Test
    public void Promisse() {
        String input = "AAAJJJJAJAJJJAJAJAJJA";
        final boolean[] wait = {true};
        BasePromise<String> p = new BasePromise<>(s -> {
            assertEquals(s, input);
            return s.replace("A", "");
        });

        p.then(o -> {
            assertTrue(o instanceof String);
            String s = (String) o;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(s, input.replace("A", ""));
            return null;
        });

        p.then(o -> {
            assertNull(o);
            return 1;
        });

        p.Input = input;

        p.resolve(o -> {
            assertTrue(o instanceof Integer);
            assertEquals(Thread.currentThread(), Looper.getMainLooper().getThread());

            wait[0] = false;
        });

        while (wait[0]);
    }
}

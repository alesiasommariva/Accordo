package com.project.accordo;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.project.accordo.Service.RequestController;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = ExampleInstrumentedTest.class.getCanonicalName();

    @Test
    public void testRegister(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RequestController requestController = new RequestController(appContext);
        CountDownLatch lock = new CountDownLatch(1);
        requestController.register(
                response -> {
                    Log.d(TAG, "Response OK "+response.toString());
                    lock.countDown();
                },
                error -> {
                    Log.d(TAG, "Response KO "+error.toString());
                    lock.countDown();
                    fail("Reister should always be ok");
                });
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.project.accordo", appContext.getPackageName());
    }
}
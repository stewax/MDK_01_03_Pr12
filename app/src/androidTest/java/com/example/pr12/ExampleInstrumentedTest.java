package com.example.pr12;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import com.example.network.datas.users.UserCreate;
import com.example.network.datas.users.UserGet;
import com.example.network.datas.users.UserLogin;
import com.example.network.datas.users.UserLogout;
import com.example.network.datas.users.UserSend;
import com.example.network.datas.users.UserUpdate;
import com.example.network.domains.callbacks.MyResponseCallbacks;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.User;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static String CURRENT_TOKEN = null;

    @Test
    public void A_UserLogin() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);
        User User = new User("testing@mail.ru", "Asdfg123*");

        new UserLogin(User, new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER LOGIN", result);
                try {
                    if (result.contains("\"token\"")) {
                        String[] parts = result.split("\"token\":\"");
                        if (parts.length > 1) {
                            CURRENT_TOKEN = parts[1].split("\"")[0];
                            Log.d("TOKEN_SAVED", "Token saved: " + CURRENT_TOKEN);
                        }
                    }
                } catch (Exception e) {
                    Log.e("TOKEN_PARSE", "Error parsing token", e);
                }

                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER LOGIN", error);
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
        assertNotNull("Token should not be null after login", CURRENT_TOKEN);
    }

    @Test
    public void B_UserCreate() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);
        User User = new User("test_unique_" + System.currentTimeMillis() + "@mail.ru", "Asdfg123*", "Тестовый", "Пользователь", "Системы", 0);

        new UserCreate(User, new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER CREATE", result);
                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER CREATE", error);
                if (error.contains("already exists")) {
                    Success[0] = true;
                } else {
                    Success[0] = false;
                }
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
    }

    @Test
    public void C_UserUpdate() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);

        if (CURRENT_TOKEN == null) {
            fail("No token available. Did Login test pass?");
        }

        User User = new User("testing@mail.ru", "Asdfg123*", "Обновленный", "Юзер", "Теста", 0);

        new UserUpdate(User, CURRENT_TOKEN, new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER UPDATE", result);
                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER UPDATE", error);
                Success[0] = false;
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
    }

    @Test
    public void D_UserGet() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);

        if (CURRENT_TOKEN == null) {
            fail("No token available.");
        }

        new UserGet(CURRENT_TOKEN, new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER GET", result);
                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER GET", error);
                Success[0] = false;
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
    }

    @Test
    public void E_UserLogout() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);

        if (CURRENT_TOKEN == null) {
            fail("No token available.");
        }

        // Передаем СОХРАНЕННЫЙ токен
        new UserLogout(CURRENT_TOKEN, new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER LOGOUT", result);
                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER LOGOUT", error);
                Success[0] = false;
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
    }

    @Test
    public void F_UserSend() throws InterruptedException {
        final Boolean[] Success = {false};
        CountDownLatch Latch = new CountDownLatch(1);

        new UserSend("testing@mail.ru", new MyResponseCallbacks() {
            @Override
            public void onCompile(String result) {
                Log.d("USER SEND", result);
                Success[0] = true;
                Latch.countDown();
            }

            @Override
            public void onError(String error) {
                Log.e("USER SEND", error);
                Success[0] = false;
                Latch.countDown();
            }
        }).execute();

        boolean Completed = Latch.await(60, TimeUnit.SECONDS);
        assertTrue(Success[0]);
    }
}
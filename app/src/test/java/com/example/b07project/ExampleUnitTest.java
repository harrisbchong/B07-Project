package com.example.b07project;
import java.util.function.Consumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

import java.util.function.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    StudentLoginPage view;

    @Mock
    AdminLoginPage adview;

    @Mock
    Model model;

    @Captor
    ArgumentCaptor<Consumer<Student>> captor;
    // ArgumentCaptor<Consumer<User>> captor;

    @Captor
    ArgumentCaptor<Consumer<Admin>> adcaptor;
    // ArgumentCaptor<Consumer<User>> captor;

    /**
     * Tests Successful student login when received student
     */
    @Test
    public void testStudentLoginSuccessful() {
        String email = "t@mail.com";
        String password = "123456";
        Student s = new Student();

        SPresenter presenter = new SPresenter(model, view);
        presenter.stulogin(email, password);

        verify(model).stuauthenticate(eq(email), eq(password), captor.capture());
        Consumer<Student> callback = captor.getValue();
        callback.accept(s);


        verify(view, times(1)).redirectToStudentFrontPage(any());
    }

    /**
     * Tests if Student login fails when received null
     */
    @Test
    public void testStudentLoginFailed() {
        String email = "t@mail.com";
        String password = "123456";

        SPresenter presenter = new SPresenter(model, view);
        presenter.stulogin(email, password);


        verify(model).stuauthenticate(eq(email), eq(password), captor.capture());
        Consumer<Student> callback = captor.getValue();
        callback.accept(null);

        verify(view, times(1)).failedToLogin();
    }

    /**
     * Tests Student login fail with empty strings
     */
    @Test
    public void testStudentEmptyLogin() {
        String email = "";
        String password = "";

        SPresenter presenter = new SPresenter(model, view);
        presenter.stulogin(email, password);


        verify(model).stuauthenticate(eq(email), eq(password), captor.capture());
        Consumer<Student> callback = captor.getValue();
        callback.accept(null);

        verify(view, times(1)).failedToLogin();
    }

    /**
     * Tests Successful admin login when received admin
     */
    @Test
    public void testAdminLoginSuccessful() {
        String email = "a@mail.com";
        String password = "123456";
        Admin a = new Admin();

        APresenter presenter = new APresenter(model, adview);
        presenter.alogin(email, password);

        verify(model).adauthenticate(eq(email), eq(password), adcaptor.capture());
        Consumer<Admin> callback = adcaptor.getValue();
        callback.accept(a);


        verify(adview, times(1)).redirectToAdminFrontPage(any());
    }

    /**
     * Tests admin login failed when received null
     */
    @Test
    public void testAdminLoginFailed() {
        String email = "a@mail.com";
        String password = "123456";

        APresenter presenter = new APresenter(model, adview);
        presenter.alogin(email, password);


        verify(model).adauthenticate(eq(email), eq(password), adcaptor.capture());
        Consumer<Admin> callback = adcaptor.getValue();
        callback.accept(null);

        verify(adview, times(1)).failedToLogin();
    }

    /**
     * Tests admin login fail with empty strings
     */
    @Test
    public void testAdminEmptyLogin() {
        String email = "";
        String password = "";

        APresenter presenter = new APresenter(model, adview);
        presenter.alogin(email, password);


        verify(model).adauthenticate(eq(email), eq(password), adcaptor.capture());
        Consumer<Admin> callback = adcaptor.getValue();
        callback.accept(null);

        verify(adview, times(1)).failedToLogin();
    }
}
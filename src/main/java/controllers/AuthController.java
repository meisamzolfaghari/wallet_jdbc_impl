package controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    @Authenticated
    public void m1 () {
        System.out.println("hello");
    }

}

package controllers.auth;

import entities.User;
import entities.dto.UserDetails;
import services.UserService;
import services.exception.UserServiceException;
import services.exception.notValidUserToCreateException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (username == null || password == null) {
            resp.sendError(403, "username or password must be provided!");
            return;
        }

        String passwordHash = Base64.getEncoder().encodeToString(password.getBytes());

        try {

            UserDetails userDetails = userService.createSignUp(new User(username, passwordHash, email));

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.write("<html>" +
                    "<body>" +
                    "<h2>Registered Successfully...</h2>" +
                    "<p> Username: " + userDetails.getUserName() + "</p>" +
                    "<p> Email: " + userDetails.getEmail() + "</p>" +
                    "</body>" +
                    "</html>");

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (notValidUserToCreateException e) {
            resp.sendError(400, e.getMessage());
        }
    }

}

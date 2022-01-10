package controllers.user;

import entities.dto.UserDetails;
import services.UserService;
import services.exception.UserNotFoundException;
import services.exception.UserServiceException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateUserProfileServlet", urlPatterns = "/user/update-profile")
public class UpdateUserProfileServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");

        try {

            UserDetails userDetails = userService.updateUserProfile(new UserDetails(username, email));

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.write("<html>" +
                    "<body>" +
                    "<h2>Updated User Profile Successfully...</h2>" +
                    "<p> Username: " + userDetails.getUserName() + "</p>" +
                    "<p> Email: " + userDetails.getEmail() + "</p>" +
                    "</body>" +
                    "</html>");

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (UserNotFoundException e) {
            resp.sendError(401, e.getMessage());
        }

    }
}

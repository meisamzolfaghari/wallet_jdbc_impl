package controllers.user;

import controllers.AuthUserUtils;
import entities.User;
import entities.dto.UserDetails;
import services.UserService;
import services.exception.UserNotFoundException;
import services.exception.UserServiceException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "UserProfileServlet", urlPatterns = "/user/update-profile")
public class UserProfileServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();
        String email = req.getParameter("email");

        try {

            UserDetails userDetails = userService.updateUserProfile(new UserDetails(currentUser.getUsername(), email));

            showUserProfileHtml(resp, userDetails);

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (UserNotFoundException e) {
            resp.sendError(406, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();

        try {
            UserDetails userDetails = userService.getUserProfileByUsername(currentUser.getUsername());

            showUserProfileHtml(resp, userDetails);
        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (UserNotFoundException e) {
            resp.sendError(403, e.getMessage());
        }
    }

    private void showUserProfileHtml(HttpServletResponse resp, UserDetails userDetails) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        writer.write("<html>" +
                "<body>" +
                "<h2>Updated User Profile Successfully...</h2>" +
                "<p> Username: " + userDetails.getUserName() + "</p>" +
                "<p> Email: " + userDetails.getEmail() + "</p>" +
                "</body>" +
                "</html>");
    }
}

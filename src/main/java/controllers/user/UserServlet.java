package controllers.user;

import com.google.gson.Gson;
import controllers.AuthUserUtils;
import controllers.user.dto.UpdateUserProfileDTO;
import controllers.user.dto.UserProfileDTO;
import entities.User;
import services.UserService;
import services.dto.UserDetails;
import services.exception.EntityNotFoundException;
import services.exception.UserServiceException;
import services.impl.UserEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();
        UpdateUserProfileDTO updateUserProfileDTO =
                new Gson().fromJson(req.getReader(), UpdateUserProfileDTO.class);

        try {

            UserDetails userDetails = userService.updateUserProfile(
                    new UserDetails(currentUser.getUsername(), updateUserProfileDTO.getEmail()));

            setUserProfileDTOResponse(resp, userDetails);

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (EntityNotFoundException e) {
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

            setUserProfileDTOResponse(resp, userDetails);

        } catch (UserServiceException e) {
            resp.sendError(504, e.getMessage());
        } catch (EntityNotFoundException e) {
            resp.sendError(403, e.getMessage());
        }
    }

    private void setUserProfileDTOResponse(HttpServletResponse resp, UserDetails userDetails) throws IOException {
        UserProfileDTO userProfileDTO = new UserProfileDTO(userDetails.getUserName(), userDetails.getEmail());

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        writer.write(new Gson().toJson(userProfileDTO));
        writer.flush();
    }
}

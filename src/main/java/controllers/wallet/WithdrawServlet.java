package controllers.wallet;

import controllers.AuthUserUtils;
import entities.User;
import services.WalletService;
import services.dto.MoneyDepositWithdrawDetails;
import services.exception.UserNotFoundException;
import services.exception.WalletServiceException;
import services.impl.WalletEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "WithdrawServlet", urlPatterns = "/wallet/withdraw")
public class WithdrawServlet extends HttpServlet {

    private final WalletService walletService = WalletEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();

        Integer amount = Integer.valueOf(req.getParameter("amount"));

        try {

            String transactionId =
                    walletService.withdraw(new MoneyDepositWithdrawDetails(currentUser.getUsername(), amount));

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.write("<html>" +
                    "<body>" +
                    "<h2>Withdraw Transaction Created...</h2>" +
                    "<p> Transaction Id: " + transactionId + "</p>" +
                    "</body>" +
                    "</html>");

        } catch (UserNotFoundException e) {
            resp.sendError(406, e.getMessage());
        } catch (WalletServiceException e) {
            resp.sendError(504, e.getMessage());
        }
    }

}

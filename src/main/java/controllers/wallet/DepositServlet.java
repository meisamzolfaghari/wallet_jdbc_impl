package controllers.wallet;

import controllers.AuthUserUtils;
import entities.User;
import services.WalletService;
import services.dto.MoneyDepositWithdrawDetails;
import services.exception.EntityNotFoundException;
import services.exception.WalletServiceException;
import services.impl.WalletEntityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "DepositServlet", urlPatterns = "/wallet/deposit")
public class DepositServlet extends HttpServlet {

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
                    walletService.deposit(new MoneyDepositWithdrawDetails(currentUser.getUsername(), amount));

            PrintWriter writer = resp.getWriter();
            writer.write(" Deposit Transaction Created... \n" +
                    " Transaction Id: " + transactionId);
            writer.flush();

        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        } catch (WalletServiceException e) {
            resp.sendError(504, e.getMessage());
        }
    }

}

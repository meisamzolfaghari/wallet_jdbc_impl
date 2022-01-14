package controllers.wallet;

import com.google.gson.Gson;
import controllers.AuthUserUtils;
import controllers.wallet.dto.WithdrawRequestDTO;
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

        WithdrawRequestDTO withdrawRequestDTO =
                new Gson().fromJson(req.getReader(), WithdrawRequestDTO.class);

        try {

            String transactionId = walletService.withdraw(
                    new MoneyDepositWithdrawDetails(currentUser.getUsername(), withdrawRequestDTO.getAmount()));

            PrintWriter writer = resp.getWriter();
            writer.write(" Withdraw Transaction Created... \n" +
                    " Transaction Id: " + transactionId);
            writer.flush();

        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        } catch (WalletServiceException e) {
            resp.sendError(504, e.getMessage());
        }
    }

}

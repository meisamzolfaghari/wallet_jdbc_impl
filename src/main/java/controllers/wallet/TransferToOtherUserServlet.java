package controllers.wallet;

import com.google.gson.Gson;
import controllers.AuthUserUtils;
import controllers.wallet.dto.DepositRequestDTO;
import controllers.wallet.dto.TransferToOtherUserRequestDTO;
import entities.User;
import services.WalletService;
import services.dto.MoneyTransferDetails;
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

@WebServlet(name = "TransferToOtherUserServlet", urlPatterns = "/wallet/transfer-to-user")
public class TransferToOtherUserServlet extends HttpServlet {

    private final WalletService walletService = WalletEntityServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Optional<User> currentUserOptional = AuthUserUtils.getCurrentUser(req);
        if (currentUserOptional.isEmpty()) {
            resp.sendError(401);
            return;
        }

        User currentUser = currentUserOptional.get();

        TransferToOtherUserRequestDTO transferToOtherUserRequestDTO =
                new Gson().fromJson(req.getReader(), TransferToOtherUserRequestDTO.class);

        try {

            String transactionId = walletService.moneyTransferToOtherUser(
                    new MoneyTransferDetails(currentUser.getUsername(),
                            transferToOtherUserRequestDTO.getReceiverUsername(),
                            transferToOtherUserRequestDTO.getAmount()));

            PrintWriter writer = resp.getWriter();
            writer.write(" Transfer Transaction Created... \n" +
                    " Transaction Id: " + transactionId);
            writer.flush();

        } catch (EntityNotFoundException e) {
            resp.sendError(406, e.getMessage());
        } catch (WalletServiceException e) {
            resp.sendError(504, e.getMessage());
        }
    }

}

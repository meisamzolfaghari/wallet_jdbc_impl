package services.impl;

import entities.User;
import entities.Wallet;
import entities.dto.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.UserRepository;
import repos.WalletRepository;
import services.UserService;
import services.exception.UserServiceException;

import java.sql.SQLException;

public class UserServiceImpl extends BaseServiceImpl<Integer, User, UserRepository> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final class UserServiceImplHolder {
        private static final UserServiceImpl USER_SERVICE_IMPL = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceImplHolder.USER_SERVICE_IMPL;
    }

    @Override
    public UserDetails createSignUp(User user) throws UserServiceException {

        try {
            getRepository().create(user);

            Wallet wallet = new Wallet(user.getId());
            WalletRepository.getInstance().create(wallet);

            user.setWalletId(wallet.getId());
            getRepository().update(user);

            getRepository().getConnection().commit();
            return new UserDetails(user);

        } catch (SQLException e) {
            final String message = "error on creating user! ";
            LOGGER.error(message, e);
            throw new UserServiceException(message);
        }
    }

    @Override
    public UserDetails getUserProfileByUsername(String userName) throws UserServiceException {
        try {

            User user = getRepository().findByUsername(userName)
                    .orElseThrow(() -> new UserServiceException("user not found for username: " + userName));

            return new UserDetails(user);

        } catch (SQLException e) {
            final String message = String.format("error on getting user for username: %s ", userName);
            LOGGER.error(message, e);
            throw new UserServiceException(message);
        }
    }

    @Override
    public User getUserDbEntityByUserName(String username) throws UserServiceException {
        try {

            return getByUsername(username);

        } catch (SQLException e) {
            final String message = String.format("error on getting user for username: %s ", username);
            LOGGER.error(message, e);
            throw new UserServiceException(message);
        }
    }

    private User getByUsername(String userName) throws SQLException {
        return getRepository().findByUsername(userName).orElseThrow();
    }

    @Override
    public UserDetails updateUserProfile(UserDetails userProfile) throws UserServiceException {
        try {

            User user = getByUsername(userProfile.getUserName());
            user.setEmail(userProfile.getEmail());

            getRepository().update(user);

            getRepository().getConnection().commit();

            return new UserDetails(user);

        } catch (SQLException e) {
            final String message = "error on updating user profile! ";
            LOGGER.error(message, e);
            throw new UserServiceException(message);
        }
    }

    @Override
    protected UserRepository getRepository() {
        return UserRepository.getInstance();
    }
}

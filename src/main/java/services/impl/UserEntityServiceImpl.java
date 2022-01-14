package services.impl;

import config.ConnectionFactory;
import entities.User;
import entities.Wallet;
import services.dto.UserDetails;
import repos.UserRepository;
import repos.WalletRepository;
import services.UserService;
import services.exception.EntityNotFoundException;
import services.exception.UserServiceException;
import services.exception.notValidUserToCreateException;

import java.sql.Connection;
import java.sql.SQLException;

public class UserEntityServiceImpl extends BaseEntityServiceImpl<Long, User, UserRepository> implements UserService {

    private UserEntityServiceImpl() {
    }

    private static final class UserServiceImplHolder {
        private static final UserEntityServiceImpl USER_SERVICE_IMPL = new UserEntityServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceImplHolder.USER_SERVICE_IMPL;
    }

    @Override
    public UserDetails createSignUp(User user) throws UserServiceException, notValidUserToCreateException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            if (user.getUsername() == null || user.getPasswordHash() == null)
                throw new notValidUserToCreateException("username or password not provided!");

            Wallet wallet = new Wallet();
            WalletRepository.getInstance().create(wallet, connection);

            user.setWalletId(wallet.getId());
            getRepository().create(user, connection);

            connection.commit();
            return new UserDetails(user);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException("error on creating user! ");
        }
    }

    @Override
    public UserDetails getUserProfileByUsername(String userName) throws UserServiceException, EntityNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {

            User user = getRepository().findByUsername(userName, connection)
                    .orElseThrow(() -> new EntityNotFoundException("user not found for username: " + userName));

            return new UserDetails(user);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException(String.format("error on getting user for username: %s ", userName));
        }
    }

    @Override
    public User getUserDbEntityByUserName(String username) throws UserServiceException, EntityNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {

            return getByUsername(username, connection);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException(String.format("error on getting user for username: %s ", username));
        }
    }

    private User getByUsername(String userName, Connection connection) throws SQLException, EntityNotFoundException {
        return getRepository().findByUsername(userName, connection)
                .orElseThrow(() -> new EntityNotFoundException("user not found for username: " + userName));
    }

    @Override
    public UserDetails updateUserProfile(UserDetails userProfile) throws UserServiceException, EntityNotFoundException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            User user = getByUsername(userProfile.getUserName(), connection);

            user.setEmail(userProfile.getEmail());

            getRepository().update(user, connection);

            connection.commit();

            return new UserDetails(user);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException("error on updating user profile! ");
        }
    }

    @Override
    public User getUserByWalletId(Long walletId) throws UserServiceException, EntityNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            return getRepository().getByWalletId(walletId, connection)
                    .orElseThrow(() -> new EntityNotFoundException("user not found by walletId: " + walletId));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserServiceException("error on getting user by walletId: " + walletId);
        }
    }

    @Override
    protected UserRepository getRepository() {
        return UserRepository.getInstance();
    }
}

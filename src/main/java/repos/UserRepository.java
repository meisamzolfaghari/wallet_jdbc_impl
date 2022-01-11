package repos;

import entities.BaseEntity;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<User, Long> {

    private UserRepository() {
    }

    private static final class UserRepositoryHolder {

        private static final UserRepository USER_REPOSITORY = new UserRepository();
    }

    public static UserRepository getInstance() {
        return UserRepositoryHolder.USER_REPOSITORY;
    }

    @Override
    public void create(User user, Connection connection) throws SQLException {

        String sql = String.format("insert into %s (%s, %s, %s, %s) values (?, ?, ?, ?)",
                getTableName(), User.USERNAME_SQL, User.PASSWORD_HASH_SQL, User.EMAIL_SQL, User.WALLET_ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setObject(3, user.getEmail(), Types.VARCHAR);
            statement.setLong(4, user.getWalletId());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                user.setId(generatedKeys.getLong(1));
            else throw new SQLException("Creating entity failed, no ID obtained!");

        }
    }

    @Override
    public Optional<User> findById(Long id, Connection connection) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getLong(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getLong(User.WALLET_ID_SQL));

                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<User> findByUsername(String username, Connection connection) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), User.USERNAME_SQL);
        Optional<User> userOptional = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getLong(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getLong(User.WALLET_ID_SQL));

                userOptional = Optional.of(user);
            }
        }

        return userOptional;
    }

    public Optional<User> getByWalletId(Long walletId, Connection connection) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), User.WALLET_ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, walletId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getLong(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getLong(User.WALLET_ID_SQL));

                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(User user, Connection connection) throws SQLException {

        String sql = String.format("update table %s set %s = ?, %s = ?, %s = ?, %s = ? where %s = ?;",
                getTableName(), User.USERNAME_SQL, User.PASSWORD_HASH_SQL,
                User.EMAIL_SQL, User.WALLET_ID_SQL, BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setLong(4, user.getWalletId());
            statement.setLong(5, user.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        String sql = String.format("select * from %s;", getTableName());
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getLong(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getLong(User.WALLET_ID_SQL));

                users.add(user);
            }
        }

        return users;
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME_SQL;
    }
}

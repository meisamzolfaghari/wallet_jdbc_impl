package repos;

import entities.BaseEntity;
import entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<User, Integer> {

    private UserRepository() {
    }

    private static final class UserRepositoryHolder {
        private static final UserRepository USER_REPOSITORY = new UserRepository();
    }

    public static UserRepository getInstance() {
        return UserRepositoryHolder.USER_REPOSITORY;
    }

    @Override
    public void create(User user) throws SQLException {

        String sql = String.format("insert into %s (%s, %s, %s) values (?, ?, ?)",
                getTableName(), User.USERNAME_SQL, User.PASSWORD_HASH_SQL, User.EMAIL_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());

            statement.execute();
            setCreatedId(user, statement);
        } finally {
            connection.close();
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getInt(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getInt(User.WALLET_ID_SQL));

                return Optional.of(user);
            }
        } finally {
            connection.close();
        }

        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), User.USERNAME_SQL);
        Optional<User> userOptional = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getInt(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getInt(User.WALLET_ID_SQL));

                userOptional = Optional.of(user);
            }
        } finally {
            connection.close();
        }

        return userOptional;
    }

    @Override
    public void update(User user) throws SQLException {

        String sql = String.format("update table %s set %s = ?, %s = ?, %s = ?, %s = ? where %s = ?;",
                getTableName(), User.USERNAME_SQL, User.PASSWORD_HASH_SQL,
                User.EMAIL_SQL, User.WALLET_ID_SQL, BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getWalletId());
            statement.setInt(5, user.getId());

            statement.execute();
        } finally {
            connection.close();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = String.format("select * from %s;", getTableName());
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getInt(BaseEntity.ID_SQL));
                user.setUsername(resultSet.getString(User.USERNAME_SQL));
                user.setPasswordHash(resultSet.getString(User.PASSWORD_HASH_SQL));
                user.setWalletId(resultSet.getInt(User.WALLET_ID_SQL));

                users.add(user);
            }
        } finally {
            connection.close();
        }

        return users;
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME_SQL;
    }
}

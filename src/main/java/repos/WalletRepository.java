package repos;

import entities.BaseEntity;
import entities.User;
import entities.Wallet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WalletRepository extends BaseRepository<Wallet, Integer> {

    private static final class WalletRepositoryHolder {
        private static final WalletRepository WALLET_REPOSITORY = new WalletRepository();
    }

    public static WalletRepository getInstance() {
        return WalletRepositoryHolder.WALLET_REPOSITORY;
    }

    @Override
    public void create(Wallet wallet) throws SQLException {

        String sql = String.format("insert into %s (%s, %s) values (?, ?)",
                getTableName(), Wallet.BALANCE_SQL, Wallet.USER_ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, wallet.getBalance());
            statement.setInt(2, wallet.getUserId());

            statement.execute();
            setCreatedId(wallet, statement);
        } finally {
            connection.close();
        }
    }

    @Override
    public Optional<Wallet> findById(Integer id) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                Wallet wallet = new Wallet(resultSet.getInt(Wallet.USER_ID_SQL));
                wallet.setBalance(resultSet.getInt(Wallet.BALANCE_SQL));

                return Optional.of(wallet);
            }
        } finally {
            connection.close();
        }

        return Optional.empty();
    }

    @Override
    public void update(Wallet wallet) throws SQLException {

        String sql = String.format("update table %s set %s = ? where %s = ?;",
                getTableName(), Wallet.BALANCE_SQL, BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, wallet.getBalance());
            statement.setInt(2, wallet.getId());

            statement.execute();
        } finally {
            connection.close();
        }
    }

    @Override
    public List<Wallet> findAll() throws SQLException {
        String sql = String.format("select * from %s;", getTableName());
        List<Wallet> wallets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Wallet wallet = new Wallet(resultSet.getInt(Wallet.USER_ID_SQL));
                wallet.setBalance(resultSet.getInt(Wallet.BALANCE_SQL));

                wallets.add(wallet);
            }
        } finally {
            connection.close();
        }

        return wallets;
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME_SQL;
    }
}

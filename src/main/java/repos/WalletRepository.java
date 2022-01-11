package repos;

import entities.BaseEntity;
import entities.User;
import entities.Wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WalletRepository extends BaseRepository<Wallet, Long> {

    private WalletRepository() {
    }

    private static final class WalletRepositoryHolder {
        private static final WalletRepository WALLET_REPOSITORY = new WalletRepository();
    }

    public static WalletRepository getInstance() {
        return WalletRepositoryHolder.WALLET_REPOSITORY;
    }

    @Override
    public void create(Wallet wallet, Connection connection) throws SQLException {

        String sql = String.format("insert into %s (%s) values (?)",
                getTableName(), Wallet.BALANCE_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, wallet.getBalance());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                wallet.setId(generatedKeys.getLong(1));
            else throw new SQLException("Creating entity failed, no ID obtained!");
        }
    }

    @Override
    public Optional<Wallet> findById(Long id, Connection connection) throws SQLException {
        String sql = String.format("select * from %s where %s = ?;", getTableName(), BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                Wallet wallet = new Wallet();
                wallet.setId(resultSet.getLong(BaseEntity.ID_SQL));
                wallet.setBalance(resultSet.getInt(Wallet.BALANCE_SQL));

                return Optional.of(wallet);
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(Wallet wallet, Connection connection) throws SQLException {

        String sql = String.format("update table %s set %s = ? where %s = ?;",
                getTableName(), Wallet.BALANCE_SQL, BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, wallet.getBalance());
            statement.setLong(2, wallet.getId());

            statement.execute();
        }
    }

    @Override
    public List<Wallet> findAll(Connection connection) throws SQLException {
        String sql = String.format("select * from %s;", getTableName());
        List<Wallet> wallets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Wallet wallet = new Wallet();
                wallet.setId(resultSet.getLong(BaseEntity.ID_SQL));
                wallet.setBalance(resultSet.getInt(Wallet.BALANCE_SQL));

                wallets.add(wallet);
            }
        }

        return wallets;
    }

    @Override
    protected String getTableName() {
        return Wallet.TABLE_NAME_SQL;
    }
}

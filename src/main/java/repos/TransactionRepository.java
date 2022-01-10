package repos;

import entities.BaseEntity;
import entities.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionRepository extends BaseRepository<Transaction, String> {

    private TransactionRepository() {}

    private static final class TransactionRepositoryHolder {
        private static final TransactionRepository TRANSACTION_REPOSITORY = new TransactionRepository();
    }

    public static TransactionRepository getInstance() {
        return TransactionRepositoryHolder.TRANSACTION_REPOSITORY;
    }

    @Override
    public void create(Transaction transaction) throws SQLException {

        String sql = String.format("insert into %s (%s, %s, %s, %s, %s, %s) values (?, ?, ?, ?, ?, ?)",
                getTableName(), Transaction.TRANSACTION_DATE_SQL, Transaction.STATUS_SQL, Transaction.TYPE_SQL,
                Transaction.AMOUNT_SQL, Transaction.SENDER_WALLET_ID_SQL, Transaction.RECEIVER_WALLET_ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.from(transaction.getTransactionDate().toInstant()));
            statement.setString(2, transaction.getStatus());
            statement.setString(3, transaction.getType());
            statement.setInt(4, transaction.getAmount());
            statement.setInt(5, transaction.getSenderWalletId());
            statement.setInt(6, transaction.getReceiverWalletId());

            statement.execute();
            setCreatedId(transaction, statement);
        } finally {
            connection.close();
        }
    }

    @Override
    public Optional<Transaction> findById(String id) throws SQLException {

        String sql = String.format("select * from %s where %s = ?;", getTableName(), BaseEntity.ID_SQL);
        Optional<Transaction> transactionOptional = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                Transaction transaction = new Transaction(
                        resultSet.getString(BaseEntity.ID_SQL),
                        Date.from(resultSet.getTimestamp(Transaction.TRANSACTION_DATE_SQL).toInstant()),
                        resultSet.getString(Transaction.STATUS_SQL),
                        resultSet.getString(Transaction.TYPE_SQL),
                        resultSet.getInt(Transaction.AMOUNT_SQL),
                        resultSet.getInt(Transaction.SENDER_WALLET_ID_SQL),
                        resultSet.getInt(Transaction.RECEIVER_WALLET_ID_SQL)
                );

                transactionOptional = Optional.of(transaction);
            }

            return transactionOptional;

        } finally {
            connection.close();
        }
    }

    @Override
    public void update(Transaction transaction) throws SQLException {

        String sql = String.format("update table %s set %s = ?, %s = ?, %s = ?, %s = ? where %s = ?;",
                getTableName(), Transaction.TRANSACTION_DATE_SQL, Transaction.STATUS_SQL,
                Transaction.TYPE_SQL, Transaction.AMOUNT_SQL, BaseEntity.ID_SQL);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.from(transaction.getTransactionDate().toInstant()));
            statement.setString(2, transaction.getStatus());
            statement.setString(3, transaction.getType());
            statement.setInt(4, transaction.getAmount());
            statement.setString(5, transaction.getId());

            statement.execute();
        } finally {
            connection.close();
        }
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String sql = String.format("select * from %s;", getTableName());
        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Transaction transaction = new Transaction(
                        resultSet.getString(BaseEntity.ID_SQL),
                        Date.from(resultSet.getTimestamp(Transaction.TRANSACTION_DATE_SQL).toInstant()),
                        resultSet.getString(Transaction.STATUS_SQL),
                        resultSet.getString(Transaction.TYPE_SQL),
                        resultSet.getInt(Transaction.AMOUNT_SQL),
                        resultSet.getInt(Transaction.SENDER_WALLET_ID_SQL),
                        resultSet.getInt(Transaction.RECEIVER_WALLET_ID_SQL)
                );

                transactions.add(transaction);
            }
        } finally {
            connection.close();
        }

        return transactions;
    }

    @Override
    protected String getTableName() {
        return Transaction.TABLE_NAME_SQL;
    }
}

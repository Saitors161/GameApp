package org.example.repository.impl;

import org.example.model.Clan;
import org.example.repository.ClanRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseClanRepositoryImpl implements ClanRepository {
    public DatabaseClanRepositoryImpl() {

        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            Statement statement = connection.createStatement();
            String dropTableQuery = "drop TABLE clans " ;
            String clansTableQuery = "CREATE TABLE clans " +
                    "(id LONG auto_increment, name TEXT, gold INTEGER)";
            String clanEntryQuery = "INSERT INTO clans(name,gold) " +
                    "VALUES ('Brian', 33)";
            statement.executeUpdate(dropTableQuery);
            statement.executeUpdate(clansTableQuery);
            statement.executeUpdate(clanEntryQuery);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Clan save(Clan clan) {
        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            String query = "INSERT INTO clans (name,gold)" +
                    "VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, clan.getName());
            statement.setInt(2, clan.getGold());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()) {
                    clan.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clan;
    }

    private Connection getNewConnection() throws SQLException {
        String url = "jdbc:h2:~/test";
        return DriverManager.getConnection(url);
    }

    @Override
    public void deleteById(long id) {
        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            Statement statement = connection.createStatement();
            String query = "DELETE from clans where id =" +id;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Clan getById(long id) {
        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT * from clans WHERE id="+ id;
            List<Clan> clans = getClansFromResultSet(statement.executeQuery(query));
            if(!clans.isEmpty()){
                return clans.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Clan update(Clan clan) {
        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            String query = "UPDATE clans SET name = ?, gold = ? WHERE id = ?" ;
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, clan.getName());
            statement.setInt(2, clan.getGold());
            statement.setLong(3, clan.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clan;

    }

    private List<Clan> getClansFromResultSet(ResultSet resultSet) throws SQLException {
        List<Clan> clans = new ArrayList<>();
        while(resultSet.next()){
            clans.add(new Clan(resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)));
        }
        return clans;
    }

    @Override
    public Collection<Clan> getAll() {
        Connection conn = null;
        try (Connection connection = getNewConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT * from clans";
            return getClansFromResultSet(statement.executeQuery(query));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

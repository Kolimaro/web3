package dao;

import model.BankClient;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO() {
        connection = DBConnection.getConnection();
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> bankClientList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from bank_client");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            BankClient client = new BankClient();
            client.setId(rs.getLong(1));
            client.setName(rs.getString(2));
            client.setPassword(rs.getString(3));
            client.setMoney(rs.getLong(4));
            bankClientList.add(client);
        }
        rs.close();
        ps.close();
        return bankClientList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        boolean isValidate = false;
        List<BankClient> list = getAllBankClient();
        PreparedStatement ps = null;
        ResultSet rs = null;
        for (BankClient x : list) {
            if (x.getName().equals(name)) {
                ps = connection.prepareStatement("select * from bank_client where id = ?");
                ps.setString(1, String.valueOf(x.getId()));
                rs = ps.executeQuery();
                rs.next();
                if (rs.getString(3).equals(password)) {
                    rs.close();
                    ps.close();
                    isValidate = true;
                    break;
                }
            }
        }
        if (!isValidate) {
            rs.close();
            ps.close();
        }
        return isValidate;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("update bank_client set money = ? where name = ? and password = ?");
        ps.setString(1, transactValue.toString());
        ps.setString(2, name);
        ps.setString(3, password);
        ps.executeUpdate();
        connection.commit();
        ps.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from bank_client where id = ?");
        ps.setString(1, String.valueOf(id));
        ResultSet rs = ps.executeQuery();
        rs.next();
        BankClient bankClient = new BankClient();
        bankClient.setId(rs.getLong(1));
        bankClient.setName(rs.getString(2));
        bankClient.setPassword(rs.getString(3));
        bankClient.setMoney(rs.getLong(4));
        rs.close();
        ps.close();
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        return getClientByName(name).getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from bank_client where name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        rs.next();
        long id = rs.getLong(1);
        rs.close();
        ps.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("select * from bank_client where name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        rs.next();
        BankClient bankClient = new BankClient();
        bankClient.setId(rs.getLong(1));
        bankClient.setName(rs.getString(2));
        bankClient.setPassword(rs.getString(3));
        bankClient.setMoney(rs.getLong(4));
        rs.close();
        ps.close();
        return bankClient;
    }

    public boolean addClient(BankClient client) throws SQLException {
        boolean isAdded = false;
        createTable();
        if (!getAllBankClient().contains(client)) {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("insert into bank_client(name, password, money) VALUES (?, ?, ?)");
            ps.setString(1, client.getName());
            ps.setString(2, client.getPassword());
            ps.setLong(3, client.getMoney());
            ps.executeUpdate();
            connection.commit();
            ps.close();
            isAdded = true;
        }
        return isAdded;
    }

    public void createTable() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        ps.executeUpdate();
        ps.close();
    }

    public void dropTable() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("drop table if exists bank_client");
        ps.executeUpdate();
        ps.close();
    }

    public boolean deleteClient(String name) throws SQLException {
        boolean isDeleted = false;
        List<BankClient> list = getAllBankClient();
        for (BankClient x : list) {
            if (x.getName().equals(name)) {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement("delete from bank_client where name = ?");
                ps.setString(1, name);
                ps.executeUpdate();
                connection.commit();
                isDeleted = true;
                break;
            }
        }
        return isDeleted;
    }
}

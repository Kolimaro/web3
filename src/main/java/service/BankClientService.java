package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    private BankClientDAO dao;

    public BankClientService() {
        dao = new BankClientDAO();
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return dao.getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws DBException {
        try {
            return dao.getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() throws DBException {
        try {
            return dao.getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean deleteClient(String name) throws DBException {
        try {
            return dao.deleteClient(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            return dao.addClient(client);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        boolean isComplete = false;
        try {
            BankClient recipient = dao.getClientByName(name);
            if (dao.isClientHasSum(sender.getName(), value)
                    && dao.validateClient(sender.getName(), sender.getPassword())) {
                dao.updateClientsMoney(sender.getName(), sender.getPassword(), sender.getMoney() - value);
                dao.updateClientsMoney(recipient.getName(), recipient.getPassword(), recipient.getMoney() + value);
                isComplete = true;
            }
        } catch (SQLException e) {
            isComplete = false;
        }
        return isComplete;
    }

    public void cleanUp() throws DBException {
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}

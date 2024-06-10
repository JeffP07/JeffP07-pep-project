package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public Account insertAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet results = ps.getGeneratedKeys();
            if (results.next()) {
                return new Account(results.getInt(1), account.getUsername(), account.getPassword());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet results = ps.executeQuery();

            if (results.next()) {
                return new Account(results.getInt("account_id"), results.getString("username"), results.getString("password"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
}

package br.com.battlebits.ycommon.common.connection.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.connection.BattleConnection;

public class MySQLBackend extends BattleConnection {

	private Connection connection;
	public static ReentrantLock lock = new ReentrantLock(true);
	private String hostname;
	private int port;
	private String database;
	private String username;
	private String password;

	public MySQLBackend(String hostname, int port, String database, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void startConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		BattlebitsAPI.getLogger().info("Conectando ao MySQL");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
	}

	public void update(String sqlString) {
		
	}

	public void closeConnection() throws SQLException {
		if (isConnected())
			connection.close();
	}

	public void recallConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (isConnected())
			return;
		BattlebitsAPI.getLogger().info("Reconectando ao MySQL");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
	}

	public boolean isConnected() throws SQLException {
		if (connection == null)
			return false;
		if (connection.isClosed())
			return false;
		return true;
	}

	public Connection getConnection() {
		return connection;
	}

}

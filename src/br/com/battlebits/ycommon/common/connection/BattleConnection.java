package br.com.battlebits.ycommon.common.connection;

public abstract class BattleConnection {

	public abstract void startConnection();

	public abstract Object getData(String dataId);
	
	public abstract void closeConnection();
	
	public abstract void recallConnection();
	

}
package br.com.battlebits.ycommon.bungee.networking.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public abstract class CommonClient {
	public int written;
	public int read;

	private String serverIp = "";
	private final CommonConnectionOutput outputHandler;
	private final CommonConnectionInput inputHandler;
	private CommonHandler packetHandler;
	public final Socket socket;
	public int keepAlive;

	public CommonClient(Socket socket) throws Exception {
		this.socket = socket;
		keepAlive = 0;
		outputHandler = new CommonConnectionOutput(this, new DataOutputStream(socket.getOutputStream()));
		inputHandler = new CommonConnectionInput(this, new DataInputStream(socket.getInputStream()));
		inputHandler.start();
		outputHandler.start();
	}
	
	protected void setPacketHandler(CommonHandler packetHandler) {
		this.packetHandler = packetHandler;
	}
	
	public void sendPacket(CommonPacket packet) {
		outputHandler.send(packet);
	}

	public void disconnect() {
		if (!socket.isClosed()) {
			try {
				inputHandler.close();
				outputHandler.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inputHandler.stopThread();
		outputHandler.stopThread();
		System.out.println("Cliente desconectado");
	}
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	public void registerClient() {
		CommonServer.registerClient(this);
	}
	
	public String getServerIp() {
		return serverIp;
	}

	public CommonHandler getPacketHandler() {
		return packetHandler;
	}

	public CommonConnectionInput getInputHandler() {
		return inputHandler;
	}

	public CommonConnectionOutput getOutputHandler() {
		return outputHandler;
	}

}

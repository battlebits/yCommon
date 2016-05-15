package br.com.battlebits.ycommon.bukkit.api.chat;

public class ChatAPI {

	private ChatState chatState = ChatState.ENABLED;
	private static ChatAPI instance;

	{
		instance = new ChatAPI();
	}

	public static ChatAPI getInstance() {
		return instance;
	}

	public ChatState getChatState() {
		return chatState;
	}

	public void setChatState(ChatState chatState) {
		this.chatState = chatState;
	}

	public enum ChatState {
		ENABLED, STAFF, YOUTUBER, DISABLED;
	}
}

package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class CPacketTranslationsRequest extends CommonPacket {
	private Language language;

	public CPacketTranslationsRequest() {
	}

	public CPacketTranslationsRequest(Language language) {
		this.language = language;
	}
	
	public Language getLanguage() {
		return language;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.language = Language.valueOf(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(language.name());
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleTranslationsRequest(this);
	}

}

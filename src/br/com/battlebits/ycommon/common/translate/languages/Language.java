package br.com.battlebits.ycommon.common.translate.languages;

public enum Language {

	ENGLISH("English"), PORTUGUES("Portugu�s");

	private String name;

	private Language(String languageName) {
		this.name = languageName;
	}
	
	public String getName() {
		return name;
	}

}

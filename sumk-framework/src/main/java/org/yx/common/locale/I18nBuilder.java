package org.yx.common.locale;

public class I18nBuilder {
	public static I18nMessageProvider getProvider() {
		return I18n.getProvider();
	}

	static void setProvider(I18nMessageProvider provider) {
		I18n.setProvider(provider);
	}
}

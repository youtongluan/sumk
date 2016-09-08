package org.yx.http;

public enum EncryptType {
	NONE,BASE64,AES_BASE64,AES;
	
	public boolean isAes(){
		return this==AES_BASE64||this==AES;
	}
	
	public boolean isBase64(){
		return this==AES_BASE64||this==BASE64;
	}
}

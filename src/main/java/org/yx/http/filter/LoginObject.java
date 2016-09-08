package org.yx.http.filter;

public class LoginObject {

	/**
	 * 登陆失败的信息
	 */
	private String errorMsg;
	
	/**
	 * 其它数据，一般是json格式
	 */
	private String json;
	public String getJson() {
		return json;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	/**
	 * 登陆失败
	 * @param errorMsg 失败信息
	 * @return
	 */
	public static LoginObject error(String errorMsg){
		LoginObject obj=new LoginObject();
		obj.errorMsg=errorMsg;
		return obj;
	}
	
	/**
	 * 登陆成功，要返回给客户端的信息
	 * @param text
	 * @return
	 */
	public static LoginObject success(String text){
		LoginObject obj=new LoginObject();
		obj.json=text;
		return obj;
	} 
}

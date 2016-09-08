package org.yx.http.start;

import java.io.IOException;

import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;

public class HttpBeanListener extends AbstractBeanListener {

	public HttpBeanListener(String packs) {
		super(packs);
	}
	private HttpFactory factory=new HttpFactory();
	@Override
	public void listen(BeanEvent event) {
		try {
			factory.resolve(Class.forName(event.getClassName()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

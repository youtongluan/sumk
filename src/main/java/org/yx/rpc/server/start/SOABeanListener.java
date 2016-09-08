package org.yx.rpc.server.start;

import java.io.IOException;

import org.yx.bean.AbstractBeanListener;
import org.yx.bean.BeanEvent;

public class SOABeanListener extends AbstractBeanListener {

	public SOABeanListener(String packs) {
		super(packs);
	}
	private SoaFactory factory=new SoaFactory();
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

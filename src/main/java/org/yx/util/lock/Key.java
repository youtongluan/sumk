package org.yx.util.lock;

/**
 * 分布式锁的钥匙
 * 
 * @author 游夏
 *
 */
public interface Key {
	/**
	 * 钥匙的Id，肯定不为空
	 * 
	 * @return
	 */
	String getId();
}

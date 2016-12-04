package org.yx.listener;

import java.util.List;

/**
 * 实现类的order值，都要在10000以内
 * 
 * @author 游夏
 *
 * @param <T>
 */
public interface FactorysBean<T> {

	List<T> create() throws Exception;

}

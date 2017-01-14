/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.listener;

public interface ListenerGroup<T extends SumkEvent> {

	/**
	 * 如果这个组的listen和support的listen的类型一致，就返回true。否则返回false。
	 * 注意：即使返回true，也不代表就添加到group中，因为如果重复的话，也照样没有添加进来
	 * 
	 * @param listner
	 * @return
	 */
	boolean addListener(Listener<T> listener);

	Listener<T> removeListener(Listener<T> listener);

	/**
	 * 监听器的总数
	 * 
	 * @return
	 */
	int size();

	void listen(T event);

}
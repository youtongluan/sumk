/**
 * Copyright (C) 2016 - 2030 youtongluan.
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
package org.yx.bean.aop;

/**
 * <B>返回值如果是原始类型，因为不支持null，所以在before()返回false和onError()吃掉异常的情况下，框架会抛出异常</B><BR
 * />
 * 执行顺序：
 * <LI>先执行before()，如果返回false，就直接执行close()
 * <LI>如果before()和业务方法都执行成功，就执行after()
 * <LI>有异常发生就执行onError(),这个是用来转换异常，如果异常被前面的Executor转换了，将不再被执行。这个方法不建议抛出异常
 * <LI>close()无论什么情况都会被执行。这个方法不建议抛出异常
 * 
 * @author youtl
 *
 */
public interface AopExecutor {

	/**
	 * 这个方法可以抛出异常，如果抛出异常，就不会执行后面的AopExecutor，并且真正的业务方法也不会被执行你
	 * 
	 * @param params 原始方法的参数
	 */
	void before(Object[] params) throws Exception;

	/**
	 * 无论上面的before()方法执行得怎么样，所有调用过before()的AopExecutor， 它的close()都会确保被调用到
	 * 
	 * @param result         原始方法的返回值。返回值引用不可变，但可以修改返回值的内部属性
	 * @param e              原始或上一个AopExecutor的异常，如果没有发生异常，它就是null
	 * @param methodExecuted true表示业务方法有被执行
	 * @return 原始异常或者处理后的异常。如果返回null,就表示没有异常了
	 */
	Throwable after(Object result, Throwable e, boolean methodExecuted);

}

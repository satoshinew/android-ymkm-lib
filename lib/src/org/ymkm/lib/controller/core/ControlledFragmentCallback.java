/*******************************************************************************
 * Copyright 2013 Yoann Mikami <yoann@ymkm.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.ymkm.lib.controller.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.ymkm.lib.controller.ControlledFragment;
import org.ymkm.lib.controller.FragmentControllerApplication;

import android.os.Handler.Callback;
import android.os.Message;
import android.os.Messenger;

/**
 * TODO 
 * 
 * <p>
 * - Note : A constructor that takes the ControlledFragment as a parameter MUST be defined in the subclass<br>
 * - When defined as an inner class of another class, it will added automatically by the compiler,
 * unless the {@code static} keyword is used, which will cause the empty constructor to be created instead.<br>
 * </p>
 * <p>
 * All messages sent using any variation of {@link ControlledFragmentCallback#sendToController()} are
 * sent to the controller using {@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} as the message.
 * </p>
 */
public abstract class ControlledFragmentCallback implements ControllableFragmentCallback {

	/**
	 * Creates a new {@link ControlledFragmentCallback} instance, initialized with given parameters
	 * 
	 * @param clazz Class of the callback instance to create
	 * @param controlId the control ID that identifies the {@link ControllableFragment} in the application
	 * @param controllerMessenger the {@link FragmentControllerApplication} {@link Messenger}
	 * @return {@link ControllableFragmentCallback} if instantiation and initialization succeeded
	 * @throws ControlledFragmentException if instantiation fails
	 */
	public static ControllableFragmentCallback createCallback(Class<? extends ControllableFragmentCallback> clazz, 
			ControllableFragment fragment) throws ControlledFragmentException {
		ControllableFragmentCallback callback = null;
		try {
			Constructor<? extends ControllableFragmentCallback> cs = clazz.getConstructor(fragment.getClass());
			callback = cs.newInstance(fragment);
			callback.init(fragment);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ControlledFragmentException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ControlledFragmentException(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new ControlledFragmentException(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new ControlledFragmentException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new ControlledFragmentException(e.getMessage());
		}
		return callback;
	}


	/**
	 * Initializes this {@linkplain ControllableFragmentCallback} by setting the {@link ControllerFragment}
	 * <p>
	 * Subclass contract : superclass doInit MUST be called, otherwise an assertion failure will occur.
	 * </p>
	 * 
	 * @param controllerFragment
	 */
	@Override
	public final void init(ControllableFragment controllerFragment) {
		doInit(controllerFragment);
	}

	
	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what passed as parameter of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @return {@code true} if message could be sent, {@code false} otherwise	 */
	@Override
	public boolean sendToController(int what) {
		return sendToController(what, 0, 0, null);
	}

	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what, arg1 passed as parameters
	 * of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @param arg1 first message int argument
	 * @return {@code true} if message could be sent, {@code false} otherwise	 */
	@Override
	public boolean sendToController(int what, int arg1) {
		return sendToController(what, arg1, 0, null);
	}

	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what, obj passed as parameters
	 * of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @param obj Object argument
	 * @return {@code true} if message could be sent, {@code false} otherwise
	 */
	@Override
	public boolean sendToController(int what, Object obj) {
		return sendToController(what, 0, 0, obj);
	}

	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what, arg1, arg2 passed as parameters
	 * of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @param arg1 first message int argument
	 * @param arg2 second message int argment
	 * @return {@code true} if message could be sent, {@code false} otherwise
	 */
	@Override
	public boolean sendToController(int what, int arg1, int arg2) {
		return sendToController(what, arg1, arg2, null);
	}

	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what, arg1, obj passed as parameters
	 * of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @param arg1 first message int argument
	 * @param obj Object argument
	 * @return {@code true} if message could be sent, {@code false} otherwise
	 */
	@Override
	public boolean sendToController(int what, int arg1, Object obj) {
		return sendToController(what, arg1, 0, obj);
	}
	
	/**
	 * Sends a message to the controller via its supplied {@link Messenger}
	 * <p>
	 * A {@link ControlledFragment} can only communicate with the {@link FragmentControllerApplication}.
	 * </p>
	 * The sent message wraps the actual message that will be dispatched by the controller,
	 * and has the following values :
	 * <dl>
	 * <dt>what</dt><dd>{@link FragmentControllerApplication#MSG_DISPATCH_MESSAGE} => Dispatches the message in the controller</dd>
	 * <dt>arg1</dt><dd>The control ID assigned to the ControlledFragment that owns this callback</dd>
	 * <dt>arg2</dt><dd>The {@code what} passed as a parameter that needs to be dispatched</dd>
	 * <dt>obj</dt><dd>{@link Message} that will get dispatched (the {@code what} will be added to it)</dd>
	 * </dl>
	 * obj is a message instance that will eventually contain what, arg1, arg2, obj passed as parameters
	 * of this method.<br>
	 * 
	 * @param what message ID to get dispatched by the controller
	 * @param arg1 first message int argument
	 * @param arg2 second message int argment
	 * @param obj Object argument
	 * @return {@code true} if message could be sent, {@code false} otherwise
	 */
	@Override
	public boolean sendToController(int what, int arg1, int arg2, Object obj) {
		return mFragment.sendToController(what, arg1, arg2, obj);
	}

	/**
	 * Returns true if the current callback runs in the UI thread
	 * <p>
	 * This may be used within the {@linkplain Callback#handleMessage(Message)} to force
	 * certain operations to run in the UI thread.
	 * </p>
	 * 
	 * @return true if callback runs in the UI thread, false otherwise
	 */
	@Override
	public boolean runsOnUiThread() {
		return !mFragment.hasOwnThread();
	}

	/**
	 * Sends the specified Runnable to run on the UI thread
	 * 
	 * @param runnable the task to run on the UI thread
	 * @return itself for chaining
	 */	
	@Override
	public ControlledFragmentCallback sendToUi(Runnable runnable) {
		mFragment.sendToUi(runnable);
		return this;
	}
	
	/**
	 * Subclasses can override this method to perform initialization routines
	 * <p>
	 * Contract : this doInit MUST be called by the subclass, or an assertion failure will occur!
	 * </p>
	 * 
	 * @param controllerFragment the {@link ControlledFragment}
	 */
	protected void doInit(ControllableFragment controllableFragment) {
		mFragment = controllableFragment;
	}

	private ControllableFragment mFragment;
}

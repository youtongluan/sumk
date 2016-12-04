package org.yx.listener;

public class SumkEvent {

	protected Object source;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source
	 *            The object on which the Event initially occurred.
	 * @exception IllegalArgumentException
	 *                if source is null.
	 */
	public SumkEvent(Object source) {
		if (source == null)
			throw new IllegalArgumentException("null source");

		this.source = source;
	}

	/**
	 * The object on which the Event initially occurred.
	 *
	 * @return The object on which the Event initially occurred.
	 */
	public Object getSource() {
		return source;
	}
}

/*
    GNU GENERAL LICENSE
    Copyright (C) 2014 - 2018 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: ivan.difrancesco@yahoo.it
 */
package org.loboevolution.html.jsimpl;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.loboevolution.w3c.events.CompositionEvent;
import org.loboevolution.w3c.html.HTMLElement;
import org.w3c.dom.views.AbstractView;

/**
 * The Class CompositionEventImpl.
 */
public class CompositionEventImpl extends UIEventImpl implements CompositionEvent {

	/** The data. */
	private String data;

	/**
	 * Instantiates a new composition event impl.
	 */
	public CompositionEventImpl() {
	}

	/**
	 * Instantiates a new composition event impl.
	 *
	 * @param type
	 *            the type
	 * @param srcElement
	 *            the src element
	 */
	public CompositionEventImpl(String type, HTMLElement srcElement) {
		super(type, srcElement);
	}

	/**
	 * Instantiates a new composition event impl.
	 *
	 * @param type
	 *            the type
	 * @param srcElement
	 *            the src element
	 * @param mouseEvent
	 *            the mouse event
	 * @param leafX
	 *            the leaf x
	 * @param leafY
	 *            the leaf y
	 */
	public CompositionEventImpl(String type, HTMLElement srcElement, InputEvent mouseEvent, int leafX, int leafY) {
		super(type, srcElement, mouseEvent, leafX, leafY);
	}

	/**
	 * Instantiates a new composition event impl.
	 *
	 * @param type
	 *            the type
	 * @param srcElement
	 *            the src element
	 * @param keyEvent
	 *            the key event
	 */
	public CompositionEventImpl(String type, HTMLElement srcElement, KeyEvent keyEvent) {
		super(type, srcElement, keyEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.w3c.events.CompositionEvent#initCompositionEvent(java
	 * .lang.String, boolean, boolean, org.w3c.dom.views.AbstractView,
	 * java.lang.String)
	 */
	@Override
	public void initCompositionEvent(String type, boolean cancelBubble, boolean cancelable, AbstractView view,
			String data) {
		setType(type);
		setCanBubble(cancelBubble);
		setCancelable(cancelable);
		setView(view);
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.w3c.events.CompositionEvent#getData()
	 */
	@Override
	public String getData() {
		return data;
	}
}

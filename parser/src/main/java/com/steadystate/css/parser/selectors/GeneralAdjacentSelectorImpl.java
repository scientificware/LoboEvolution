/*
 * Copyright (C) 1999-2017 David Schweinsberg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.steadystate.css.parser.selectors;

import java.io.Serializable;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.LocatableImpl;

/**
 * @author Ahmed Ashour
 * @author rbri
 */
public class GeneralAdjacentSelectorImpl extends LocatableImpl implements SiblingSelector, CSSFormatable, Serializable {

	private static final long serialVersionUID = 1292704016876205605L;

	private short nodeType_;
	private Selector selector_;
	private SimpleSelector siblingSelector_;

	public void setNodeType(final short nodeType) {
		nodeType_ = nodeType;
	}

	public void setSelector(final Selector child) {
		selector_ = child;
		if (child instanceof Locatable) {
			setLocator(((Locatable) child).getLocator());
		} else if (child == null) {
			setLocator(null);
		}
	}

	public void setSiblingSelector(final SimpleSelector directAdjacent) {
		siblingSelector_ = directAdjacent;
	}

	public GeneralAdjacentSelectorImpl(final short nodeType, final Selector child,
			final SimpleSelector directAdjacent) {
		setNodeType(nodeType);
		setSelector(child);
		setSiblingSelector(directAdjacent);
	}

	@Override
	public short getNodeType() {
		return nodeType_;
	}

	@Override
	public short getSelectorType() {
		// not correct but the best choice from the old sac
		return Selector.SAC_DIRECT_ADJACENT_SELECTOR;
	}

	@Override
	public Selector getSelector() {
		return selector_;
	}

	@Override
	public SimpleSelector getSiblingSelector() {
		return siblingSelector_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCssText(final CSSFormat format) {
		final StringBuilder sb = new StringBuilder();

		if (null != selector_) {
			sb.append(((CSSFormatable) selector_).getCssText(format));
		}

		sb.append(" ~ ");

		if (null != siblingSelector_) {
			sb.append(((CSSFormatable) siblingSelector_).getCssText(format));
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return getCssText(null);
	}
}

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

package org.loboevolution.html.renderstate;

import org.loboevolution.html.domimpl.HTMLDocumentImpl;
import org.loboevolution.html.domimpl.HTMLElementImpl;

/**
 * Render state for elements that are displayed as blocks by default.
 */
public class BlockRenderState extends StyleSheetRenderState {

	/**
	 * Instantiates a new block render state.
	 *
	 * @param prevRenderState
	 *            the prev render state
	 * @param element
	 *            the element
	 */
	public BlockRenderState(RenderState prevRenderState, HTMLElementImpl element) {
		super(prevRenderState, element);
	}

	/**
	 * Instantiates a new block render state.
	 *
	 * @param document
	 *            the document
	 */
	public BlockRenderState(HTMLDocumentImpl document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderstate.StyleSheetRenderState#getDefaultDisplay(
	 * )
	 */
	@Override
	public int getDefaultDisplay() {
		return DISPLAY_BLOCK;
	}
}

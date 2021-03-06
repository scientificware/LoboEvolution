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
package org.loboevolution.html.renderer;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.loboevolution.html.dombl.ModelNode;
import org.loboevolution.html.info.FloatingInfo;
import org.loboevolution.util.CollectionUtilities;

/**
 * The Class RRelative.
 */
public class RRelative extends BaseRCollection {

	/** The child. */
	private final RElement child;

	/** The xoffset. */
	private final int xoffset;

	/** The yoffset. */
	private final int yoffset;

	/**
	 * Instantiates a new r relative.
	 *
	 * @param container
	 *            the container
	 * @param modelNode
	 *            the model node
	 * @param child
	 *            the child
	 * @param xoffset
	 *            the xoffset
	 * @param yoffset
	 *            the yoffset
	 */
	public RRelative(RenderableContainer container, ModelNode modelNode, final RElement child, final int xoffset,
			final int yoffset) {
		super(container, modelNode);
		child.setOriginalParent(this);
		child.setParent(this);
		child.setOrigin(xoffset, yoffset);
		this.child = child;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
	}

	/**
	 * Assign dimension.
	 */
	public void assignDimension() {
		RElement child = this.child;
		this.width = child.getWidth();
		this.height = child.getHeight();
	}

	/**
	 * Gets the exportable floating info.
	 *
	 * @return the exportable floating info
	 */
	public FloatingInfo getExportableFloatingInfo() {
		RElement child = this.child;
		if (child instanceof RBlock) {
			final FloatingInfo floatingInfo = ((RBlock) child).getExportableFloatingInfo();
			if (floatingInfo == null) {
				return null;
			} else {
				return new FloatingInfo(xoffset + floatingInfo.getShiftX(), yoffset + floatingInfo.getShiftY(),
						floatingInfo.getFloats());
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public RElement getElement() {
		return this.child;
	}

	/**
	 * Gets the x offset.
	 *
	 * @return the x offset
	 */
	public int getXOffset() {
		return xoffset;
	}

	/**
	 * Gets the y offset.
	 *
	 * @return the y offset
	 */
	public int getYOffset() {
		return yoffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.renderer.RCollection#getRenderables()
	 */
	@Override
	public Iterator getRenderables() {
		return CollectionUtilities.singletonIterator(this.child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.renderer.BaseBoundableRenderable#
	 * invalidateLayoutLocal()
	 */
	@Override
	protected void invalidateLayoutLocal() {
		// Method not implemented
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#getLowestRenderableSpot
	 * (int, int)
	 */
	@Override
	public RenderableSpot getLowestRenderableSpot(int x, int y) {
		return this.child.getLowestRenderableSpot(x - this.xoffset, y - this.yoffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#isContainedByNode()
	 */
	@Override
	public boolean isContainedByNode() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#onDoubleClick(java.awt.
	 * event.MouseEvent, int, int)
	 */
	@Override
	public boolean onDoubleClick(MouseEvent event, int x, int y) {
		return this.child.onDoubleClick(event, x - this.xoffset, y - this.yoffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#onMouseClick(java.awt.
	 * event .MouseEvent, int, int)
	 */
	@Override
	public boolean onMouseClick(MouseEvent event, int x, int y) {
		return this.child.onMouseClick(event, x - this.xoffset, y - this.yoffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#onMouseDisarmed(java.
	 * awt .event.MouseEvent)
	 */
	@Override
	public boolean onMouseDisarmed(MouseEvent event) {
		return this.child.onMouseDisarmed(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#onMousePressed(java.awt
	 * .event.MouseEvent, int, int)
	 */
	@Override
	public boolean onMousePressed(MouseEvent event, int x, int y) {
		return this.child.onMousePressed(event, x - this.xoffset, y - this.yoffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.renderer.BoundableRenderable#onMouseReleased(java.
	 * awt .event.MouseEvent, int, int)
	 */
	@Override
	public boolean onMouseReleased(MouseEvent event, int x, int y) {
		return this.child.onMouseReleased(event, x - this.xoffset, y - this.yoffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.renderer.Renderable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		this.child.paintTranslated(g);
	}
}

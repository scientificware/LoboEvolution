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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import org.loboevolution.font.FontFactory;
import org.loboevolution.font.FontKey;
import org.loboevolution.html.info.WordInfo;
import org.loboevolution.html.style.RenderStateDelegator;

/**
 * The Class FontStyleRenderState.
 */
public class FontStyleRenderState extends RenderStateDelegator {

	/** The style. */
	private final int style;

	/** The superscript. */
	private final Integer superscript;
	
	/** The i word info map. */
	private Map<String, WordInfo> iWordInfoMap = null;
	
	/** The i font. */
	private Font iFont;
	
	/** The i font metrics. */
	private FontMetrics iFontMetrics;

	/**
	 * Instantiates a new font style render state.
	 *
	 * @param prevRenderState
	 *            the prev render state
	 * @param style
	 *            the style
	 */
	public FontStyleRenderState(RenderState prevRenderState, int style) {
		this(prevRenderState, style, null);
	}

	/**
	 * Instantiates a new font style render state.
	 *
	 * @param prevRenderState
	 *            the prev render state
	 * @param style
	 *            the style
	 * @param superscript
	 *            the superscript
	 */
	FontStyleRenderState(RenderState prevRenderState, int style, Integer superscript) {
		super(prevRenderState);
		this.style = style;
		this.superscript = superscript;
	}

	/**
	 * Creates the superscript font style render state.
	 *
	 * @param prevRenderState
	 *            the prev render state
	 * @param superscript
	 *            the superscript
	 * @return the font style render state
	 */
	public static FontStyleRenderState createSuperscriptFontStyleRenderState(RenderState prevRenderState,
			Integer superscript) {
		return new FontStyleRenderState(prevRenderState, prevRenderState.getFont().getStyle(), superscript);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.style.RenderStateDelegator#getFont()
	 */
	@Override
	public Font getFont() {
		Font f = this.iFont;
		if (f != null) {
			return f;
		}
		Font parentFont = this.delegate.getFont();
		if (parentFont.getStyle() != this.style) {
			f = parentFont.deriveFont(this.style | parentFont.getStyle());
		} else {
			f = parentFont;
		}
		FontKey key = new FontKey();
		key.setFontFamily("");
		key.setFontStyle("");
		key.setFontVariant("");
		key.setFontWeight("");
		key.setFontSize(0);
		key.setLocales(null);
		key.setSuperscript(superscript);
		key.setLetterSpacing(0);
		key.setStrikethrough(false);
		key.setUnderline(0);
		f = FontFactory.scriptFont(f, key);
		this.iFont = f;
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.style.RenderStateDelegator#getFontMetrics()
	 */
	@Override
	public FontMetrics getFontMetrics() {
		FontMetrics fm = this.iFontMetrics;
		if (fm == null) {
			// TODO getFontMetrics deprecated. How to get text width?
			fm = Toolkit.getDefaultToolkit().getFontMetrics(this.getFont());
			this.iFontMetrics = fm;
		}
		return fm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.loboevolution.html.style.RenderStateDelegator#invalidate()
	 */
	@Override
	public void invalidate() {
		this.delegate.invalidate();
		this.iFont = null;
		this.iFontMetrics = null;
		Map<String, WordInfo> map = this.iWordInfoMap;
		if (map != null) {
			map.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.loboevolution.html.style.RenderStateDelegator#getWordInfo(java.lang.
	 * String)
	 */
	@Override
	public final WordInfo getWordInfo(String word) {
		// Expected to be called only in the GUI (rendering) thread.
		// No synchronization necessary.
		Map<String, WordInfo> map = this.iWordInfoMap;
		if (map == null) {
			map = new HashMap<String, WordInfo>(1);
			this.iWordInfoMap = map;
		}
		WordInfo wi = map.get(word);
		if (wi != null) {
			return wi;
		}
		wi = new WordInfo();
		FontMetrics fm = this.getFontMetrics();
		wi.setFontMetrics(fm);
		wi.setAscentPlusLeading(fm.getAscent() + fm.getLeading());
		wi.setDescent(fm.getDescent());
		wi.setHeight(fm.getHeight());
		wi.setWidth(fm.stringWidth(word));
		map.put(word, wi);
		return wi;
	}
}

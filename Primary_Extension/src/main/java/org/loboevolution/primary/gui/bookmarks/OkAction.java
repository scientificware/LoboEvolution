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
package org.loboevolution.primary.gui.bookmarks;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.loboevolution.primary.info.BookmarkInfo;
import org.loboevolution.util.Strings;

public class OkAction extends AbstractAction{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private transient AddBookmarkDialog add;
	
	public OkAction(AddBookmarkDialog add) {
		this.add = add;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
	 * ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		BookmarkInfo binfo = new BookmarkInfo();
		binfo.setUrl(add.getUrl());
		binfo.setTitle(add.getTitleField().getValue());
		binfo.setDescription(add.getDescriptionField().getValue());
		binfo.setTags(Strings.split(add.getTagsField().getValue()));
		add.setBookmarkInfo(binfo);
		add.dispose();
	}

}

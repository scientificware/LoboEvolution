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
package org.loboevolution.primary.ext;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/*
 * Created on Jun 6, 2005
 */
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The Class TextFieldComboBoxEditor.
 *
 * @author J. H. S.
 */
public class TextFieldComboBoxEditor implements ComboBoxEditor {

	/** The text field. */
	private final JTextField textField;

	/** The in notification. */
	private boolean inNotification = false;

	/**
	 * Instantiates a new text field combo box editor.
	 */
	public TextFieldComboBoxEditor() {
		this.textField = new JTextField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#getEditorComponent()
	 */
	@Override
	public Component getEditorComponent() {
		return this.textField;
	}

	// private transient Object item;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#setItem(Object)
	 */
	@Override
	public void setItem(Object arg0) {
		// this.item = arg0;
		if (!this.inNotification) {
			this.textField.setText(arg0 == null ? "" : String.valueOf(arg0));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#getItem()
	 */
	@Override
	public Object getItem() {
		return this.textField.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#selectAll()
	 */
	@Override
	public void selectAll() {
		this.textField.selectAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#addActionListener(ActionListener )
	 */
	@Override
	public void addActionListener(ActionListener arg0) {
		this.textField.addActionListener(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ComboBoxEditor#removeActionListener(ActionListener )
	 */
	@Override
	public void removeActionListener(ActionListener arg0) {
		this.textField.removeActionListener(arg0);
	}

	/**
	 * Adds the key listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addKeyListener(KeyListener listener) {
		this.textField.addKeyListener(listener);
	}

	/**
	 * Removes the key listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeKeyListener(KeyListener listener) {
		this.textField.removeKeyListener(listener);
	}

	/**
	 * Adds the change listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addChangeListener(final ChangeListener listener) {
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				TextFieldComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(TextFieldComboBoxEditor.this));
				} finally {
					TextFieldComboBoxEditor.this.inNotification = false;
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				TextFieldComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(TextFieldComboBoxEditor.this));
				} finally {
					TextFieldComboBoxEditor.this.inNotification = false;
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				TextFieldComboBoxEditor.this.inNotification = true;
				try {
					listener.stateChanged(new ChangeEvent(TextFieldComboBoxEditor.this));
				} finally {
					TextFieldComboBoxEditor.this.inNotification = false;
				}
			}
		});
	}

}

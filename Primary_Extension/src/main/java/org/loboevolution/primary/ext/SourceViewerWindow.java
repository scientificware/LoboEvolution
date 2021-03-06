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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.loboevolution.util.Strings;

/**
 * The Class SourceViewerWindow.
 */
public class SourceViewerWindow extends JFrame {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SourceViewerWindow.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The text area. */
	private transient RSyntaxTextArea textArea;

	/** The scroll bar. */
	private transient RTextScrollPane scrollBar;

	/** The jtf filter. */
	private JTextField jtfFilter;

	/** The find bsutton. */
	private JButton findButton;

	/** The pos. */
	private int pos = 0;

	/**
	 * Instantiates a new text viewer window.
	 */
	public SourceViewerWindow() {
		super("Lobo Source Viewer");
		createAndShowGUI();
	}

	private void createAndShowGUI() {
		setResizable(true);
		setLocationRelativeTo(null);
		addTextArea();
	}
	
	@Override
	public Image getIconImage() {
		return new ImageIcon(getClass().getResource("/org/loboevolution/images/icon.png")).getImage();
	}
	
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.textArea.setText(text);
	}

	private void addTextArea() {
		jtfFilter = new JTextField();
		findButton = new JButton("Next word");
		textArea = new RSyntaxTextArea();
		textArea.setHighlightCurrentLine(true);
		textArea.setAnimateBracketMatching(true);
		textArea.setAntiAliasingEnabled(true);
		textArea.setEditable(false);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);

		scrollBar = new RTextScrollPane(textArea);
		scrollBar.setBorder(null);
		scrollBar.setLineNumbersEnabled(true);
		scrollBar.setViewportView(textArea);

		this.getContentPane().add(scrollBar);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Specify a word to match:"), BorderLayout.WEST);
		panel.add(jtfFilter, BorderLayout.CENTER);
		panel.add(findButton, BorderLayout.EAST);
		add(panel, BorderLayout.SOUTH);

		findButton.addActionListener(e -> {
			String find = jtfFilter.getText().toLowerCase();
			textArea.requestFocusInWindow();
			if (!Strings.isBlank(find)) {
				Document document = textArea.getDocument();
				int findLength = find.length();
				try {
					boolean found = false;
					if (pos + findLength > document.getLength()) {
						pos = 0;
					}
					while (pos + findLength <= document.getLength()) {
						String match = document.getText(pos, findLength).toLowerCase();
						if (match.equals(find)) {
							found = true;
							break;
						}
						pos++;
					}
					if (found) {
						Rectangle viewRect = textArea.modelToView(pos);
						textArea.scrollRectToVisible(viewRect);
						textArea.setCaretPosition(pos + findLength);
						textArea.moveCaretPosition(pos);
						pos += findLength;
					}

				} catch (Exception exp) {
					logger.error(exp);
				}

			}
		});
	}
}

/*
 * Copyright (c) 2013, Tomas Mikula. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package codearea.control;

import javafx.scene.control.IndexRange;

/**
 * Extended edit actions for {@link TextEditingArea}.
 */
public interface EditActions<S> extends TextEditingArea<S> {

    /**
     * Appends the given text to the end of the text content.
     */
    default void appendText(String text) {
        insertText(getLength(), text);
    }

    /**
     * Inserts the given text at the given position.
     *
     * @param index The location to insert the text.
     * @param text The text to insert.
     */
    default void insertText(int index, String text) {
        replaceText(index, index, text);
    }

    /**
     * Removes a range of text.
     *
     * @param range The range of text to delete. It must not be null.
     *
     * @see #deleteText(int, int)
     */
    default void deleteText(IndexRange range) {
        deleteText(range.getStart(), range.getEnd());
    }

    /**
     * Removes a range of text.
     *
     * It must hold {@code 0 <= start <= end <= getLength()}.
     *
     * @param start Start index of the range to remove, inclusive.
     * @param end End index of the range to remove, exclusive.
     */
    default void deleteText(int start, int end) {
        replaceText(start, end, "");
    }

    /**
     * Deletes the character that precedes the current caret position
     * from the text.
     */
    default void deletePreviousChar() {
        int end = getCaretPosition();
        if(end > 0) {
            int start = Character.offsetByCodePoints(getText(), end, -1);
            deleteText(start, end);
        }
    }

    /**
     * Deletes the character that follows the current caret position
     * from the text.
     */
    default void deleteNextChar() {
        int start = getCaretPosition();
        if(start < getLength()) {
            int end = Character.offsetByCodePoints(getText(), start, 1);
            deleteText(start, end);
        }
    }

    /**
     * Clears the text.
     */
    default void clear() {
        replaceText(0, getLength(), "");
    }

    /**
     * Replaces the entire text content with the given text.
     */
    default void replaceText(String replacement) {
        replaceText(0,  getLength(), replacement);
    }

    /**
     * Replaces the selection with the given replacement String. If there is
     * no selection, then the replacement text is simply inserted at the current
     * caret position. If there was a selection, then the selection is cleared
     * and the given replacement text is inserted.
     */
    default void replaceSelection(String replacement) {
        replaceText(getSelection(), replacement);
    }

    default void moveSelectedText(int pos) {
        IndexRange sel = getSelection();

        if(pos >= sel.getStart() && pos <= sel.getEnd()) {
            // no move, just position the caret
            selectRange(pos, pos);
        }
        else {
            String text = getSelectedText();
            if(pos > sel.getEnd())
                pos -= sel.getLength();
            deleteText(sel);
            insertText(pos, text);
        }
    }
}

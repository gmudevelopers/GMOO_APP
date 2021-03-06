/*
 * MIT License
 *
 * Copyright (c) 2017 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gmoo.com.gmudevelopers.edu.library.structure;

/**
 * {@link DrawerItem} which should only display a divider.
 */
public class DrawerHeaderItem extends DrawerItem {
    public DrawerHeaderItem() {
        setIsHeader(true);
    }


    /**
     * Sets a title to the header
     *
     * @param title Title to set
     */
    public DrawerItem setTitle(String title) {
        setTextPrimary(title);
        return this;
    }

    /**
     * Gets the title of the header
     *
     * @return Title of the header
     */
    public String getTitle() {
        return getTextPrimary();
    }

    /**
     * Gets whether the header has a title set to it
     *
     * @return True if the header has a title set to it, false otherwise.
     */
    public boolean hasTitle() {
        return hasTextPrimary();
    }


    /**
     * Removes the title from the header
     */
    public DrawerItem removeTitle() {
        removeTextPrimary();
        return this;
    }
}

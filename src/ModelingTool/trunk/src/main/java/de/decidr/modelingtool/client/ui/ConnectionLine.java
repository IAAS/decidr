/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.modelingtool.client.ui;

import com.google.gwt.user.client.ui.HTML;

/**
 * A single line in a connection.
 * 
 * @author Johannes Engelhardt
 */
public class ConnectionLine extends HTML {

    /** The thickness of the line in pixels. */
    private int thickness;

    /** The connection which owns the connection line. */
    private Connection connection;

    /**
     * The constructor.
     * 
     * @param connection
     *            The parent connection of the connection line
     * @param thickness
     *            The thickness of the line in pixels
     */
    public ConnectionLine(Connection connection, int thickness) {
        this.connection = connection;
        this.thickness = thickness;

        setStyleName("connection-line");
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the orientation of the line to horizontal.
     * 
     * @param width
     *            The length of the horizontal line
     */
    public void setHorizontalOrientation(int width) {
        setPixelSize(width, thickness);
    }

    /**
     * Sets the graphical selected representation of the line.
     * 
     * @param selected
     *            The selected state of the line.
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setStyleName("connection-line-selected");
        } else {
            setStyleName("connection-line");
        }
    }

    /**
     * Sets the orientation of the line to vertical.
     * 
     * @param height
     *            The length of the vertical line
     */
    public void setVerticalOrientation(int height) {
        setPixelSize(thickness, height);
    }

}

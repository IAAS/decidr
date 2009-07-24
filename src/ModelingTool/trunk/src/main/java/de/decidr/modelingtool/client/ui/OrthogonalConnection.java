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

import com.google.gwt.user.client.ui.AbsolutePanel;

import de.decidr.modelingtool.client.ui.selection.SelectionHandler;

/**
 * A connection with orthogonal lines.
 * 
 * @author Johannes Engelhardt
 */
public class OrthogonalConnection extends Connection {

    /** The width of the connection lines. */
    private final int LINE_WIDTH = 2;

    /** X coordinate offset of the label position relative to the middle line. */
    private final int LABEL_XOFFSET = 5;
    /** Y coordinate offset of the label position relative to the middle line. */
    private final int LABEL_YOFFSET = -20;

    /** the start line of the connection. */
    private ConnectionLine startLine = new ConnectionLine(this, LINE_WIDTH);
    /** The middle line of the connection. */
    private ConnectionLine midLine = new ConnectionLine(this, LINE_WIDTH);
    /** The end line of the connection. */
    private ConnectionLine endLine = new ConnectionLine(this, LINE_WIDTH);

    public OrthogonalConnection(HasChildren parentPanel) {
        super(parentPanel);

        // register selection handler to lines
        SelectionHandler sh = SelectionHandler.getInstance();
        startLine.addMouseDownHandler(sh);
        midLine.addMouseDownHandler(sh);
        endLine.addMouseDownHandler(sh);
        //label.addMouseDownHandler(sh);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        startLine.setSelected(selected);
        midLine.setSelected(selected);
        endLine.setSelected(selected);
    }

    @Override
    public void draw() {
        if (parentPanel instanceof AbsolutePanel && startDragBox != null
                && endDragBox != null) {
            AbsolutePanel absPanel = (AbsolutePanel) parentPanel;

            int startX = startDragBox.getMiddleLeft() - parentPanel.getLeft()
                    - LINE_WIDTH / 2;
            int startY = startDragBox.getMiddleTop() - parentPanel.getTop();
            int endX = endDragBox.getMiddleLeft() - parentPanel.getLeft()
                    - LINE_WIDTH / 2;
            int endY = endDragBox.getMiddleTop() - parentPanel.getTop();

            // calculate height and width
            int width = Math.abs(startX - endX) + LINE_WIDTH;
            int height = Math.abs(startY - endY);

            // add lines to panel / brint to front
            absPanel.add(startLine);
            absPanel.add(midLine);
            absPanel.add(endLine);
            // add label to panel /bring to front
            absPanel.add(label);

            if (startY <= endY) {
                absPanel.setWidgetPosition(startLine, startX, startY);
                absPanel.setWidgetPosition(endLine, endX, endY - height / 2);
            } else {
                absPanel.setWidgetPosition(startLine, startX, startY - height
                        / 2);
                absPanel.setWidgetPosition(endLine, endX, endY);
            }

            if (startX <= endX) {
                absPanel.setWidgetPosition(midLine, startX, (startY + endY) / 2);
                absPanel.setWidgetPosition(label, startX + LABEL_XOFFSET,
                        (startY + endY) / 2 + LABEL_YOFFSET);
            } else {
                absPanel.setWidgetPosition(midLine, endX, (startY + endY) / 2);
                absPanel.setWidgetPosition(label, endX + LABEL_XOFFSET,
                        (startY + endY) / 2 + LABEL_YOFFSET);
            }

            // set orientation and length of lines
            startLine.setVerticalOrientation(height / 2);
            endLine.setVerticalOrientation(height / 2);
            midLine.setHorizontalOrientation(width);

        } else {
            // TODO: Debug
            System.out.println("Connection cannot be drawn");
        }
    }

    @Override
    public void remove() {
        startLine.removeFromParent();
        midLine.removeFromParent();
        endLine.removeFromParent();
        label.removeFromParent();
    }

}

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

    /** The contraction of the end lines in pixels. */
    private final int LINE_CONTRACTION = 4;

    /** The length of the end lines in pixels. */
    private final int STARTEND_LINE_LENGTH = 20;

    /** X coordinate offset of the label position relative to the middle line. */
    private final int LABEL_XOFFSET = 5;
    /** Y coordinate offset of the label position relative to the middle line. */
    private final int LABEL_YOFFSET = -20;

    /** The start line of the connection. */
    private ConnectionLine startLine = new ConnectionLine(this, LINE_WIDTH);
    /** The line between start line and middle line. */
    private ConnectionLine startMidLine = new ConnectionLine(this, LINE_WIDTH);
    /** The middle line of the connection. */
    private ConnectionLine midLine = new ConnectionLine(this, LINE_WIDTH);
    /** The end line of the connection. */
    private ConnectionLine endMidLine = new ConnectionLine(this, LINE_WIDTH);
    /** The line beween middle line and end line. */
    private ConnectionLine endLine = new ConnectionLine(this, LINE_WIDTH);

    public OrthogonalConnection(HasChildren parentPanel) {
        super(parentPanel);

        // register selection handler to lines
        SelectionHandler sh = SelectionHandler.getInstance();
        startLine.addMouseDownHandler(sh);
        startMidLine.addMouseDownHandler(sh);
        midLine.addMouseDownHandler(sh);
        endMidLine.addMouseDownHandler(sh);
        endLine.addMouseDownHandler(sh);
    }

    @Override
    public void draw() {
        assert ((parentPanel instanceof AbsolutePanel)
                && (startDragBox != null) && (endDragBox != null));
        AbsolutePanel absPanel = (AbsolutePanel) parentPanel;

        int startX = startDragBox.getMiddleLeft() - LINE_WIDTH / 2;
        int startY = startDragBox.getMiddleTop();
        int endX = endDragBox.getMiddleLeft() - LINE_WIDTH / 2;
        int endY = endDragBox.getMiddleTop();

        // calculate offset values that are caused by the parent panels
        if (parentPanel instanceof Container) {
            startX = startX - ((Container) parentPanel).getParentXOffset();
            startY = startY - ((Container) parentPanel).getParentYOffset();
            endX = endX - ((Container) parentPanel).getParentXOffset();
            endY = endY - ((Container) parentPanel).getParentYOffset();
        }

        // calculate width and height
        int width = Math.abs(startX - endX) + LINE_WIDTH;
        int height = Math.abs(startY - endY);

        // add lines to panel / bring to front
        absPanel.add(startMidLine);
        absPanel.add(midLine);
        absPanel.add(endMidLine);
        // add label to panel / bring to front
        absPanel.add(label);

        if (startY <= endY) {
            // startLine and endLine not needed
            startLine.removeFromParent();
            endLine.removeFromParent();

            // 3-part line
            absPanel.setWidgetPosition(startMidLine, startX, startY
                    + LINE_CONTRACTION);
            absPanel.setWidgetPosition(endMidLine, endX, endY - height / 2);

            if (startX <= endX) {
                absPanel
                        .setWidgetPosition(midLine, startX, (startY + endY) / 2);
                absPanel.setWidgetPosition(label, startX + LABEL_XOFFSET,
                        (startY + endY) / 2 + LABEL_YOFFSET);
            } else {
                absPanel.setWidgetPosition(midLine, endX, (startY + endY) / 2);
                absPanel.setWidgetPosition(label, endX + LABEL_XOFFSET,
                        (startY + endY) / 2 + LABEL_YOFFSET);
            }

            // set orientation and length of lines
            startMidLine.setVerticalOrientation(height / 2 - LINE_CONTRACTION);
            endMidLine.setVerticalOrientation(height / 2 - LINE_CONTRACTION);
            midLine.setHorizontalOrientation(width);

        } else {
            // 5-part-line
            // add additional lines
            absPanel.add(startLine);
            absPanel.add(endLine);

            absPanel.setWidgetPosition(startLine, startX, startY
                    + LINE_CONTRACTION);
            startLine.setVerticalOrientation(STARTEND_LINE_LENGTH + LINE_WIDTH);

            absPanel.setWidgetPosition(endLine, endX, endY
                    - STARTEND_LINE_LENGTH - LINE_CONTRACTION);
            endLine.setVerticalOrientation(STARTEND_LINE_LENGTH);

            absPanel.setWidgetPosition(midLine, (startX + endX) / 2, endY
                    - STARTEND_LINE_LENGTH - LINE_CONTRACTION);
            midLine.setVerticalOrientation(height + STARTEND_LINE_LENGTH * 2
                    + LINE_CONTRACTION * 2 + LINE_WIDTH);

            if (startX <= endX) {
                absPanel.setWidgetPosition(startMidLine, startX, startY
                        + STARTEND_LINE_LENGTH + LINE_CONTRACTION);
                absPanel.setWidgetPosition(endMidLine, (startX + endX) / 2,
                        endY - STARTEND_LINE_LENGTH - LINE_CONTRACTION);
            } else {
                absPanel.setWidgetPosition(startMidLine, (startX + endX) / 2,
                        startY + STARTEND_LINE_LENGTH + LINE_CONTRACTION);
                absPanel.setWidgetPosition(endMidLine, endX, endY
                        - STARTEND_LINE_LENGTH - LINE_CONTRACTION);
            }

            startMidLine.setHorizontalOrientation(width / 2 + LINE_WIDTH / 2);
            endMidLine.setHorizontalOrientation(width / 2 + LINE_WIDTH / 2);
        }
    }

    @Override
    public void remove() {
        startLine.removeFromParent();
        startMidLine.removeFromParent();
        midLine.removeFromParent();
        endMidLine.removeFromParent();
        endLine.removeFromParent();
        label.removeFromParent();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        startLine.setSelected(selected);
        startMidLine.setSelected(selected);
        midLine.setSelected(selected);
        endMidLine.setSelected(selected);
        endLine.setSelected(selected);
    }

}

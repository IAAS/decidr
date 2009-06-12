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
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class OrthogonalConnection extends Connection {

    private final int LINE_WIDTH = 1;
    private final String STYLE_HORIZONTAL = "connection-orthogonal-horizontalline";
    private final String STYLE_VERTICAL = "connection-orthogonal-verticalline";

    private HTML startLine = new HTML();
    private HTML midLine = new HTML();
    private HTML endLine = new HTML();

    public OrthogonalConnection() {
        super();
    }

    @Override
    public void draw() {
        if (parentPanel != null && startDragBox != null && endDragBox != null) {       
            int startX = startDragBox.getMiddleLeft();
            int startY = startDragBox.getMiddleTop();
            int endX = endDragBox.getMiddleLeft();
            int endY = endDragBox.getMiddleTop();
            
            // calculate height and width
            int width = Math.abs(startX - endX);
            int height = Math.abs(startY - endY);
            
            // add lines to panel / brint to front
            parentPanel.add(startLine);
            parentPanel.add(midLine);
            parentPanel.add(endLine);
            
            startLine.setStyleName(STYLE_VERTICAL);
            midLine.setStyleName(STYLE_HORIZONTAL);
            endLine.setStyleName(STYLE_VERTICAL);
            
            if (startY <= endY) {
                parentPanel.setWidgetPosition(startLine, startX, startY);
                parentPanel.setWidgetPosition(endLine, endX, endY - height / 2);
            } else {
                parentPanel.setWidgetPosition(startLine, startX, startY - height / 2);
                parentPanel.setWidgetPosition(endLine, endX, endY);
            }
            
            if (startX <= endX) {
                parentPanel.setWidgetPosition(midLine, startX, (startY + endY) / 2);
            } else {
                parentPanel.setWidgetPosition(midLine, endX, (startY + endY) / 2);
            }
            
            startLine.setHeight((height / 2) + "px");
            startLine.setWidth(LINE_WIDTH + "px");
            
            endLine.setHeight((height / 2) + "px");
            endLine.setWidth(LINE_WIDTH + "px");
            
            midLine.setWidth(width + "px");
            midLine.setHeight(LINE_WIDTH + "px");
        }
    }

    @Override
    public void delete() {
        startLine.removeFromParent();
        midLine.removeFromParent();
        endLine.removeFromParent();
    }

}

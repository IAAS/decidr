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

package de.decidr.modelingtool.client.ui.dialogs.canvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class Spinner extends HorizontalPanel {

    private Image upImage;
    private Image downImage;
    private VerticalPanel images = new VerticalPanel();
    private TextBox box = new TextBox();
    private int step = 20;
    private int max = 0;
    private int min = 0;
    private int curVal = 0;
    // delay to wait before increasing/decreasing further, in milliseconds
    private int delay = 120;

    Timer t_raise = new Timer() {
        public void run() {
            raise();
        }
    };
    Timer t_lower = new Timer() {
        public void run() {
            lower();
        }
    };

    public Spinner(int minVal, int maxVal, int stepVal, int startVal) {
        SpinnerImageBundle imgBundle = GWT.create(SpinnerImageBundle.class);
        upImage = new Image(imgBundle.up().getHTML());
        downImage = new Image(imgBundle.down().getHTML());

        upImage.setPixelSize(10, 10);
        downImage.setPixelSize(10, 10);

        // Take over parameters
        step = stepVal;
        max = maxVal;
        min = minVal;

        // set curVal inside min or max bounds
        if (startVal < min) {
            curVal = min;
        } else if (startVal > max) {
            curVal = max;
        } else {
            curVal = startVal;
        }

        // Build our UI
        images.add(upImage);
        upImage.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                t_raise.run();
                t_raise.scheduleRepeating(delay);

            }
        });
        upImage.addMouseUpHandler(new MouseUpHandler() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.
             * google.gwt.event.dom.client.MouseUpEvent)
             */
            @Override
            public void onMouseUp(MouseUpEvent event) {
                t_raise.cancel();
            }
        });

        images.add(downImage);
        downImage.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                t_lower.run();
                t_lower.scheduleRepeating(delay);

            }
        });
        downImage.addMouseUpHandler(new MouseUpHandler() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.
             * google.gwt.event.dom.client.MouseUpEvent)
             */
            @Override
            public void onMouseUp(MouseUpEvent event) {
                t_lower.cancel();
            }
        });

        updateTextBox();
        box.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                TextBox tb = (TextBox) event.getSource();
                int index = tb.getCursorPos();
                String str = tb.getText();

                // If no number has been entered, ignore it
                if (!Character.isDigit(event.getCharCode())) {
                    tb.cancelKey();
                    return;
                }

                // If we have 0123 as input and 123 is selected, we need to
                // calculate with "09" if "9" was entered
                String newstr;
                if (tb.getSelectionLength() > 0) {
                    // str = str.replaceFirst(str.substring(tb.getCursorPos(),
                    // tb.getSelectionLength()+1), "");
                    newstr = str.substring(0, tb.getCursorPos());
                    newstr += event.getCharCode();
                    newstr += str.substring(tb.getCursorPos()
                            + tb.getSelectionLength(), str.length());

                } else {

                    newstr = str.substring(0, index) + event.getCharCode()
                            + str.substring(index, str.length());
                }

                int newint = Integer.parseInt(newstr);

                // If we are not inside the bounds, leave this method
                // TODO: Choose, whether to display the maxvalue or leave
                if (newint > max || newint < min) {
                    tb.cancelKey();
                    return;
                }

                curVal = newint;

            }

        });

        // set width analog to max length of the maximum value
        box.setWidth((((max + "").length() + 1) / 2) + 1 + "em");
        box.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
        add(box);
        add(images);
    }

    private void updateTextBox() {
        box.setText(curVal + "");
        if (curVal == max) {
            t_raise.cancel();
        } else if (curVal == min) {
            t_lower.cancel();
        }
    }

    private void raise() {
        if (curVal + step < max) {
            curVal += step;
        } else {
            curVal = max;
        }
        updateTextBox();
    }

    private void lower() {
        if (curVal - step > min) {
            curVal -= step;
        } else {
            curVal = min;
        }
        updateTextBox();
    }

    public void setDelay(int delayMilliSeconds) {
        delay = delayMilliSeconds;
    }

    public int getValue() {
        return curVal;
    }
}

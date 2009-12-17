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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * A simple spinner input field for integers.
 * 
 * @author Jonas Schlaak
 */
public class Spinner extends HorizontalPanel {

    private TextBox textBox = new TextBox();

    private VerticalPanel buttons = new VerticalPanel();
    private PushButton upButton;
    private PushButton downButton;
    private Image upImage;
    private Image downImage;

    private static final int STEPSIZE = 100;

    private int max = 0;
    private int min = 0;
    private int value = 0;
    private int delay = 150;

    private Timer raiser;
    private Timer lowerer;

    /**
     * Default constructor.
     * 
     * @param min
     *            the lower bound
     * @param max
     *            the upper bound
     * @param startValue
     *            the initial value
     */
    public Spinner(int min, int max, int startValue) {
        createTimers();

        SpinnerImageBundle imgBundle = GWT.create(SpinnerImageBundle.class);
        upImage = imgBundle.up().createImage();
        downImage = imgBundle.down().createImage();

        upButton = new PushButton(upImage);

        downButton = new PushButton(downImage);

        this.max = max;
        this.min = min;

        // set current value within bounds
        if (startValue < this.min) {
            value = this.min;
        } else if (startValue > this.max) {
            value = this.max;
        } else {
            value = startValue;
        }

        // create ui
        createTextField();
        createButtons();
        updateTextBox();

    }

    private void createTextField() {
        textBox = new TextBox();

        textBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
        textBox.setWidth(((Integer) max).toString().length() + "em");

        textBox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                int cursorPos = textBox.getCursorPos();
                String text = textBox.getText();

                // Check if input is a digit
                if (!Character.isDigit(event.getCharCode())) {
                    textBox.cancelKey();
                    return;
                }

                // Insert new digit in correct position
                String valueString = text.substring(0, cursorPos)
                        + event.getCharCode()
                        + text.substring(cursorPos, text.length());
                int newValue = Integer.parseInt(valueString);

                // Check if input is within bounds
                if (newValue > max || newValue < min) {
                    textBox.cancelKey();
                    Window.alert(ModelingToolWidget.getMessages().sizeMax()
                            + " " + Workflow.MAX_SIZE + "!");
                    return;
                }
                value = newValue;
            }
        });

        add(textBox);
    }

    private void createButtons() {
        upButton.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                raiser.run();
                raiser.scheduleRepeating(delay);
            }
        });
        upButton.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                raiser.cancel();
            }
        });
        buttons.add(upButton);

        downButton.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                lowerer.run();
                lowerer.scheduleRepeating(delay);
            }
        });
        downButton.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                lowerer.cancel();
            }
        });
        buttons.add(downButton);

        add(buttons);
    }

    private void createTimers() {
        raiser = new Timer() {
            @Override
            public void run() {
                if (value + STEPSIZE < max) {
                    value = value + STEPSIZE;
                } else {
                    value = max;
                }
                updateTextBox();
            }
        };

        lowerer = new Timer() {
            @Override
            public void run() {
                if (value - STEPSIZE > min) {
                    value = value - STEPSIZE;
                } else {
                    value = min;
                }
                updateTextBox();
            }
        };
    }

    /**
     * Updates the textbox to show the current value.
     */
    private void updateTextBox() {
        textBox.setText(value + "");
        if (value == max) {
            raiser.cancel();
        } else if (value == min) {
            lowerer.cancel();
        }
    }

    /**
     * Gets the current value of the spinner.
     * 
     * @return the integer value
     */
    public int getValue() {
        return value;
    }
}

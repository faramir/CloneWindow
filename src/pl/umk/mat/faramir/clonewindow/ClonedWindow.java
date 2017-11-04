/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import pl.umk.mat.faramir.clonewindow.win32.Constants;
import pl.umk.mat.faramir.clonewindow.win32.GDI32;
import pl.umk.mat.faramir.clonewindow.win32.User32;

/**
 *
 * @author faramir
 */
final public class ClonedWindow extends JFrame {

    private final HWND sourceHandle;
    private Point initialPoint;
    private Timer refreshTimer;
    private HWND outputHandle;

    public ClonedWindow(HWND sourceHandle, int refreshTime) {
        this.sourceHandle = sourceHandle;
        refreshTimer = new Timer(refreshTime, evt -> ClonedWindow.this.repaint());
        refreshTimer.setInitialDelay(0);

        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                initialPoint = evt.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                initialPoint = null;
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                if (initialPoint == null) {
                    return;
                }

                int deltaX = evt.getX() - initialPoint.x;
                int deltaY = evt.getY() - initialPoint.y;

                int newX = getLocation().x + deltaX;
                int newY = getLocation().y + deltaY;
                setLocation(newX, newY);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                refreshTimer.stop();
                dispose();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(sourceHandle, rect);
        if (rect.right - rect.left == 0) {
            dispose();
            return;
        }

        Dimension sourceSize = new Dimension(rect.right - rect.left, rect.bottom - rect.top);
        Dimension cloneSize = this.getSize();
        if (!cloneSize.equals(sourceSize)) {
            this.setSize(sourceSize);
        }

        char[] captionArray = new char[User32.INSTANCE.GetWindowTextLength(sourceHandle) + 1];
        int captionLength = User32.INSTANCE.GetWindowText(sourceHandle, captionArray, captionArray.length);
        String caption = new String(captionArray, 0, captionLength);
        this.setTitle(caption + " [cloned]");

        HDC outputHDC = User32.INSTANCE.GetWindowDC(outputHandle);
        User32.INSTANCE.PrintWindow(sourceHandle, outputHDC, 0);

//        HDC sourceHDC = User32.INSTANCE.GetDC(sourceHandle);
//        WINDOWINFO wi = new WinUser.WINDOWINFO();
//        User32.INSTANCE.GetWindowInfo(sourceHandle, wi);
//
//        GDI32.INSTANCE.BitBlt(outputHDC, 0, 0, sourceSize.width, sourceSize.height,
//                sourceHDC, 0, 0, Constants.SRCCOPY);
//
//        User32.INSTANCE.ReleaseDC(sourceHandle, sourceHDC);

        User32.INSTANCE.ReleaseDC(outputHandle, outputHDC);
    }

    void cloneWindow() {
        refreshTimer.start();
        setVisible(true);

        outputHandle = new HWND(Native.getWindowPointer(this));
        int windowStyle = User32.INSTANCE.GetWindowLong(sourceHandle, Constants.GWL_STYLE);
        setResizable((windowStyle & Constants.WS_SIZEBOX) != 0);
    }
}

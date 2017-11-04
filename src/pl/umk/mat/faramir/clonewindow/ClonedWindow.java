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
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI.ICONINFO;
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
import pl.umk.mat.faramir.clonewindow.win32.Constants.CURSORINFO;
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

        /* for drag&drop of entire window */
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
        /* get source window size */
        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(sourceHandle, rect);
        if (rect.right - rect.left == 0) {
            dispose();
            return;
        }

        /* calculate dimension and if necessary change output window size */
        Dimension sourceSize = new Dimension(rect.right - rect.left, rect.bottom - rect.top);
        if (!getSize().equals(sourceSize)) {
            setPreferredSize(sourceSize);
            setMinimumSize(sourceSize);
            setMaximumSize(sourceSize);
            setSize(sourceSize);
            revalidate();
        }


        /* get title of source window */
        char[] captionArray = new char[User32.INSTANCE.GetWindowTextLength(sourceHandle) + 1];
        int captionLength = User32.INSTANCE.GetWindowText(sourceHandle, captionArray, captionArray.length);
        String caption = new String(captionArray, 0, captionLength);
        setTitle(caption + " [cloned]");

        /* get full window (not only "client"; with titlebar) DC - for painting */
        HDC outputHDC = User32.INSTANCE.GetWindowDC(outputHandle);
        try {
            /* copy from source window to output DC with GPU rendered*/
            User32.INSTANCE.PrintWindow(sourceHandle, outputHDC, Constants.PW_RENDERFULLCONTENT);

            /* draw also cursor */
            CURSORINFO cursorInfo = new CURSORINFO();
            cursorInfo.cbSize = cursorInfo.size();
            User32.INSTANCE.GetCursorInfo(cursorInfo);

            if ((cursorInfo.flags & Constants.CURSOR_SHOWING) != 0) {
                ICONINFO iconInfo = new ICONINFO();
                User32.INSTANCE.GetIconInfo(cursorInfo.hCursor, iconInfo);

                User32.INSTANCE.DrawIcon(
                        outputHDC,
                        cursorInfo.ptScreenPos.x - rect.left - iconInfo.xHotspot,
                        cursorInfo.ptScreenPos.y - rect.top - iconInfo.yHotspot,
                        cursorInfo.hCursor);

            }

        } finally {
            User32.INSTANCE.ReleaseDC(outputHandle, outputHDC);
        }

//        HDC outputClientHDC = User32.INSTANCE.GetDC(outputHandle);
//        try {
//            CURSORINFO cursorInfo = new CURSORINFO();
//            cursorInfo.cbSize = cursorInfo.size();
//            User32.INSTANCE.GetCursorInfo(cursorInfo);
//
//            if ((cursorInfo.flags & Constants.CURSOR_SHOWING) != 0) {
//                ICONINFO iconInfo = new ICONINFO();
//                User32.INSTANCE.GetIconInfo(cursorInfo.hCursor, iconInfo);
//
//                POINT p = new POINT();
//                User32.INSTANCE.GetCursorPos(p);
//                User32.INSTANCE.ScreenToClient(sourceHandle, p);
//                User32.INSTANCE.DrawIcon(
//                        outputClientHDC,
//                        p.x - iconInfo.xHotspot, p.y - iconInfo.yHotspot,
//                        cursorInfo.hCursor);
//
//            }
//        } finally {
//            User32.INSTANCE.ReleaseDC(outputHandle, outputClientHDC);
//        }
    }

    void cloneWindow() {
        refreshTimer.start();
        setVisible(true);

        /* get handle of this window */
        outputHandle = new HWND(Native.getWindowPointer(this));

        /* check source style (is resizeable) and set it for output window */
        int sourceWindowStyle = User32.INSTANCE.GetWindowLong(sourceHandle, Constants.GWL_STYLE);
        setResizable((sourceWindowStyle & Constants.WS_SIZEBOX) != 0);

        /* disable output window maximize button on titlebar */
        int outputWindowStyle = User32.INSTANCE.GetWindowLong(outputHandle, Constants.GWL_STYLE);
        User32.INSTANCE.SetWindowLong(outputHandle, Constants.GWL_STYLE, outputWindowStyle & ~Constants.WS_MAXIMIZEBOX);
    }
}

/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HBRUSH;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI.ICONINFO;
import com.sun.jna.ptr.IntByReference;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import pl.umk.mat.faramir.clonewindow.win32.Constants;
import pl.umk.mat.faramir.clonewindow.win32.GDI32;
import pl.umk.mat.faramir.clonewindow.win32.Shcore;
import pl.umk.mat.faramir.clonewindow.win32.Structures.CURSORINFO;
import pl.umk.mat.faramir.clonewindow.win32.User32;

/**
 *
 * @author faramir
 */
final public class ClonedWindow extends JFrame {

//    private static final HBRUSH NULL_HBRUSH = new HBRUSH(Pointer.NULL);
    private final int dpiAwareness;
    private final Dimension mousePointerSystemSize;
    private final HWND sourceHandle;
    private HWND outputHandle;
    private Point dragInitialPoint;
    private Dimension outputWindowSize;
    private boolean isMinimized;
    private Dimension previousSourceSize;
    private int previousOutputDpi;
    private int previousSourceDpi;
    private final ScheduledExecutorService refreshExecutor;

    public ClonedWindow(WindowHandleItem source, int refreshTime) {
        IntByReference awareness = new IntByReference(-1);
        Shcore.INSTANCE.GetProcessDpiAwareness(null, awareness);
        dpiAwareness = awareness.getValue();

        int mousePointerWidth = User32.INSTANCE.GetSystemMetrics(User32.SM_CXICON);
        int mousePointerHeight = User32.INSTANCE.GetSystemMetrics(User32.SM_CYICON);
        mousePointerSystemSize = new Dimension(mousePointerWidth, mousePointerHeight);

        this.sourceHandle = source.hWnd();
        refreshExecutor = Executors.newScheduledThreadPool(1);
        refreshExecutor.scheduleWithFixedDelay(() -> {
            boolean isCurrentlyMinimized = User32.INSTANCE.IsIconic(sourceHandle);

            if (isCurrentlyMinimized != isMinimized) {
                isMinimized = isCurrentlyMinimized;
                setState(isMinimized ? ICONIFIED : NORMAL);
            }

            ClonedWindow.this.repaint();
        }, 0, refreshTime, TimeUnit.MILLISECONDS);

        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        /* for drag&drop of entire window */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                dragInitialPoint = evt.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                dragInitialPoint = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                if (dragInitialPoint == null) {
                    return;
                }

                int deltaX = evt.getX() - dragInitialPoint.x;
                int deltaY = evt.getY() - dragInitialPoint.y;

                int newX = getLocation().x + deltaX;
                int newY = getLocation().y + deltaY;
                setLocation(newX, newY);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                refreshExecutor.shutdown();
                dispose();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                toFront();
                setAlwaysOnTop(true);
                setAlwaysOnTop(false);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!getSize().equals(outputWindowSize)) {
                    setPreferredSize(outputWindowSize);
                    setMinimumSize(outputWindowSize);
                    setMaximumSize(outputWindowSize);
                    setSize(outputWindowSize);
                    revalidate();
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        /* get source window size */
        RECT sourceRect = new RECT();
        if (!User32.INSTANCE.GetWindowRect(sourceHandle, sourceRect)) {
            dispose();
            return;
        }

        /* calculate scale */
        int sourceDpi = User32.INSTANCE.GetDpiForWindow(sourceHandle);
        int outputDpi = User32.INSTANCE.GetDpiForWindow(outputHandle);

        /* calculate dimensions */
        Function<POINT, POINT> mousePointerPositionTransform;
        Dimension sourceSize = new Dimension(sourceRect.right - sourceRect.left, sourceRect.bottom - sourceRect.top);
        Dimension bitmapSize;
        Dimension outputSize;
        Dimension mousePointerSize;
        if (dpiAwareness == Shcore.PROCESS_PER_MONITOR_DPI_AWARE) {
            bitmapSize = sourceSize;

            double sizeScale = 96.0 / sourceDpi;
            outputWindowSize = new Dimension((int) (sourceSize.width * sizeScale), (int) (sourceSize.height * sizeScale));

            double outputSizeScale = (double) outputDpi / sourceDpi;
            outputSize = new Dimension((int) (sourceSize.width * outputSizeScale), (int) (sourceSize.height * outputSizeScale));
            mousePointerPositionTransform = point -> new POINT((int) (point.x * outputSizeScale), (int) (point.y * outputSizeScale));

            double outputScale = outputDpi / 96.0;
            mousePointerSize = new Dimension((int) (mousePointerSystemSize.width * outputScale), (int) (mousePointerSystemSize.height * outputScale));
        } else {
            outputWindowSize = sourceSize;
            outputSize = sourceSize;
            mousePointerPositionTransform = point -> point;
            mousePointerSize = mousePointerSystemSize;

            double sourceScale = (double) sourceDpi / outputDpi;
            bitmapSize = new Dimension((int) (sourceSize.width * sourceScale), (int) (sourceSize.height * sourceScale));
        }

        /* if necessary change output window size */
        if (!previousSourceSize.equals(sourceSize)
                || outputDpi != previousOutputDpi || sourceDpi != previousSourceDpi) {
            previousSourceSize = sourceSize;
            previousOutputDpi = outputDpi;
            previousSourceDpi = sourceDpi;

            setPreferredSize(outputWindowSize);
            setMinimumSize(outputWindowSize);
            setMaximumSize(outputWindowSize);
            setSize(outputWindowSize);
            revalidate();
        }

        /* get title of source window */
        char[] captionArray = new char[User32.INSTANCE.GetWindowTextLength(sourceHandle) + 1];
        int captionLength = User32.INSTANCE.GetWindowText(sourceHandle, captionArray, captionArray.length);
        String caption = new String(captionArray, 0, captionLength);
        setTitle(caption + " [cloned]");

        /**
         * * copy window content **
         */
        /* get full window (not only "client"; with titlebar) DC - for painting */
        HDC sourceHDC = User32.INSTANCE.GetWindowDC(sourceHandle);

        /* create temporary DC */
        HDC memDC = GDI32.INSTANCE.CreateCompatibleDC(sourceHDC);
        HBITMAP memBM = GDI32.INSTANCE.CreateCompatibleBitmap(sourceHDC, bitmapSize.width, bitmapSize.height);
        GDI32.INSTANCE.SelectObject(memDC, memBM);

        /* copy from source window to temporary DC with GPU rendered*/
        User32.INSTANCE.PrintWindow(sourceHandle, memDC, Constants.PW_RENDERFULLCONTENT);

        /* copy from temporary DC to output DC */
        HDC outputHDC = User32.INSTANCE.GetWindowDC(outputHandle);
        GDI32.INSTANCE.SetStretchBltMode(outputHDC, Constants.HALFTONE);
        GDI32.INSTANCE.SetBrushOrgEx(outputHDC, 0, 0, null);
        GDI32.INSTANCE.StretchBlt(outputHDC, 0, 0, outputSize.width, outputSize.height,
                memDC, 0, 0, bitmapSize.width, bitmapSize.height,
                Constants.SRCCOPY);

        GDI32.INSTANCE.DeleteObject(memBM);
        GDI32.INSTANCE.DeleteObject(memDC);

        /* draw also cursor */
        CURSORINFO cursorInfo = new CURSORINFO();
        cursorInfo.cbSize = cursorInfo.size();
        User32.INSTANCE.GetCursorInfo(cursorInfo);

        /* only if it is showing */
        if ((cursorInfo.flags & Constants.CURSOR_SHOWING) != 0) {
            HWND mouseWindow = User32.INSTANCE.WindowFromPoint(
                    new POINT.ByValue(cursorInfo.ptScreenPos.getPointer()));

            /* and if cursor is above visible part of cloned window */
            if (sourceHandle.equals(mouseWindow)) {
                ICONINFO iconInfo = new ICONINFO();
                User32.INSTANCE.GetIconInfo(cursorInfo.hCursor, iconInfo);
                POINT mousePointerPos = mousePointerPositionTransform.apply(
                        new POINT(cursorInfo.ptScreenPos.x - sourceRect.left - iconInfo.xHotspot,
                                cursorInfo.ptScreenPos.y - sourceRect.top - iconInfo.yHotspot));

                User32.INSTANCE.DrawIconEx(outputHDC,
                        mousePointerPos.x,
                        mousePointerPos.y,
                        cursorInfo.hCursor,
                        mousePointerSize.width,
                        mousePointerSize.height,
                        0,
                        null,
                        Constants.DI_NORMAL | Constants.DI_COMPAT);

            }
        }

        User32.INSTANCE.ReleaseDC(outputHandle, outputHDC);
    }

    void cloneWindow() {
        /* check source style (is resizeable) and set it for output window */
        int sourceWindowStyle = User32.INSTANCE.GetWindowLong(sourceHandle, Constants.GWL_STYLE);
        setResizable((sourceWindowStyle & Constants.WS_SIZEBOX) != 0);

        /* set minimized state of the clone */
        isMinimized = (sourceWindowStyle & Constants.WS_ICONIC) != 0;
        setState(isMinimized ? ICONIFIED : NORMAL);

        /* dummy value for size - not to have NullPointerException */
        outputWindowSize = new Dimension(0, 0);
        previousSourceSize = new Dimension(0, 0);

        /* show window to get handle */
        setVisible(true);

        /* get handle of this window */
        outputHandle = new HWND(Native.getWindowPointer(this));

        /* disable output window maximize button on titlebar */
        int outputWindowStyle = User32.INSTANCE.GetWindowLong(outputHandle, Constants.GWL_STYLE);
        User32.INSTANCE.SetWindowLong(outputHandle, Constants.GWL_STYLE, outputWindowStyle & ~Constants.WS_MAXIMIZEBOX);
    }

    public HWND getOutputHandle() {
        return outputHandle;
    }
}

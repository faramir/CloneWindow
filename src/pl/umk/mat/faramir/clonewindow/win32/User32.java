/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author faramir
 */
public interface User32 extends com.sun.jna.platform.win32.User32 {

    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    HDC GetWindowDC(HWND hWnd);

    boolean PrintWindow(HWND hWnd, HDC hDC, int nFlags);
}
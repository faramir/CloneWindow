/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow;

import com.sun.jna.platform.win32.WinDef.HWND;

/**
 *
 * @author faramir
 */
public class WindowHandleItem {

    private final String name;
    private final HWND hWnd;

    public WindowHandleItem(String name, HWND hWnd) {
        this.name = name;
        this.hWnd = hWnd;
    }

    public HWND hWnd() {
        return hWnd;
    }

    @Override
    public String toString() {
        return name;
    }

}

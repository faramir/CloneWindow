/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow;

import com.sun.jna.platform.win32.WinDef.HWND;
import java.util.Objects;

/**
 *
 * @author faramir
 */
public class WindowHandleItem implements Comparable<WindowHandleItem> {

    private final String windowName;
    private final HWND hWnd;
    private final int processId;
    private final String exeName;

    public WindowHandleItem(HWND hWnd, String name, int processId, String exeName) {
        this.hWnd = hWnd;
        this.windowName = name;
        this.processId = processId;
        this.exeName = exeName;

    }

    public HWND hWnd() {
        return hWnd;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", processId, windowName);
    }

    @Override
    public int compareTo(WindowHandleItem o) {
        if (this.processId == o.processId) {
            return this.hWnd.getPointer().toString().compareTo(o.hWnd.getPointer().toString());
        }
        return this.processId - o.processId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.windowName);
        hash = 53 * hash + Objects.hashCode(this.hWnd);
        hash = 53 * hash + this.processId;
        hash = 53 * hash + Objects.hashCode(this.exeName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WindowHandleItem other = (WindowHandleItem) obj;
        if (this.processId != other.processId) {
            return false;
        }
        if (!Objects.equals(this.windowName, other.windowName)) {
            return false;
        }
        if (!Objects.equals(this.exeName, other.exeName)) {
            return false;
        }
        return Objects.equals(this.hWnd, other.hWnd);
    }

   

    

}

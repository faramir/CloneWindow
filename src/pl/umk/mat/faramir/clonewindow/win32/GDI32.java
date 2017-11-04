/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author faramir
 */
public interface GDI32 extends com.sun.jna.platform.win32.GDI32 {

    GDI32 INSTANCE = (GDI32) Native.loadLibrary("gdi32", GDI32.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean BitBlt(HDC hdcDest, int nXDest, int nYDest, int nWidth, int nHeight, HDC hdcSrc, int nXSrc, int nYSrc, int dwRop);
}

/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow.win32;

/**
 *
 * @author faramir
 */
public interface Constants extends com.sun.jna.platform.win32.WinUser {

    final int WS_OVERLAPPED = 0x00000000;
    final int WS_CLIPSIBLINGS = 0x04000000;
    final int WS_CLIPCHILDREN = 0x02000000;
    final int WS_DISABLED = 0x08000000;
    final int WS_CAPTION = 0x00C00000;
    final int WS_BORDER = 0x00800000;
    final int WS_DLGFRAME = 0x00400000;
    final int WS_VSCROLL = 0x00200000;
    final int WS_HSCROLL = 0x00100000;
    final int WS_SYSMENU = 0x00080000;
    final int WS_THICKFRAME = 0x00040000;
    final int WS_MINIMIZEBOX = 0x00020000;
    final int WS_MAXIMIZEBOX = 0x00010000;
    final int WS_GROUP = 0x00020000;
    final int WS_TABSTOP = 0x00010000;
    final int WS_SIZEBOX = 0x00040000;
    
    final int PW_CLIENTONLY = 0x00000001;
    
}

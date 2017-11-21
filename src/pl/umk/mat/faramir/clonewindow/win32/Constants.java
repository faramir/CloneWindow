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

    final int PW_CLIENTONLY = 0x00000001;
    final int PW_RENDERFULLCONTENT = 0x00000002;

    final int SRCCOPY = 0x00CC0020;
    final int SRCPAINT = 0x00EE0086;
    final int SRCAND = 0x008800C6;
    final int SRCINVERT = 0x00660046;
    final int SRCERASE = 0x00440328;
    final int NOTSRCCOPY = 0x00330008;
    final int NOTSRCERASE = 0x001100A6;
    final int MERGECOPY = 0x00C000CA;
    final int MERGEPAINT = 0x00BB0226;
    final int PATCOPY = 0x00F00021;
    final int PATPAINT = 0x00FB0A09;
    final int PATINVERT = 0x005A0049;
    final int DSTINVERT = 0x00550009;
    final int BLACKNESS = 0x00000042;
    final int WHITENESS = 0x00FF0062;
    final int CAPTUREBLT = 0x40000000;
    final int CURSOR_SHOWING = 0x00000001;

    final int BLACKONWHITE = 0x00000001;
    final int WHITEONBLACK = 0x00000002;
    final int COLORONCOLOR = 0x00000003;
    final int HALFTONE = 0x00000004;
    final int MAXSTRETCHBLTMODE = 0x00000004;

    final int DI_COMPAT = 0x0004;
    final int DI_DEFAULTSIZE = 0x0008;
    final int DI_IMAGE = 0x0002;
    final int DI_MASK = 0x0001;
    final int DI_NOMIRROR = 0x0010;
    final int DI_NORMAL = 0x0003;
}

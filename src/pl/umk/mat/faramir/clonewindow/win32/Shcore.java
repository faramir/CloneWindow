/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.faramir.clonewindow.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 *
 * @author faramir
 */
public interface Shcore extends StdCallLibrary {

    final Shcore INSTANCE = (Shcore) Native.loadLibrary("Shcore", Shcore.class);

    HRESULT GetProcessDpiAwareness(HANDLE processId, IntByReference value);

    final int PROCESS_DPI_UNAWARE = 0;
    final int PROCESS_SYSTEM_DPI_AWARE = 1;
    final int PROCESS_PER_MONITOR_DPI_AWARE = 2;
}

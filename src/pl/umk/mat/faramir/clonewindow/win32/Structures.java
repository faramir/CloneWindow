/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow.win32;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author faramir
 */
public interface Structures {

    public static class CURSORINFO extends Structure {

        public int cbSize;
        public int flags;
        public WinDef.HCURSOR hCursor;
        public WinDef.POINT ptScreenPos;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{"cbSize", "flags", "hCursor", "ptScreenPos"});
        }
    }

}

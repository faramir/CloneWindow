/*
 * Copyright (c) 2017, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.faramir.clonewindow;

import pl.umk.mat.faramir.clonewindow.win32.User32;
import pl.umk.mat.faramir.clonewindow.win32.Constants;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author faramir
 */
public class Main extends javax.swing.JFrame {

    private final List<HWND> clonedWindowHandles = new ArrayList<>();

    /**
     * Creates new form CloneWindow
     */
    public Main() {
        initComponents();

        refreshTimeSliderStateChanged(new ChangeEvent(refreshTimeSlider));
        windowCaptionComboBoxPopupMenuWillBecomeVisible(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        windowCaptionLabel = new javax.swing.JLabel();
        windowCaptionComboBox = new javax.swing.JComboBox<>();
        refreshTimeLabel = new javax.swing.JLabel();
        refreshTimeSlider = new javax.swing.JSlider();
        refreshTimeValueLabel = new javax.swing.JLabel();
        cloneWindowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Clone Window");
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setName("mainFrame"); // NOI18N
        setResizable(false);

        windowCaptionLabel.setLabelFor(windowCaptionComboBox);
        windowCaptionLabel.setText("Window caption:");

        windowCaptionComboBox.setName(""); // NOI18N
        windowCaptionComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                windowCaptionComboBoxPopupMenuWillBecomeVisible(evt);
            }
        });

        refreshTimeLabel.setLabelFor(refreshTimeSlider);
        refreshTimeLabel.setText("Refresh time:");

        refreshTimeSlider.setMajorTickSpacing(100);
        refreshTimeSlider.setMaximum(500);
        refreshTimeSlider.setMinorTickSpacing(10);
        refreshTimeSlider.setPaintLabels(true);
        refreshTimeSlider.setPaintTicks(true);
        refreshTimeSlider.setSnapToTicks(true);
        refreshTimeSlider.setNextFocusableComponent(cloneWindowButton);
        refreshTimeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                refreshTimeSliderStateChanged(evt);
            }
        });

        refreshTimeValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        refreshTimeValueLabel.setLabelFor(refreshTimeSlider);
        refreshTimeValueLabel.setText("X ms");

        cloneWindowButton.setText("Clone window");
        cloneWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cloneWindowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(windowCaptionLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshTimeValueLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(refreshTimeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                            .addComponent(windowCaptionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cloneWindowButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowCaptionComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(windowCaptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshTimeLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refreshTimeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(refreshTimeValueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cloneWindowButton)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshTimeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_refreshTimeSliderStateChanged
        javax.swing.JSlider source = (javax.swing.JSlider) evt.getSource();
        refreshTimeValueLabel.setText(source.getValue() + " ms");
    }//GEN-LAST:event_refreshTimeSliderStateChanged

    private void cloneWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cloneWindowButtonActionPerformed
        WindowHandleItem windowHandle = (WindowHandleItem) windowCaptionComboBox.getSelectedItem();
        HWND sourcePointer = windowHandle.hWnd();

        ClonedWindow clonedFrame = new ClonedWindow(sourcePointer, refreshTimeSlider.getValue());
        clonedFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                clonedWindowHandles.remove(clonedFrame.getOutputHandle());
            }
        });
        clonedFrame.cloneWindow();
        clonedWindowHandles.add(clonedFrame.getOutputHandle());
    }//GEN-LAST:event_cloneWindowButtonActionPerformed

    private void windowCaptionComboBoxPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_windowCaptionComboBoxPopupMenuWillBecomeVisible
        HWND mainWindow = new HWND(Native.getWindowPointer(this));

        List<WindowHandleItem> comboBoxItems = new ArrayList<>();
        User32.INSTANCE.EnumWindows((hWnd, arg) -> {
            if (mainWindow.equals(hWnd) || clonedWindowHandles.contains(hWnd)) {
                return true;
            }

            int windowStyle = User32.INSTANCE.GetWindowLong(hWnd, Constants.GWL_STYLE);

            char[] windowNameArray = new char[255];
            int windowNameLength = User32.INSTANCE.GetWindowText(hWnd, windowNameArray, windowNameArray.length);
            String windowName = new String(windowNameArray, 0, windowNameLength);

            if ((windowStyle & Constants.WS_POPUP) != 0
                    || (windowStyle & Constants.WS_CAPTION) == 0
                    || (windowStyle & Constants.WS_VISIBLE) == 0
                    || windowNameLength == 0) {
                return true;
            }

            IntByReference processId = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hWnd, processId);
            HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, processId.getValue());
            char[] exeNameArray = new char[1024];
            IntByReference exeNameLength = new IntByReference(exeNameArray.length);
            Kernel32.INSTANCE.QueryFullProcessImageName(processHandle, WinNT.PROCESS_NAME_NATIVE, exeNameArray, exeNameLength);
            String exeName = new String(exeNameArray, 0, exeNameLength.getValue());
            exeName = exeName.substring(exeName.lastIndexOf("\\") + 1);

            WindowHandleItem windowHandle = new WindowHandleItem(hWnd, windowName, processId.getValue(), exeName);
            comboBoxItems.add(windowHandle);
            return true;
        }, Pointer.NULL);

        WindowHandleItem selectedItem = (WindowHandleItem) windowCaptionComboBox.getSelectedItem();
        windowCaptionComboBox.removeAllItems();
        comboBoxItems.stream().sorted().forEach(windowCaptionComboBox::addItem);
        if (selectedItem == null) {
            windowCaptionComboBox.setSelectedIndex(0);
        } else {
            windowCaptionComboBox.setSelectedItem(selectedItem);
        }
    }//GEN-LAST:event_windowCaptionComboBoxPopupMenuWillBecomeVisible

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cloneWindowButton;
    private javax.swing.JLabel refreshTimeLabel;
    private javax.swing.JSlider refreshTimeSlider;
    private javax.swing.JLabel refreshTimeValueLabel;
    private javax.swing.JComboBox<pl.umk.mat.faramir.clonewindow.WindowHandleItem> windowCaptionComboBox;
    private javax.swing.JLabel windowCaptionLabel;
    // End of variables declaration//GEN-END:variables
}

/*
 * This file is part of an application developed by Open Education Inc.
 * The source code of the entire project is the exclusive property of
 * Open Education Inc. If you have received this file in error please
 * inform the project leader at altekrusejason@gmail.com to report where
 * the file was found and delete it immediately. 
 */

package gui;

import java.awt.datatransfer.*;
import java.awt.*;

public class OCclipboard implements ClipboardOwner {
    
    public void CopyToClipboard(String s){
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
          try {
             sm.checkSystemClipboardAccess();
             }
          catch (Exception e) {e.printStackTrace();}
          }
        Toolkit tk = Toolkit.getDefaultToolkit();
        StringSelection st = 
             new StringSelection(s);
        Clipboard cp = tk.getSystemClipboard();
        cp.setContents(st, this);
    }

    @Override
    public void lostOwnership(Clipboard clip, Transferable tr) { 
       System.out.println("Lost Clipboard Ownership?!?");
    }
}
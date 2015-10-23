/*
   This file is part of OpenNotebook.

   OpenNotebook is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OpenNotebook is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
    along with OpenNotebook.  If not, see <http://www.gnu.org/licenses/>.
 */

package doc_gui;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JMenuBar;

public interface TopLevelContainerOld {

public GlassPane getGlassPanel();

public Point getLocationOnScreen();

public Container getContentPane();

public JMenuBar getJMenuBar();

public int getHeight();

public int getWidth();
}
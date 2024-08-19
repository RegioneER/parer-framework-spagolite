/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystemUtil {

    public static final String[] EXLUDE_PATH = { ".svn", ".git" };

    /**
     * Verifica se il file passato appartiene alla lista di esclusione passata
     *
     * @param file
     *            da verificare
     * @param excludePath
     *            path da escludere
     *
     * @return true/false
     */
    public static final boolean isToExclude(File file, String[] excludePath) {
        for (int i = 0; i < excludePath.length; i++) {
            if (excludePath[i].equalsIgnoreCase(file.getName()))
                return true;
        }

        return false;
    }

    /**
     * Verifica se il file passato appartiene alla lista di esclusione di defaut
     *
     * @param file
     *            da verificare
     *
     * @return true/false
     */
    public static final boolean isToExclude(File file) {
        return isToExclude(file, EXLUDE_PATH);
    }

    /**
     *
     * @param list
     *            lista file
     * @param file
     *            da verificare
     * @param filter
     *            filtro da applicare
     * @param excludePath
     *            path da escludere
     *
     */
    private static final void getFileList(List list, File file, String filter, String[] excludePath) {
        if (isToExclude(file, excludePath))
            return;

        if (file.isFile()) {
            if (file.getName().indexOf(filter) >= 0)
                list.add(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                getFileList(list, files[i], filter, excludePath);
            }
        }
    }

    /**
     * Ritorna l'elenco dei file di un determinato tipo a partire dalla directory passata
     *
     * @param directory
     *            da verificare
     * @param filter
     *            filtro da applicare
     * @param excludePath
     *            path da escludere
     *
     * @return lista file
     */
    public static final List<File> getFileList(String directory, String filter, String[] excludePath) {
        List<File> list = new ArrayList<File>();
        getFileList(list, new File(directory), filter, excludePath);

        return list;
    }
}

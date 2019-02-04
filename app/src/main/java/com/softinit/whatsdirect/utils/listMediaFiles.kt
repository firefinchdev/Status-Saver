package com.softinit.whatsdirect.utils

import java.io.File

fun listMediaFiles(directory: File): MutableList<File> {
    return directory.listFiles()
        .filter { FileType.isFileImageVideo(it) }
        //Sort with most recently modified (created) file first
        .sortedWith(Comparator {f1, f2 -> if (f1.lastModified() > f2.lastModified()) -1 else 1})
        .toMutableList()
}
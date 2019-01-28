package com.softinit.whatsdirect.utils

import java.io.File
import java.net.URLConnection

object FileType {
    const val TYPE_DIRECTORY = 1
    const val TYPE_IMAGE = 2
    const val TYPE_VIDEO = 3
    const val TYPE_OTHER = 4

    fun getFileType(file: File): Int {
        if (file.isDirectory) return FileType.TYPE_DIRECTORY
        URLConnection.guessContentTypeFromName(file.name).let {
            if (it.startsWith("image")) return FileType.TYPE_IMAGE
            if (it.startsWith("video")) return FileType.TYPE_VIDEO
        }
        return FileType.TYPE_OTHER
    }

    fun isFileImageVideo(file: File): Boolean = getFileType(file).let { it == TYPE_IMAGE || it == TYPE_VIDEO }
    fun isFileImage(file: File): Boolean = getFileType(file).let { it == TYPE_IMAGE }
    fun isFileVideo(file: File): Boolean = getFileType(file).let { it == TYPE_VIDEO }
}


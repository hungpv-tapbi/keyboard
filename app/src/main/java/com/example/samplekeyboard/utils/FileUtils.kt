package com.example.samplekeyboard.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.CountDownLatch

object FileUtils {
    suspend fun deleteRecursively(path: File): Boolean = withContext(Dispatchers.IO) {
        if (path.isDirectory()) {
            val files = path.listFiles()
            if (files != null) {
                for (child in files) {
                    deleteRecursively(child)
                }
            }
        }
        return@withContext path.delete()
    }

    suspend fun deleteFilteredFiles(dir: File, fileNameFilter: FilenameFilter?): Boolean =
        withContext(
            Dispatchers.IO
        ) {
            if (!dir.isDirectory()) {
                return@withContext false
            }
            val files = dir.listFiles(fileNameFilter) ?: return@withContext false
            var hasDeletedAllFiles = true
            for (file in files) {
                if (!deleteRecursively(file)) {
                    hasDeletedAllFiles = false
                }
            }
            return@withContext hasDeletedAllFiles
        }

    /**
     * copy data to file on different thread to avoid NetworkOnMainThreadException
     * still effectively blocking, as we only use small files which are mostly stored locally
     */
    @Throws(IOException::class)
    suspend fun copyContentUriToNewFile(uri: Uri, context: Context, outfile: File) = withContext(
        Dispatchers.IO
    ) {
        val allOk = booleanArrayOf(true)
        val wait = CountDownLatch(1)
        try {
            copyStreamToNewFile(context.contentResolver.openInputStream(uri)!!, outfile)
        } catch (e: IOException) {
            allOk[0] = false
        } finally {
            wait.countDown()
        }
        try {
            wait.await()
        } catch (e: InterruptedException) {
            allOk[0] = false
        }
        if (!allOk[0]) throw IOException("could not copy from uri")
    }

    @Throws(IOException::class)
    suspend fun copyStreamToNewFile(`in`: InputStream, outfile: File) =
        withContext(Dispatchers.IO) {
            val parentFile = outfile.getParentFile()
            if (parentFile == null || (!parentFile.exists() && !parentFile.mkdirs())) {
                throw IOException("could not create parent folder")
            }
            val out = FileOutputStream(outfile)
            copyStreamToOtherStream(`in`, out)
            out.close()
        }

    @Throws(IOException::class)
    suspend fun copyStreamToOtherStream(`in`: InputStream, out: OutputStream) =
        withContext(Dispatchers.IO) {
            val buf = ByteArray(1024)
            var len: Int
            while ((`in`.read(buf).also { len = it }) > 0) {
                out.write(buf, 0, len)
            }
            out.flush()
        }
}
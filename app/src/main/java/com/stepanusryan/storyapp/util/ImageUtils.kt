package com.stepanusryan.storyapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAXIMAL_SIZE = 1_000_000
private const val MAXIMAL_DIMENSION = 2048
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

fun createCustomTempFile(context: Context): File {
    val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    val directory = File(context.cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("STORY_${timeStamp}_", ".jpg", directory)
}

fun getImageUri(context: Context): Uri = FileProvider.getUriForFile(
    context,
    "${context.packageName}.provider",
    createCustomTempFile(context)
)

suspend fun uriToFile(imageUri: Uri, context: Context): File = withContext(Dispatchers.IO) {
    val file = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri)
        ?: throw IOException("Unable to open $imageUri")
    inputStream.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    file
}

suspend fun File.reduceFileImage(): File = withContext(Dispatchers.IO) {
    val file = this@reduceFileImage
    val bitmap = decodeSampledBitmap(file).rotateByExif(file)
    var quality = 100
    var bytes: ByteArray
    do {
        bytes = ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            stream.toByteArray()
        }
        quality -= 5
    } while (bytes.size > MAXIMAL_SIZE && quality > 0)
    file.writeBytes(bytes)
    file
}

private fun decodeSampledBitmap(file: File): Bitmap {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.path, bounds)
    var sampleSize = 1
    while (bounds.outWidth / sampleSize > MAXIMAL_DIMENSION ||
        bounds.outHeight / sampleSize > MAXIMAL_DIMENSION
    ) {
        sampleSize *= 2
    }
    val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    return BitmapFactory.decodeFile(file.path, options)
        ?: throw IOException("Unable to decode image")
}

private fun Bitmap.rotateByExif(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    val degrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> return this
    }
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

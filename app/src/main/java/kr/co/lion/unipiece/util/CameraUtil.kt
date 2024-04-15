package kr.co.lion.unipiece.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class CameraUtil {
    companion object {
        // 촬영된 사진이 저장될 경로 구하는 메서드
        fun Context.getPictureUri(authorities: String): Uri {
            val rootPath = getExternalFilesDir(null).toString()
            val picPath = "$rootPath/tempImage.jpg"
            val file = File(picPath)
            // 사진이 저장된 위치를 관리할 Uri 생성
            return FileProvider.getUriForFile(this, authorities, file)
        }


        // 사진의 회전 각도값 반환 메서드
        fun Context.getDegree(uri: Uri): Int {
            val exifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    ExifInterface(inputStream)
                }
            } else {
                ExifInterface(uri.path!!)
            }

            return exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)?.let { ori ->
                when (ori) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            } ?: 0
        }

        // 회전시키는 메서드
        fun Bitmap.rotate(degree: Float): Bitmap {
            val matrix = Matrix().apply { postRotate(degree) }
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
        }

        // 이미지 사이즈 조정 메서드
        fun Bitmap.resize(targetWidth: Int): Bitmap {
            val ratio = targetWidth.toDouble() / width.toDouble()
            val targetHeight = (height * ratio).toInt()
            return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, false)
        }

        // 이미지뷰의 이미지를 추출해 로컬에 저장
        fun ImageView.saveAsImage(context: Context, fileName: String) {
            val filePath = context.getExternalFilesDir(null).toString()
            val bitmapDrawable = drawable as BitmapDrawable

            // 로컬에 저장할 경로
            val file = File("$filePath/$fileName")
            val fileOutputStream = FileOutputStream(file)

            // 이미지 저장
            bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
    }
}
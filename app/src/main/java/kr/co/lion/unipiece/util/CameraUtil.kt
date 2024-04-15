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
        fun getPictureUri(context: Context, authorities:String): Uri {
            // 촬영한 사진이 저장될 경로
            // 외부 저장소 중에 애플리케이션 영역 경로를 가져온다.
            val rootPath = context.getExternalFilesDir(null).toString()
            // 이미지 파일명을 포함한 경로
            val picPath = "${rootPath}/tempImage.jpg"
            // File 객체 생성
            val file = File(picPath)
            // 사진이 저장된 위치를 관리할 Uri 생성
            val contentUri = FileProvider.getUriForFile(context, authorities, file)

            return contentUri
        }

        // 사진의 회전 각도값 반환 메서드
        fun getDegree(context:Context, uri:Uri) : Int {
            // 사진 정보를 가지고 있는 객체 가져오기
            var exifInterface: ExifInterface? = null


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                val inputStream = context.contentResolver.openInputStream(uri)!!
                exifInterface = ExifInterface(inputStream)
            } else {
                exifInterface = ExifInterface(uri.path!!)
            }

            if(exifInterface != null){
                // 반환할 각도값을 담을 변수
                var degree = 0
                // ExifInterface 객체에서 회전 각도값 가져오기
                val ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

                degree = when(ori){
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }

                return degree
            }

            return 0
        }

        // 회전시키는 메서드
        fun rotateBitmap(bitmap: Bitmap, degree:Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degree)

            // 회전 행렬을 적용하여 회전된 이미지 생성
            val rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)

            return rotateBitmap
        }

        // 이미지 사이즈 조정 메서드
        fun resizeBitmap(bitmap: Bitmap, targetWidth:Int): Bitmap {
            val ratio = targetWidth.toDouble() / bitmap.width.toDouble()
            val targetHeight = (bitmap.height * ratio).toInt()
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)

            return resizedBitmap
        }

        // 이미지뷰의 이미지를 추출해 로컬에 저장
        fun saveImageViewData(context:Context, imageView: ImageView, fileName:String){
            // 외부 저장소까지의 경로를 가져온다.
            val filePath = context.getExternalFilesDir(null).toString()
            // 이미지 뷰에서 BitmapDrawable 객체를 추출한다.
            val bitmapDrawable = imageView.drawable as BitmapDrawable

            // 로컬에 저장할 경로
            val file = File("${filePath}/${fileName}")
            // 스트림 추출
            val fileOutputStream = FileOutputStream(file)
            // 이미지를 저장한다.
            // 첫 번째 : 이미지 데이터 포멧(JPEG, PNG, WEBP_LOSSLESS, WEBP_LOSSY)
            // 두 번째 : 이미지의 퀄리티
            // 세 번째 : 이미지 데이터를 저장할 파일과 연결된 스트림
            bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
    }
}
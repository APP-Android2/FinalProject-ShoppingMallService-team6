package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivityUpdateAuthorBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.viewmodel.UpdateAuthorViewModel
import kr.co.lion.unipiece.ui.infomation.InfoOneActivity
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.saveAsImage
import kr.co.lion.unipiece.util.showSoftInput

class UpdateAuthorActivity : AppCompatActivity() {

    lateinit var activityUpdateAuthorBinding: ActivityUpdateAuthorBinding

    lateinit var fileLauncher: ActivityResultLauncher<Intent>

    val viewModel: UpdateAuthorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUpdateAuthorBinding = ActivityUpdateAuthorBinding.inflate(layoutInflater)
        setContentView(activityUpdateAuthorBinding.root)
        initView()
        settingView()
        settingAlbumLauncher()
    }

    private fun initView(){
        activityUpdateAuthorBinding.apply {
            toolBarUpdate.apply {
                title = "작가 갱신"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
            }
            buttonAuthorUpdate.setOnClickListener {
                val chk = checkEmptyList()
                if (chk){
                    checkImg()
                }
            }

            buttonUpdateGetPicture.setOnClickListener {
                startAlbumLauncher()
            }

            lifecycleScope.launch {
                val authorIdx = intent?.getIntExtra("authorIdx", -1)
                Log.d("test1234", "${authorIdx}")

                val authorInfo = viewModel.getAuthorInfoByAuthorIdx(authorIdx?:-1)

                textUpdateUni.setText(authorInfo?.authorUni)
                textUpdateName.setText(authorInfo?.authorName)
                textUpdateMajor.setText(authorInfo?.authorMajor)

            }

        }
    }

    private fun settingView(){
        activityUpdateAuthorBinding.apply {
            //포커스주기
            showSoftInput(textUpdateUni)

            //에러 해결
            textUpdateUni.addTextChangedListener {
                textUpdateUniLayout.error = null
            }
            textUpdateName.addTextChangedListener {
                textUpdateNameLayout.error = null
            }
            textUpdateMajor.addTextChangedListener {
                textUpdateMajorLayout.error = null
            }
        }
    }

    private fun checkImg(){
        activityUpdateAuthorBinding.apply {
            val file = textAddUpdateFile.text.toString()

            if (file.trim().isEmpty()){
                val dialog = CustomDialog("첨부파일 오류", "첨부파일을 추가해주세요")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        showSoftInput(textAddUpdateFile)
                    }

                    override fun noButtonClick() {

                    }

                })
                dialog.show(supportFragmentManager, "CustomDialog")
            }
        }
    }

    private fun checkEmptyList():Boolean{
        activityUpdateAuthorBinding.apply {
            var emptyList: View? = null

            val uni = textUpdateUni.text.toString()
            val name = textUpdateName.text.toString()
            val major = textUpdateMajor.text.toString()

            if (uni.trim().isEmpty()){
                textUpdateUniLayout.error = "학교를 입력해주세요"
                if (emptyList == null){
                    emptyList = textUpdateUni
                }else{
                    textUpdateUniLayout.error = null
                }
            }

            if (name.trim().isEmpty()){
                textUpdateNameLayout.error = "이름을 입력해주세요"
                if (emptyList == null){
                    emptyList = textUpdateName
                }else{
                    textUpdateNameLayout.error = null
                }
            }

            if (major.trim().isEmpty()){
                textUpdateMajorLayout.error = "학과를 입력해주세요"
                if (emptyList == null){
                    emptyList = textUpdateMajor
                }else{
                    textUpdateMajorLayout.error = null
                }
            }
            if (emptyList != null){
                showSoftInput(emptyList)
                return false
            }else{
                return true
            }
        }
    }

    //첨부파일 런쳐 설정
    private fun settingAlbumLauncher(){
        val contract = ActivityResultContracts.StartActivityForResult()
        fileLauncher = registerForActivityResult(contract){ result ->
            if(result.resultCode == RESULT_OK){
                // 선택한 이미지 경로 데이터 관리하는 Uri 객체 추출
                val uri = result.data?.data
                if(uri != null){
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지 생성할 수 있는 객체 생성
                        val source = ImageDecoder.createSource(this.contentResolver, uri)
                        // Bitmap 생성
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근
                        val cursor = this.contentResolver.query(uri, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지 경로 가져오기
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지 생성
                            BitmapFactory.decodeFile(source)
                        }  else {
                            null
                        }
                    }

                    // 회전 각도값 가져오기
                    val degree = this.getDegree(uri)
                    // 회전 이미지 가져오기
                    val bitmap2 = bitmap?.rotate(degree.toFloat())
                    // 크기를 줄인 이미지 가져오기
                    val bitmap3 = bitmap2?.resize(1024)

                    activityUpdateAuthorBinding.imageViewUpdate.setImageBitmap(bitmap3)
                    activityUpdateAuthorBinding.textAddUpdateFile.setText(bitmap3.toString())
                }
            }
        }
    }
    //첨부파일 런쳐를 실행하는 메서드
    private fun startAlbumLauncher(){
        //사진 가져오기
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        albumIntent.setType("AuthorAdd/*")
        val mimeType = arrayOf("AuthorAdd/*")
        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)

        fileLauncher.launch(albumIntent)
    }
}
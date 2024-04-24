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
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivityUpdateAuthorBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorAddViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.saveAsImage
import kr.co.lion.unipiece.util.showSoftInput

class UpdateAuthorActivity : AppCompatActivity() {

    lateinit var activityUpdateAuthorBinding: ActivityUpdateAuthorBinding

    lateinit var fileLauncher: ActivityResultLauncher<Intent>

    val viewModel: AuthorAddViewModel by viewModels()
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
                textUpdateUniState.setText(authorInfo?.authorUniState)

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
            textUpdateUniState.addTextChangedListener {
                textUpdateUniStateLayout.error = null
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
            }else{
                saveAuthorUpdateInfo()
            }
        }
    }

    private fun checkEmptyList():Boolean{
        activityUpdateAuthorBinding.apply {
            var emptyList: View? = null

            val uni = textUpdateUni.text.toString()
            val name = textUpdateName.text.toString()
            val major = textUpdateMajor.text.toString()
            val uniState = textUpdateUniState.text.toString()

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
            if (uniState.trim().isEmpty()){
                textUpdateUniStateLayout.error = "학적 상태를 입력해주세요"
                if (emptyList == null){
                    emptyList = textUpdateUniState
                }else{
                    textUpdateUniStateLayout.error = null
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

    //저장하기
    private fun saveAuthorUpdateInfo(){
        //서버에서의 첨부 이미지 이름
        activityUpdateAuthorBinding.apply {
            lifecycleScope.launch {
                //첨부파일
                var fileServerName:String? = null

                activityUpdateAuthorBinding.imageViewUpdate.saveAsImage(this@UpdateAuthorActivity, "authorFile")
                fileServerName = "authorFile_${System.currentTimeMillis()}.jpg"

                viewModel.uploadFileByApp(this@UpdateAuthorActivity, "authorFile", fileServerName)

                val getIdx = intent?.getIntExtra("authorIdx", -1)

                val getInfo = viewModel.getAuthorInfoByAuthorIdx(getIdx?:-1)


                //업로드 할 정보를 담아준다
                val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", -1)
                val authorFile = fileServerName
                val authorName = textUpdateName.text.toString()
                val authorMajor = textUpdateMajor.text.toString()
                val authorUni = textUpdateUni.text.toString()
                val authorInfo = ""
                val authorImg = getInfo?.authorImg.toString()
                val authorIdx = getInfo?.authorIdx?:-1
                val authorUniState = textUpdateUniState.text.toString()
                val authorRegisterTime = Timestamp.now()

                viewModel.insertAuthorInfo(userIdx, authorFile, authorName, authorMajor, authorUni, authorInfo, authorImg, false, authorIdx, authorUniState, authorRegisterTime){ sucess->
                    if (sucess){
                        val dialog = CustomDialog("작가 갱신 신청 완료", "작가 갱신이 신청되었습니다\n갱신 완료 시까지 1 ~ 2일 소요됩니다")
                        dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                            override fun okButtonClick() {
                                val newIntent = Intent(this@UpdateAuthorActivity, MainActivity::class.java)
                                newIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(newIntent)
                            }

                            override fun noButtonClick() {
                                val newIntent = Intent(this@UpdateAuthorActivity, MainActivity::class.java)
                                newIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(newIntent)
                            }

                        })
                        dialog.show(supportFragmentManager, "CustomDialog")

                    }
                }
            }
        }
    }
}
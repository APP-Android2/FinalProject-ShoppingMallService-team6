package kr.co.lion.unipiece.ui.author

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentRegisterAuthorBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorAddViewModel
import kr.co.lion.unipiece.ui.author.viewmodel.AuthorInfoViewModel
import kr.co.lion.unipiece.util.AddAuthorFragmentName
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.saveAsImage
import kr.co.lion.unipiece.util.showSoftInput


class RegisterAuthorFragment : Fragment() {

    lateinit var fragmentRegisterAuthorBinding: FragmentRegisterAuthorBinding

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    lateinit var imageLauncher:ActivityResultLauncher<Intent>

    val viewModel: AuthorAddViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentRegisterAuthorBinding = FragmentRegisterAuthorBinding.inflate(layoutInflater)
        initView()
        settingEvent()
        settingView()
        settingImageLauncher()
        settingAlbumLauncher()
        return fragmentRegisterAuthorBinding.root
    }

    //툴바 설정
    private fun initView(){
        fragmentRegisterAuthorBinding.apply {
            toolBarRegister.apply {
                title = "작가 등록"
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(AddAuthorFragmentName.REGISTER_AUTHOR_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                }
            }
        }
    }

    //이벤트 설정
    private fun settingEvent(){
        fragmentRegisterAuthorBinding.apply {
            buttonAuthorRegister.setOnClickListener {
                val chk = checkEmptyList()
                if (chk){
                    checkImg()
                }
            }

            buttonGetPicture.setOnClickListener {
                startImageLauncher()
            }

            buttonGetFile.setOnClickListener {
                startAlbumLauncher()
            }
        }
    }


    private fun settingView(){
        fragmentRegisterAuthorBinding.apply {
            //포커스주기
            requireActivity().showSoftInput(textRegisterUni)

            //에러 해결
            textRegisterUni.addTextChangedListener {
                textRegisterUniLayout.error = null
            }
            textRegisterName.addTextChangedListener {
                textRegisterNameLayout.error = null
            }
            textRegisterMajor.addTextChangedListener {
                textRegisterMajorLayout.error = null
            }
        }
    }




    private fun checkEmptyList():Boolean{
        fragmentRegisterAuthorBinding.apply {
            var emptyList:View? = null

            val uni = textRegisterUni.text.toString()
            val name = textRegisterName.text.toString()
            val major = textRegisterMajor.text.toString()

            if (uni.trim().isEmpty()){
                textRegisterUniLayout.error = "학교를 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterUni
                }else{
                    textRegisterUniLayout.error = null
                }
            }

            if (name.trim().isEmpty()){
                textRegisterNameLayout.error = "이름을 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterName
                }else{
                    textRegisterNameLayout.error = null
                }
            }

            if (major.trim().isEmpty()){
                textRegisterMajorLayout.error = "학과를 입력해주세요"
                if (emptyList == null){
                    emptyList = textRegisterMajor
                }else{
                    textRegisterMajorLayout.error = null
                }
            }
            if (emptyList != null){
                requireActivity().showSoftInput(emptyList)
                return false
            }else{
                return true
            }
        }
    }

    //저장하기
    private fun saveAuthorInfo(){
        //서버에서의 첨부 이미지 이름
        fragmentRegisterAuthorBinding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                //첨부파일
                var fileServerName:String? = null

                fragmentRegisterAuthorBinding.imageView3.saveAsImage(requireActivity(), "authorFile")
                fileServerName = "authorFile_${System.currentTimeMillis()}.jpg"

                viewModel.uploadFileByApp(requireActivity(), "authorFile", fileServerName)

                //프로필 이미지
                var imageServerName:String? = null

                fragmentRegisterAuthorBinding.imageView4.saveAsImage(requireActivity(), "authorImage")
                imageServerName = "authorImage_${System.currentTimeMillis()}.jpg"

                viewModel.uploadImageByApp(requireActivity(), "authorImage", imageServerName)


                //업로드 할 정보를 담아준다
                val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", -1)
                val authorFile = fileServerName
                val authorName = textRegisterName.text.toString()
                val authorMajor = textRegisterMajor.text.toString()
                val authorUni = textRegisterUni.text.toString()
                val authorInfo = ""
                val authorImg = imageServerName
                val authorIdx = -1
                val authorUniState = textRegisterUniState.text.toString()
                val authorRegisterTime = Timestamp.now()

                viewModel.insertAuthorInfo(userIdx, authorFile, authorName, authorMajor, authorUni, authorInfo, authorImg, true, authorIdx, authorUniState, authorRegisterTime){ sucess->
                    if (sucess){
                        val dialog = CustomDialog("작가 등록 신청 완료", "작가 등록이 신청되었습니다\n등록 완료 시까지 1 ~ 2일 소요됩니다")
                        dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                            override fun okButtonClick() {
                                /*val newIntent = Intent(requireActivity(), MainActivity::class.java)
                                newIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(newIntent)*/
                                // 나의 갤러리 fragment로 이동
                                setIntent("MyGalleryFragment")
                            }

                            // 이 버튼을 눌렀을때는 작가 등록에 추가가 안되게 해야할 것 같습니다!
                            override fun noButtonClick() {
                                val newIntent = Intent(requireActivity(), MainActivity::class.java)
                                newIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(newIntent)
                            }

                        })
                        dialog.show(parentFragmentManager, "CustomDialog")

                    }
                }
            }
        }
    }

    fun setIntent(name: String) {

        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            // MainActivity가 이미 실행 중인 경우 해당 인스턴스를 재사용합니다.
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        intent.putExtra(name, true)
        requireActivity().finish()
        startActivity(intent)
    }

    //이미지 체크
    private fun checkImg(){
        fragmentRegisterAuthorBinding.apply {
            val file = textAddFile.text.toString()
            val image = textAddImage.text.toString()
            if (file.trim().isEmpty()){
                val dialog = CustomDialog("첨부파일 오류", "첨부파일을 추가해주세요")
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                    override fun okButtonClick() {
                        requireActivity().showSoftInput(textAddFile)
                    }

                    override fun noButtonClick() {

                    }

                })
                dialog.show(parentFragmentManager, "CustomDialog")
            }else{
                if (image.trim().isEmpty()){
                    val dialog = CustomDialog("프로필 오류", "프로필 이미지를 추가해주세요")
                    dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener{
                        override fun okButtonClick() {
                            requireActivity().showSoftInput(textAddImage)
                        }

                        override fun noButtonClick() {

                        }

                    })
                    dialog.show(parentFragmentManager, "CustomDialog")
                }else{
                    saveAuthorInfo()
                }
            }
        }
    }


    //첨부파일 런쳐 설정
    private fun settingAlbumLauncher(){
        val contract = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract){ result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지 경로 데이터 관리하는 Uri 객체 추출
                val uri = result.data?.data
                if(uri != null){
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지 생성할 수 있는 객체 생성
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        // Bitmap 생성
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근
                        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
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
                    val degree = requireActivity().getDegree(uri)
                    // 회전 이미지 가져오기
                    val bitmap2 = bitmap?.rotate(degree.toFloat())
                    // 크기를 줄인 이미지 가져오기
                    val bitmap3 = bitmap2?.resize(1024)

                    fragmentRegisterAuthorBinding.imageView3.setImageBitmap(bitmap3)
                    fragmentRegisterAuthorBinding.textAddFile.setText(bitmap3.toString())
                }
            }
        }

    }

    //첨부파일 런쳐 설정
    private fun settingImageLauncher(){
        val contract = ActivityResultContracts.StartActivityForResult()
        imageLauncher = registerForActivityResult(contract){ result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지 경로 데이터 관리하는 Uri 객체 추출
                val uri = result.data?.data
                if(uri != null){
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지 생성할 수 있는 객체 생성
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        // Bitmap 생성
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근
                        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
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
                    val degree = requireActivity().getDegree(uri)
                    // 회전 이미지 가져오기
                    val bitmap2 = bitmap?.rotate(degree.toFloat())
                    // 크기를 줄인 이미지 가져오기
                    val bitmap3 = bitmap2?.resize(1024)

                    fragmentRegisterAuthorBinding.imageView4.setImageBitmap(bitmap3)
                    fragmentRegisterAuthorBinding.textAddImage.setText(bitmap3.toString())
                }
            }
        }

    }

    //첨부파일 런쳐를 실행하는 메서드
    private fun startAlbumLauncher(){
        //사진 가져오기
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        albumIntent.setType("image/*")
        val mimeType = arrayOf("image/*")
        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)

        albumLauncher.launch(albumIntent)
    }

    //프로필 이미지 런쳐를 실행하는 메서드
    private fun startImageLauncher(){
        //사진 가져오기
        val imageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageIntent.setType("image/*")
        val mimeType = arrayOf("image/*")
        imageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)

        imageLauncher.launch(imageIntent)
    }
}
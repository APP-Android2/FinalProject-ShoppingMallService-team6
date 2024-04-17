package kr.co.lion.unipiece.ui.author

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentModifyAuthorInfoBinding
import kr.co.lion.unipiece.ui.author.viewmodel.ModifyAuthorInfoViewModel
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.getPictureUri
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.setImage
import java.io.File

class ModifyAuthorInfoFragment : Fragment() {

    private lateinit var fragmentModifyAuthorInfoBinding: FragmentModifyAuthorInfoBinding
    private val modifyAuthorInfoViewModel: ModifyAuthorInfoViewModel by viewModels()

    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", 0)
    }

    // 카메라, 앨범 실행을 위한 런처
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 촬영한 사진이 저장될 경로
    private val imageUri:Uri by lazy{
        requireActivity().getPictureUri("kr.co.lion.unipiece.file_provider")
    }

    // 작가 이미지 변경 여부
    private var checkImg = false
    private var albumImageUri:Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentModifyAuthorInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_author_info, container, false)
        fragmentModifyAuthorInfoBinding.modifyAuthorInfoViewModel = modifyAuthorInfoViewModel
        fragmentModifyAuthorInfoBinding.lifecycleOwner = this

        settingCameraLauncher()
        settingAlbumLauncher()

        lifecycleScope.launch {
            fetchData(authorIdx)
            settingToolbar()
            settingImageViewEvent()
            settingButtonUpdateAuthor()
            settingButtonModifyAuthorInfo()
        }


        return fragmentModifyAuthorInfoBinding.root
    }



    // 작가 정보 불러오기
    private fun fetchData(authorIdx:Int){
        lifecycleScope.launch {
            modifyAuthorInfoViewModel.getAuthorInfoData(authorIdx)

            // 작가 이미지 셋팅
            if(modifyAuthorInfoViewModel.authorInfoData.value != null){
                val authorImg = modifyAuthorInfoViewModel.authorInfoData.value!!.authorImg
                val imageUrl = modifyAuthorInfoViewModel.getAuthorInfoImg(authorImg)
                requireActivity().setImage(fragmentModifyAuthorInfoBinding.imageViewModifyAuthor, imageUrl)
            }
        }
    }

    // 툴바 셋팅
    private fun settingToolbar() {
        fragmentModifyAuthorInfoBinding.apply {
            toolbarModifyAuthorInfo.apply {
                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    removeFragment()
                }
            }
        }
    }

    // 이미지 뷰 클릭 시 사진 변경
    private fun settingImageViewEvent(){
        with(fragmentModifyAuthorInfoBinding){
            imageViewModifyAuthor.setOnClickListener {
                requireActivity().hideSoftInput()
                changeAuthorImage()
            }
            buttonModifyAuthorImage.setOnClickListener {
                requireActivity().hideSoftInput()
                changeAuthorImage()
            }
        }
    }

    // 앨범에서 가져올지 사진을 찍어서 가져올지 확인하는 다이얼로그
    private fun changeAuthorImage(){
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("이미지 변경 방법 선택")
        val items = arrayOf("카메라", "앨범")
        dialog.setItems(items, DialogInterface.OnClickListener { dialogInterface, i ->
            when(i){
                // 카메라 런처 실행
                0 -> { startCameraLauncher() }
                // 앨범 런처 실행
                1 -> { startAlbumLauncher() }
            }
        })
        dialog.show()
    }

    // 카메라 런처 설정
    fun settingCameraLauncher(){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        cameraLauncher = registerForActivityResult(contract1){
            // 사진을 사용하겠다고 한 다음에 돌아왔을 경우
            if(it.resultCode == AppCompatActivity.RESULT_OK){
                // 회전 각도값을 구한다.
                val degree = requireActivity().getDegree(imageUri)

                // 사진 객체를 생성한다.
                val bitmap = BitmapFactory
                    .decodeFile(imageUri.path)
                    .rotate(degree.toFloat())
                    .resize(150)

                // 이미지뷰에 변경된 이미지로 셋팅
                fragmentModifyAuthorInfoBinding.imageViewModifyAuthor.setImageBitmap(bitmap)
                checkImg = true
            }
        }
    }

    // 카메라 런처를 실행하는 메서드
    fun startCameraLauncher(){
        // 실행할 액티비티를 카메라 액티비티로 지정한다.
        // 단말기에 설치되어 있는 모든 애플리케이션이 가진 액티비티 중에 사진촬영이
        // 가능한 액티비가 실행된다.
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 이미지가 저장될 경로를 가지고 있는 Uri 객체를 인텐트에 담아준다.
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        // 카메라 액티비티 실행
        cameraLauncher.launch(cameraIntent)
    }

    // 앨범 런처 설정
    fun settingAlbumLauncher(){
        val contract2 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract2){
            // 사진 선택을 완료한 후 돌아왔다면
            if(it.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체를 추출한다.
                val uri = it.data?.data

                if(uri != null){
                    albumImageUri = uri
                    // 회전 각도값을 가져온다.
                    val degree = requireActivity().getDegree(uri)
                    // 안드로이드 Q(10) 이상이라면
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지를 생성할 수 있는 객체를 생성한다.
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        // Bitmap을 생성한다.
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성한다
                            BitmapFactory.decodeFile(source)
                                .rotate(degree.toFloat())
                                .resize(150)
                        } else {
                            null
                        }
                    }

                    // 이미지뷰에 변경된 이미지로 셋팅
                    fragmentModifyAuthorInfoBinding.imageViewModifyAuthor.setImageBitmap(bitmap)
                    checkImg = true
                }
            }
        }
    }

    // 앨범 런처를 실행하는 메서드
    fun startAlbumLauncher(){
        // 앨범에서 사진을 선택할 수 있도록 셋팅된 인텐트를 생성한다.
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것이 뜨게 한다)
        albumIntent.setType("image/*")
        // 선택할 수 있는 파들의 MimeType을 설정한다.
        // 여기서 선택한 종류의 파일만 선택이 가능하다. 모든 이미지로 설정한다.
        val mimeType = arrayOf("image/*")
        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        // 액티비티를 실행한다.
        albumLauncher.launch(albumIntent)
    }

    // 작가 갱신 버튼
    private fun settingButtonUpdateAuthor(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorUpdateAuthor.setOnClickListener {
            // 작가 갱신 액티비티로 이동
            val updateAuthorIntent = Intent(requireActivity(), UpdateAuthorActivity::class.java)
            updateAuthorIntent.putExtra("authorIdx", authorIdx)
            startActivity(updateAuthorIntent)
        }
    }

    // 수정 버튼
    private fun settingButtonModifyAuthorInfo(){
        fragmentModifyAuthorInfoBinding.buttonModifyAuthorInfoConfirm.setOnClickListener {
            requireActivity().hideSoftInput()
            lifecycleScope.launch {
                // 이미지 변경이 있는 경우 스토리지에 이미지 업로드
                if(checkImg){
                    with(fragmentModifyAuthorInfoBinding){
                        // 프로그래스 바 표시
                        layoutProgressModifyAuthor.visibility = View.VISIBLE
                        // 이미지 업로드 동안 버튼 클릭 방지
                        imageViewModifyAuthor.isClickable = false
                        buttonModifyAuthorImage.isClickable = false
                        buttonModifyAuthorInfoConfirm.isClickable = false
                        buttonModifyAuthorUpdateAuthor.isClickable = false
                    }

                    val uploadResult: Boolean
                    if(albumImageUri != null){
                        // 앨범의 이미지를 선택해 변경한 경우
                        uploadResult = modifyAuthorInfoViewModel.uploadImage(authorIdx, albumImageUri!!)
                    }else{
                        // 사진찍기로 변경한 경우
                        uploadResult = modifyAuthorInfoViewModel.uploadImage(authorIdx, imageUri)
                        // 임시 파일을 삭제한다.
                        val file = imageUri.path?.let { it1 -> File(it1) }
                        file?.delete()
                    }
                    // 프로그래스 바 숨기기
                    fragmentModifyAuthorInfoBinding.layoutProgressModifyAuthor.visibility = View.GONE

                    // 이미지 업로드 실패한 경우
                    if(!uploadResult){
                        Snackbar.make(requireActivity(), fragmentModifyAuthorInfoBinding.root, "통신 실패, 잠시후 다시 시도해주세요", Snackbar.LENGTH_SHORT).show()
                        with(fragmentModifyAuthorInfoBinding){
                            // 클릭 방지 해제
                            imageViewModifyAuthor.isClickable = true
                            buttonModifyAuthorImage.isClickable = true
                            buttonModifyAuthorInfoConfirm.isClickable = true
                            buttonModifyAuthorUpdateAuthor.isClickable = true
                        }
                        return@launch
                    }

                }
                // 작가 정보 업데이트
                modifyAuthorInfoViewModel.updateAuthorInfo()
                removeFragment()
            }

        }
    }

    private fun removeFragment(){
        parentFragmentManager.popBackStack(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
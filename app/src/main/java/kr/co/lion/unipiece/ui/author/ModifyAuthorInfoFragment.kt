package kr.co.lion.unipiece.ui.author

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
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
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentModifyAuthorInfoBinding
import kr.co.lion.unipiece.ui.author.viewmodel.ModifyAuthorInfoViewModel
import kr.co.lion.unipiece.util.AuthorInfoFragmentName
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.getPictureUri
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.setImage
import java.io.File

class ModifyAuthorInfoFragment : Fragment() {

    lateinit var fragmentModifyAuthorInfoBinding: FragmentModifyAuthorInfoBinding
    private val modifyAuthorInfoViewModel: ModifyAuthorInfoViewModel by viewModels()

    val authorIdx by lazy {
        requireArguments().getInt("authorIdx")
    }

    val userIdx by lazy {
        UniPieceApplication.prefs.getUserIdx("userIdx", 0)
    }

    // 카메라, 앨범 실행을 위한 런처
    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 촬영한 사진이 저장될 경로
    val imageUri:Uri by lazy{
        requireActivity().getPictureUri("kr.co.lion.unipiece.file_provider")
    }

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
                changeAuthorImage()
            }
            buttonModifyAuthorImage.setOnClickListener {
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

                // 파이어베이스 스토리지에 업로드
                
                // 업로드한 이미지 url 구하기
                
                // 파이어베이스 스토어에서 AuthorInfo의 authorImage값 업데이트
                
                // 이미지뷰에 변경된 이미지로 셋팅
                // requireActivity().setImage(fragmentModifyAuthorInfoBinding.imageViewModifyAuthor, imageUri)
                fragmentModifyAuthorInfoBinding.imageViewModifyAuthor.setImageBitmap(bitmap)

                // 임시 파일을 삭제한다.
                val file = imageUri.path?.let { it1 -> File(it1) }
                file?.delete()
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

    }

    // 앨범 런처를 실행하는 메서드
    fun startAlbumLauncher(){

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
            // 추후 수정
            lifecycleScope.launch {
                modifyAuthorInfoViewModel.updateAuthorInfo()
                removeFragment()
            }

        }
    }

    private fun removeFragment(){
        parentFragmentManager.popBackStack(AuthorInfoFragmentName.MODIFY_AUTHOR_INFO_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
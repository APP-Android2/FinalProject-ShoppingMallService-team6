package kr.co.lion.unipiece.ui.mygallery

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.ActivitySalesApplicationBinding
import kr.co.lion.unipiece.databinding.CategoryDialogBinding
import kr.co.lion.unipiece.model.PieceAddInfoData
import kr.co.lion.unipiece.ui.mygallery.viewmodel.PieceAddInfoViewModel
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.hideSoftInput
import kr.co.lion.unipiece.util.isKeyboardVisible
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.showSoftInput
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SalesApplicationActivity : AppCompatActivity() {

    lateinit var binding: ActivitySalesApplicationBinding
    private val viewModel: PieceAddInfoViewModel by viewModels()

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    private var topCategory = ""
    private var isAddPicture = false
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolbar()
        settingView()
        settingAlbumLauncher()
        setupErrorHandling()
        observeAddPieceInfoResult()
    }

    private fun observeAddPieceInfoResult() {
        viewModel.uploadImageResult.observe(this) { imageFileName ->
            if (!imageFileName.isNullOrEmpty()) {
                val pieceInfoData = createPieceInfoData(imageFileName)
                viewModel.addPieceInfo(pieceInfoData)
            } else {
                showErrorDialog()
            }
        }

        viewModel.addPieceInfoResult.observe(this) { isSuccess ->
            if (isSuccess) {
                showSuccessDialog()
            } else {
                showErrorDialog()
            }
        }
    }

    fun settingToolbar() {
        binding.apply {
            toolbarSalesApplication.apply {
                title = "작품 등록 신청"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    fun settingView() {
        binding.apply {
            imageViewSalesApplication.isVisible = false
            textViewImageError.isVisible = false

            textFieldSalesApplicationCategory.setOnClickListener {
                showCategoryDialog()
            }

            textFieldSalesApplicationDate.setOnClickListener {
                showDatePickerDialog()
            }

            textFieldSalesApplicationPieceName.apply {
                setOnEditorActionListener { v, actionId, event ->
                    this@SalesApplicationActivity.hideSoftInput()
                    showCategoryDialog()
                    true
                }
            }

            textFieldSalesApplicationPrice.apply {
                setOnEditorActionListener { v, actionId, event ->
                    this@SalesApplicationActivity.hideSoftInput()
                    showDatePickerDialog()
                    true
                }
            }

            buttonSalesApplicationSubmit.setOnClickListener {
                this@SalesApplicationActivity.hideSoftInput()

                if(isFormValid()) {
                    selectedImageUri?.let { imageUri ->
                        viewModel.uploadImage(imageUri)
                    }
                }
            }

            buttonSalesApplicationPictureAdd.setOnClickListener {
                startAlbumLauncher()
            }
        }
    }

    fun showDatePickerDialog() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.Theme_App_DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            val formattedDate = sdf.format(Date(selection))

            binding.textFieldSalesApplicationDate.setText(formattedDate)

            this@SalesApplicationActivity.showSoftInput(binding.textFieldSalesApplicationMaterial)
        }

        datePicker.show(supportFragmentManager, datePicker.toString())
    }

    fun showCategoryDialog() {
        MaterialAlertDialogBuilder(this@SalesApplicationActivity, R.style.Theme_Category_App_MaterialAlertDialog).apply {
            setTitle("카테고리")

            if(this@SalesApplicationActivity.isKeyboardVisible()) {
                this@SalesApplicationActivity.hideSoftInput()
            }

            val categoryDialogBinding = CategoryDialogBinding.inflate(layoutInflater)
            setView(categoryDialogBinding.root)

            categoryDialogBinding.apply {
                radioGroupArt.isVisible = false
                radioGroupHumanities.isVisible = false
                radioGroupEngineering.isVisible = false

                cardViewCategoryDialogArt.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupArt, imageViewArt, !radioGroupArt.isVisible)
                    hideOtherRadioGroups(radioGroupHumanities, radioGroupEngineering)
                    updateImageViewVisibility(imageViewHumanities, imageViewEngineering)
                    clearCheckedRadioButtons(radioGroupHumanities, radioGroupEngineering)
                    topCategory = "예술 대학"
                }

                cardViewCategoryDialogHumanities.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupHumanities, imageViewHumanities, !radioGroupHumanities.isVisible)
                    hideOtherRadioGroups(radioGroupArt, radioGroupEngineering)
                    updateImageViewVisibility(imageViewArt, imageViewEngineering)
                    clearCheckedRadioButtons(radioGroupArt, radioGroupEngineering)
                    topCategory = "인문 대학"
                }

                cardViewCategoryDialogEngineering.setOnClickListener {
                    toggleRadioGroupVisibility(radioGroupEngineering, imageViewEngineering, !radioGroupEngineering.isVisible)
                    hideOtherRadioGroups(radioGroupArt, radioGroupHumanities)
                    updateImageViewVisibility(imageViewArt, imageViewHumanities)
                    clearCheckedRadioButtons(radioGroupArt, radioGroupHumanities)
                    topCategory = "공과 대학"
                }
            }

            setNegativeButton("취소", null)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                val selectedRadioButton = when {
                    // 체크된 라디오 버튼이 있는지 확인
                    categoryDialogBinding.radioGroupArt.checkedRadioButtonId != -1
                    -> categoryDialogBinding.radioGroupArt.findViewById<RadioButton>(categoryDialogBinding.radioGroupArt.checkedRadioButtonId)

                    categoryDialogBinding.radioGroupHumanities.checkedRadioButtonId != -1
                    -> categoryDialogBinding.radioGroupHumanities.findViewById<RadioButton>(categoryDialogBinding.radioGroupHumanities.checkedRadioButtonId)

                    else -> categoryDialogBinding.radioGroupEngineering.findViewById<RadioButton>(categoryDialogBinding.radioGroupEngineering.checkedRadioButtonId)
                }
                binding.textFieldSalesApplicationCategory.setText(selectedRadioButton?.text ?: "")

                this@SalesApplicationActivity.showSoftInput(binding.textFieldSalesApplicationPrice)
            }

            show()
        }
    }

    fun toggleRadioGroupVisibility(radioGroup: RadioGroup, imageView: ImageView, show: Boolean) {
        radioGroup.isVisible = !radioGroup.isVisible
        imageView.setImageResource(if (show) R.drawable.arrowdropup_icon else R.drawable.arrowdropdown_icon)
    }

    fun hideOtherRadioGroups(vararg radioGroups: RadioGroup) {
        radioGroups.forEach { it.isVisible = false }
    }

    fun updateImageViewVisibility(imageView1: ImageView, imageView2: ImageView) {
        imageView1.setImageResource(R.drawable.arrowdropdown_icon)
        imageView2.setImageResource(R.drawable.arrowdropdown_icon)
    }

    fun clearCheckedRadioButtons(vararg radioGroups: RadioGroup) {
        radioGroups.forEach { it.clearCheck() }
    }

    fun startAlbumLauncher(){
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        albumIntent.setType("image/*")
        val mimeType = arrayOf("image/*")
        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        albumLauncher.launch(albumIntent)
    }

    fun settingAlbumLauncher() {
        val contract = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract){ result ->
            if(result.resultCode == RESULT_OK){
                // 선택한 이미지 경로 데이터 관리하는 Uri 객체 추출
                val uri = result.data?.data
                selectedImageUri = uri
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
                    val degree = getDegree(uri)
                    // 회전 이미지 가져오기
                    val bitmap2 = bitmap?.rotate(degree.toFloat())
                    // 크기를 줄인 이미지 가져오기
                    val bitmap3 = bitmap2?.resize(1024)


                    binding.imageViewSalesApplication.isVisible = true
                    binding.imageViewSalesApplication.setImageBitmap(bitmap3)
                    isAddPicture = true
                    binding.textViewImageError.isVisible = false
                }
            }
        }
    }

    fun createPieceInfoData(imageFileName: String): PieceAddInfoData {
        val authorName = viewModel.authorName.value ?: ""
        val pieceName = binding.textFieldSalesApplicationPieceName.text.toString()
        val pieceSort = topCategory
        val pieceDetailSort = binding.textFieldSalesApplicationCategory.text.toString()
        val fullPieceDate = binding.textFieldSalesApplicationDate.text.toString()
        val makeYear = fullPieceDate.substring(0, 4)
        val pieceSize = binding.textFieldSalesApplicationSize.text.toString()
        val pieceMaterial = binding.textFieldSalesApplicationMaterial.text.toString()
        val pieceInfo = binding.textFieldSalesApplicationDescription.text.toString()
        val addPieceImg = imageFileName
        val piecePrice = binding.textFieldSalesApplicationPrice.text.toString().toInt()
        val pieceState = "판매 승인 대기"
        val pieceDate = Timestamp.now()
        val authorIdx = viewModel.authorIdx.value ?: 0
        val pieceIdx = -1
        val addPieceIdx = 0

        return PieceAddInfoData(
            authorName, pieceName, pieceSort, pieceDetailSort,
            makeYear, pieceSize, pieceMaterial, pieceInfo,
            addPieceImg, piecePrice, pieceState, pieceDate,
            authorIdx, pieceIdx, addPieceIdx)
    }

    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(this@SalesApplicationActivity, R.style.Theme_Category_App_MaterialAlertDialog).apply {
            setTitle("작품 등록 신청 완료")
            setMessage("작품 등록 신청이 완료되었습니다.\n작품 등록 완료 시까지 1~2일 정도 소요됩니다.")

            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                finish()
            }

            show()
        }
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(this@SalesApplicationActivity, R.style.Theme_Category_App_MaterialAlertDialog).apply {
            setTitle("작품 등록 신청 실패")
            setMessage("네트워크 오류로 작품 등록 신청을 실패했습니다.\n다시 시도해주세요.")

            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }

            show()
        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        binding.apply {
            if (!isAddPicture) {
                textViewImageError.isVisible = true
                isValid = false
            }

            if (textFieldSalesApplicationPieceName.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationPieceName.error = "작품 이름을 입력해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationCategory.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationCategory.error = "카테고리를 선택해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationPrice.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationPrice.error = "최소 희망 가격을 입력해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationDate.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationDate.error = "제작 날짜를 선택해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationMaterial.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationMaterial.error = "작품 재료를 입력해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationSize.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationSize.error = "작품 크기를 입력해주세요."
                isValid = false
            }

            if (textFieldSalesApplicationDescription.text.isNullOrBlank()) {
                textInputLayoutSalesApplicationDescription.error = "작품 정보를 입력해주세요."
                isValid = false
            }
        }

        return isValid
    }

    private fun setupErrorHandling() {
        binding.apply {
            textFieldSalesApplicationPieceName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationPieceName.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationCategory.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationCategory.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationPrice.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationDate.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationDate.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationMaterial.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationMaterial.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationSize.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationSize.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldSalesApplicationDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutSalesApplicationDescription.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

}
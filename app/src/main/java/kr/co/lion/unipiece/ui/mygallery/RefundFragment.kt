package kr.co.lion.unipiece.ui.mygallery

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.FragmentRefundBinding
import kr.co.lion.unipiece.model.PieceBuyInfoData
import kr.co.lion.unipiece.ui.mygallery.viewmodel.RefundViewModel
import kr.co.lion.unipiece.util.CustomDialog
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName
import kr.co.lion.unipiece.util.getDegree
import kr.co.lion.unipiece.util.resize
import kr.co.lion.unipiece.util.rotate
import kr.co.lion.unipiece.util.setImage
import java.text.DecimalFormat

class RefundFragment : Fragment() {

    lateinit var binding: FragmentRefundBinding
    private val viewModel: RefundViewModel by viewModels()

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    private var pieceIdx = 0
    private var pieceBuyIdx = 0
    private var isAddPicture = false
    private var selectedImageUri: Uri? = null

    var refundReasonDialogData = arrayOf(
        "작품에 문제가 있어요", "주문하지 않은 작품이 왔어요", "작품을 받지 못했어요"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefundBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieceIdx = arguments?.getInt("pieceIdx", -1) ?: -1
        pieceBuyIdx = arguments?.getInt("pieceBuyIdx", -1) ?: -1

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarRefund.isVisible = true
            } else {
                binding.progressBarRefund.isVisible = false
            }
        }

        initData()
        initView()
        settingToolbar()
        settingTextFieldRefundReason()
        settingAlbumLauncher()
        settingButtonRefund()
        setupErrorHandling()
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.getPieceBuyInfoByPieceBuyIdx(pieceIdx, pieceBuyIdx)
        }
    }

    fun settingToolbar() {
        binding.apply {
            toolbarRefund.apply {
                title = "반품 접수"

                setNavigationIcon(R.drawable.back_icon)
                setNavigationOnClickListener {
                    parentFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }
        }
    }

    private fun initView() {
        binding.textViewRefundImageError.isVisible = false
        binding.imageViewRefundReason.isVisible = false

        val priceFormat = DecimalFormat("###,###")

        viewModel.pieceBuyInfoData.observe(viewLifecycleOwner) { pair ->
            binding.textViewRefundPieceName.text = pair.second?.pieceName
            binding.textViewRefundArtistName.text = pair.second?.authorName
            val piecePrice = priceFormat.format(pair.second?.piecePrice)
            binding.textViewRefundPiecePrice.text = "${piecePrice}원"
            val pieceBuyTotalPrice = priceFormat.format(pair.first?.pieceBuyTotalPrice)
            binding.textViewRefundTotalPrice.text = "${pieceBuyTotalPrice}원"
            val pieceBuyPrice = priceFormat.format(pair.first?.pieceBuyPrice)
            binding.textViewRefundPiecePrice2.text = "${pieceBuyPrice}원"
            val pieceBuySendPrice = priceFormat.format(pair.first?.pieceBuySendPrice)
            binding.textViewRefundDeliveryCharge.text = "${pieceBuySendPrice}원"
            binding.textViewRefundDiscount.text = "0원"
            binding.textViewRefundMethod.text = pair.first?.pieceBuyMethod
            requireActivity().setImage(binding.imageViewRefundPiece, pair.second?.pieceImg)
        }

        binding.buttonRefundPicture.setOnClickListener {
            startAlbumLauncher()
        }
    }

    fun settingTextFieldRefundReason() {
        binding.apply {
            textFieldRefundReason.setOnClickListener {
                showRefundReasonDialog()
            }
        }
    }

    fun showRefundReasonDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_Category_App_MaterialAlertDialog)
        materialAlertDialogBuilder.setTitle("반품 사유")
        materialAlertDialogBuilder.setNegativeButton("취소", null)
        materialAlertDialogBuilder.setItems(refundReasonDialogData) { dialogInterface: DialogInterface, i: Int ->
            binding.textFieldRefundReason.setText(refundReasonDialogData[i])

            if(binding.textInputLayoutRefundReason.isHelperTextEnabled) {
                binding.textInputLayoutRefundReason.isHelperTextEnabled = false
            }
        }
        materialAlertDialogBuilder.show()
    }

    fun settingButtonRefund() {
        binding.apply {
            buttonRefund.setOnClickListener {
                if(isFormValid()) {
                    val dialog = CustomDialog("반품 접수", "반품 접수를 하면 되돌일 수 없습니다.\n반품 접수하시겠습니까?\n\n반품 승인까지 1~2일 정도 소요됩니다.")

                    dialog.setButtonClickListener(object: CustomDialog.OnButtonClickListener{
                        override fun okButtonClick() {

                            lifecycleScope.launch {
                                selectedImageUri?.let { imageUri ->
                                    viewModel.uploadImage(pieceBuyIdx, imageUri)
                                }

                                viewModel.uploadImageResult.observe(viewLifecycleOwner) { imageFileName ->
                                    if (!imageFileName.isNullOrEmpty()) {
                                        val pieceInfoData = createRefundData(imageFileName)
                                        lifecycleScope.launch {
                                            viewModel.updatePieceBuyRefund(pieceInfoData)

                                            Snackbar.make(requireView(), "반품 접수 신청이 완료되었습니다.", Snackbar.LENGTH_SHORT)
                                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.second))
                                                .show()

                                            parentFragmentManager.popBackStack(PurchasedPieceDetailFragmentName.REFUND_FRAGMENT.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                        }
                                    } else {
                                        Snackbar.make(requireView(), "네트워크 오류로 반품 접수 신청을 실패했습니다.", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.second))
                                            .show()
                                    }
                                }
                            }
                        }

                        override fun noButtonClick() {
                        }
                    })

                    dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
                }
            }
        }
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
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지 경로 데이터 관리하는 Uri 객체 추출
                val uri = result.data?.data
                selectedImageUri = uri
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


                    binding.imageViewRefundReason.isVisible = true
                    binding.imageViewRefundReason.setImageBitmap(bitmap3)
                    isAddPicture = true
                    binding.textViewRefundImageError.isVisible = false
                }
            }
        }
    }

    fun createRefundData(imageFileName: String): PieceBuyInfoData {
        val pieceBuyState = "반품 승인 대기"
        val pieceBuyName = ""
        val pieceBuyPhone = ""
        val pieceBuyAddress = ""
        val pieceBuyMemo = ""
        val pieceBuyCo = ""
        val pieceBuySendNum = ""
        val pieceBuyPrice = 0
        val pieceBuySendPrice = 0
        val pieceBuyTotalPrice = 0
        val pieceBuyMethod = ""
        val pieceBuyCancel = ""
        val pieceBuyRefund = binding.textFieldRefundReason.text.toString()
        val pieceBuyDetailRefund = binding.textFieldRefundDetailReason.text.toString()
        val pieceBuyImg = imageFileName
        val pieceBuyDate = Timestamp.now()
        val pieceBuyIdx = pieceBuyIdx
        val pieceIdx = 0
        val userIdx = 0

        return PieceBuyInfoData(
            pieceBuyState, pieceBuyName, pieceBuyPhone, pieceBuyAddress, pieceBuyMemo,
            pieceBuyCo, pieceBuySendNum, pieceBuyPrice, pieceBuySendPrice, pieceBuyTotalPrice,
            pieceBuyMethod, pieceBuyCancel, pieceBuyRefund, pieceBuyDetailRefund,
            pieceBuyImg, pieceBuyDate, pieceBuyIdx, pieceIdx, userIdx)
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        binding.apply {
            if (!isAddPicture) {
                textViewRefundImageError.isVisible = true
                isValid = false
            }

            if (textFieldRefundReason.text.isNullOrBlank()) {
                textInputLayoutRefundReason.error = "반품 사유를 선택해주세요."
                isValid = false
            }

            if (textFieldRefundDetailReason.text.isNullOrBlank()) {
                textInputLayoutRefundDetailReason.error = "반품 상세 사유를 작성해주세요."
                isValid = false
            }
        }

        return isValid
    }

    private fun setupErrorHandling() {
        binding.apply {
            textFieldRefundReason.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutRefundReason.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            textFieldRefundDetailReason.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textInputLayoutRefundDetailReason.isErrorEnabled = false
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
}
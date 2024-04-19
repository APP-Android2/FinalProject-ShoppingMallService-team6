package kr.co.lion.unipiece.ui.payment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentDeliveryUpdateBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.adapter.DeliveryAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.DeliveryFragmentName

class DeliveryUpdateFragment : Fragment() {
    private lateinit var binding: FragmentDeliveryUpdateBinding
    private val viewModel: DeliveryViewModel by viewModels()

    val deliveryIdx by lazy {
        requireArguments().getInt("deliveryIdx")
    }

    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryUpdateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun initView(){
        // 바인딩
        with(binding) {

            // 툴바
            with(toolbarDeliveryUpdate) {
                // 툴바 타이틀
                title = "배송지 수정"
                // 툴바 뒤로가기 버튼
                setNavigationIcon(R.drawable.back_icon)
                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {

                }
            }


            // 텍스트 필드 초기값 셋팅
            textFieldDeliveryUpdateReceiver.text = "".toEditable()
            textFieldDeliveryUpdatePhone.text = "".toEditable()
//            val length = addNickName.length
//            textFieldDeliveryAddNickName.text = addNickName.substring(1, length - 1).toEditable()
            textFieldDeliveryUpdateNickName.text = "".toEditable()
            textFieldDeliveryUpdateAddress.text = "".toEditable()
            textFieldDeliveryUpdateAddressDetail.text = "".toEditable()
            checkBoxUpdateDeliveryBasicDelivery.isChecked = false

            // 집 클릭 시
            buttonDeliveryUpdateHouse.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = false
                textFieldDeliveryUpdateNickName.text = "집".toEditable()
            }
            // 회사 클릭 시
            buttonDeliveryUpdateCompany.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = false
                textFieldDeliveryUpdateNickName.text = "회사".toEditable()
            }
            // 학교 클릭 시
            buttonDeliveryUpdateSchool.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = false
                textFieldDeliveryUpdateNickName.text = "학교".toEditable()
            }
            // 친구 클릭 시
            buttonDeliveryUpdateFriend.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = false
                textFieldDeliveryUpdateNickName.text = "친구".toEditable()
            }
            // 가족 클릭 시
            buttonDeliveryUpdateFamily.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = false
                textFieldDeliveryUpdateNickName.text = "가족".toEditable()
            }
            // 직접입력 클릭 시
            buttonDeliveryUpdateDirectly.setOnClickListener {
                textFieldDeliveryUpdateNickName.isEnabled = true
                textFieldDeliveryUpdateNickName.text = "".toEditable()

            }
            // 주소 검색
            with(buttonDeliveryUpdateAddessSearch) {
                setOnClickListener {

                }
            }

            // 수정하기 버튼
            with(updateButton) {

                text = "수정하기"

                // 수정하기 버튼 클릭 시
                setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val deliveryData = setUpdateDeliveryData()
                        with(viewModel){
                            insertDeliveryData(deliveryData)

                        }
                        val supportFragmentManager = parentFragmentManager.beginTransaction()
                        supportFragmentManager.replace(R.id.containerDelivery, DeliveryManagerFragment())
                            .addToBackStack(DeliveryFragmentName.DELIVERY_MANAGER_FRAGMENT.str)
                            .commit()
                        viewModel.getDeliveryDataByIdx(userIdx)
                    }

                }
            }


            // 주소 비활성화
            textFieldDeliveryUpdateAddress.isEnabled = true
        }
    }


    // 업데이트할 데이터 셋팅
    fun setUpdateDeliveryData(): DeliveryData {
        with(binding) {
            val deliveryName = textFieldDeliveryUpdateReceiver.text.toString()
            val deliveryPhone = textFieldDeliveryUpdatePhone.text.toString()
            val deliveryNickName = textFieldDeliveryUpdateNickName.text.toString()
            val deliveryAddress = textFieldDeliveryUpdateAddress.text.toString()
            val deliveryAddressDetail = textFieldDeliveryUpdateAddressDetail.text.toString()
            val basicDelivery = checkBoxUpdateDeliveryBasicDelivery.isChecked
            val deliveryMemo = ""
            val userIdx = userIdx

            return DeliveryData(
                deliveryName,
                deliveryPhone,
                deliveryNickName,
                deliveryAddress,
                deliveryAddressDetail,
                deliveryMemo,
                basicDelivery,
                userIdx,
                deliveryIdx
            )
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}

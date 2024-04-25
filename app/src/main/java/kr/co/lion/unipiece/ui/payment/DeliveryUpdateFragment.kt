package kr.co.lion.unipiece.ui.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
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
    private val viewModel: DeliveryViewModel by activityViewModels()
    private lateinit var addressResultLauncher: ActivityResultLauncher<Intent>

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

        addressResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Intent에서 주소 데이터 추출
                val fullRoadAddr = intent?.getStringExtra("EXTRA_ROAD_ADDR")
                val jibunAddr = intent?.getStringExtra("EXTRA_JIBUN_ADDR")
                // 추출한 주소 데이터를 텍스트 필드에 설정
                binding.textFieldDeliveryUpdateAddress.setText(jibunAddr)
                binding.textFieldDeliveryUpdateAddress.setText(fullRoadAddr)

            }
        }

        initView()
    }

    fun initView() {
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
                    val supportFragmentManager = parentFragmentManager.beginTransaction()
                    supportFragmentManager.replace(
                        R.id.containerDelivery,
                        DeliveryManagerFragment()
                    ).commit()
                }
            }


            // 텍스트필드 초기값 셋팅
            lifecycleScope.launch {
                with(viewModel) {
                    getDeliveryDataByDeliveryIdx(deliveryIdx)
                    getDeliveryIdxDataLoading.observe(viewLifecycleOwner) {
                        if (it == true) {
                            viewModel.setGetDeliverIdxData()
                            viewModel.deliveryIdxDeliveryDataList.observe(viewLifecycleOwner) { list ->
                                // 받는 이
                                textFieldDeliveryUpdateReceiver.text = list[0].deliveryName.toEditable()
                                // 연락처
                                textFieldDeliveryUpdatePhone.text = list[0].deliveryPhone.toEditable()
                                // 배송지명
                                textFieldDeliveryUpdateNickName.text = list[0].deliveryNickName.toEditable()
                                // 주소
                                textFieldDeliveryUpdateAddress.text = list[0].deliveryAddress.toEditable()
                                // 상세주소
                                textFieldDeliveryUpdateAddressDetail.text =
                                    list[0].deliveryAddressDetail.toEditable()
                                // 배송메모
                                textFieldDeliveryUpdateMemo.text = list[0].deliveryMemo.toEditable()
                                // 기본 배송지 여부
                                checkBoxUpdateDeliveryBasicDelivery.isChecked = list[0].basicDelivery
                            }
                        }
                    }
                }


            }


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
                    val intent = Intent(context, SearchAddressActivity::class.java)
                    addressResultLauncher.launch(intent)
                }
            }

            // 수정하기 버튼
            with(updateButton) {

                text = "수정하기"

                // 수정하기 버튼 클릭 시
                setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val deliveryData = setUpdateDeliveryData()
                        with(viewModel) {
                            insertDeliveryData(deliveryData)
                            insertDataLoading.observe(viewLifecycleOwner) {
                                if (it == true) {
                                    viewModel.setInsertData()
                                    parentFragmentManager.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
            // 주소 비활성화
            textFieldDeliveryUpdateAddress.isEnabled = false
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
            val deliveryMemo = textFieldDeliveryUpdateMemo.text.toString()
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

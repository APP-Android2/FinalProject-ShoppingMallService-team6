package kr.co.lion.unipiece.ui.payment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentDeliveryAddBinding
import kr.co.lion.unipiece.model.DeliveryData
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.util.DeliveryFragmentName

class DeliveryAddFragment : Fragment() {

    lateinit var binding: FragmentDeliveryAddBinding
    private val viewModel: DeliveryViewModel by activityViewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)
    private lateinit var addressResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryAddBinding.inflate(inflater, container, false)

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
                binding.textFieldDeliveryAddAddress.setText(jibunAddr)
                binding.textFieldDeliveryAddAddress.setText(fullRoadAddr)

            }
        }


        initView()

    }

    fun initView() {

        // 바인딩
        with(binding) {

            // 툴바
            with(toolbarDeliveryAdd) {
                // 툴바 타이틀
                title = "신규 배송지 등록"
                // 툴바 뒤로가기 버튼
                setNavigationIcon(R.drawable.back_icon)
                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    val supportFragmentManager = parentFragmentManager.beginTransaction()
                    supportFragmentManager.replace(
                        R.id.containerDelivery,
                        DeliveryManagerFragment()
                    )
                        .addToBackStack(DeliveryFragmentName.DELIVERY_MANAGER_FRAGMENT.str)
                        .commit()
                }
            }

            // 텍스트 필드 초기값 셋팅
            textFieldDeliveryAddReceiver.text = "".toEditable()
            textFieldDeliveryAddPhone.text = "".toEditable()
            textFieldDeliveryAddNickName.text = "".toEditable()
            textFieldDeliveryAddAddress.text = "".toEditable()
            textFieldDeliveryAddAddressDetail.text = "".toEditable()
            textFieldDeliveryAddMemo.text = "".toEditable()

            checkBoxAddDeliveryBasicDelivery.isChecked = false

            // 집 클릭 시
            buttonDeliveryAddHouse.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "집".toEditable()
            }
            // 회사 클릭 시
            buttonDeliveryAddCompany.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "회사".toEditable()
            }
            // 학교 클릭 시
            buttonDeliveryAddSchool.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "학교".toEditable()
            }
            // 친구 클릭 시
            buttonDeliveryAddFriend.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "친구".toEditable()
            }
            // 가족 클릭 시
            buttonDeliveryAddFamily.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = false
                textFieldDeliveryAddNickName.text = "가족".toEditable()
            }
            // 집 클릭 시
            buttonDeliveryAddDirectly.setOnClickListener {
                textFieldDeliveryAddNickName.isEnabled = true
                textFieldDeliveryAddNickName.text = "".toEditable()

            }
            // 주소 검색
            with(buttonDeliveryAddAddressSearch) {
                setOnClickListener {
                    val intent = Intent(context, SearchAddressActivity::class.java)
                    addressResultLauncher.launch(intent)
                }
            }

            // 등록하기 버튼
            with(saveButton) {

                text = "등록하기"

                // 등록하기 버튼 클릭 시
                setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val deliveryData = setInsertDeliveryData()
                        with(viewModel) {
                            insertDeliveryData(deliveryData)
                            insertDataLoading.observe(viewLifecycleOwner) {
                                // 데이터 삽입 작업이 완료되었을 때 호출됨
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
            textFieldDeliveryAddAddress.isEnabled = true
        }
    }

    // 신규 등록할 데이터 셋팅
    fun setInsertDeliveryData(): DeliveryData {
        with(binding) {
            val deliveryName = textFieldDeliveryAddReceiver.text.toString()
            val deliveryPhone = textFieldDeliveryAddPhone.text.toString()
            val deliveryNickName = textFieldDeliveryAddNickName.text.toString()
            val deliveryAddress = textFieldDeliveryAddAddress.text.toString()
            val deliveryAddressDetail = textFieldDeliveryAddAddressDetail.text.toString()
            val basicDelivery = checkBoxAddDeliveryBasicDelivery.isChecked
            val deliveryMemo = textFieldDeliveryAddMemo.text.toString()
            val userIdx = userIdx
            val deliveryIdx = 0

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
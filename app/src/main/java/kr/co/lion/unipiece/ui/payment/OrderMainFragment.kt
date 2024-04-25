package kr.co.lion.unipiece.ui.payment

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentOrderMainBinding
import kr.co.lion.unipiece.ui.author.AuthorInfoActivity
import kr.co.lion.unipiece.ui.buy.BuyDetailActivity
import kr.co.lion.unipiece.ui.payment.adapter.OrderMainAdapter
import kr.co.lion.unipiece.ui.payment.viewmodel.DeliveryViewModel
import kr.co.lion.unipiece.ui.payment.viewmodel.OrderViewModel
import java.text.DecimalFormat
import java.util.Locale


class OrderMainFragment : Fragment() {

    lateinit var binding: FragmentOrderMainBinding
    private val orderViewModel: OrderViewModel by viewModels()
    private val deliveryViewModel: DeliveryViewModel by viewModels()
    val userIdx = UniPieceApplication.prefs.getUserIdx("userIdx", 0)

    // 배송지관리에서 넘어온 배송지 번호
    private val deliveryIdx: Int by lazy {
        arguments?.getInt("deliveryIdx", 0) ?: 0
    }

    // 장바구니에서 넘어온 작품 번호 리스트
    private val pieceIdxList: ArrayList<Int>? by lazy {
        arguments?.getIntegerArrayList("pieceIdxList")
    }

    // 작품상세에서 넘어온 작품 번호 하나
    private val pieceIdx: Int by lazy{
        arguments?.getInt("pieceIdx",0)?:0
    }

    // 어답터 조작
    val orderMainAdapter: OrderMainAdapter = OrderMainAdapter(
        emptyList(),
        pieceImgOnClickListener = {
            val intent = Intent(context, BuyDetailActivity::class.java).apply {
                putExtra("authorIdx", it["authorIdx"])
                putExtra("pieceIdx", it["pieceIdx"])
            }
            startActivity(intent)
        },
        authorNameOnClickListener = {
            val intent = Intent(context, AuthorInfoActivity::class.java).apply {
                putExtra("authorIdx", it)
            }
            startActivity(intent)
        }

    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentOrderMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeData()
        viewLifecycleOwner.lifecycleScope.launch {

            // 작품상세에서 넘어온 경우
            if (pieceIdx != 0) {
                orderViewModel.getIdxPieceInfoOne(pieceIdx) // 단일 작품 정보 가져오는 메소드를 구현해야 함.
            }
            Log.d("pieceIdxList test5678",pieceIdxList.toString())
            // 장바구니에서 넘어온 경우
            pieceIdxList?.let {
                if (it.isNotEmpty()) {
                    orderViewModel.getIdxPieceInfo(it)
                }
            }
        }


    }

    fun initView() {
        // 바인딩
        with(binding) {

            // 툴바
            toolbarOrderMain.apply {
                // 타이틀
                title = "주문하기"
                isTitleCentered = true

                // 뒤로가기 아이콘
                setNavigationIcon(R.drawable.back_icon)
                // 클릭 시 동작
                setNavigationOnClickListener {
                    requireActivity().finish()
                }
            }

            // 처음 들어왔을 때 기본 배송지 세팅
            deliveryViewModel.getBasicDeliveryData.observe(viewLifecycleOwner) {
                lifecycleScope.launch {


                    // 데이터가 있다면
                    if (it.isNotEmpty()) {
                        // 배송지 관리에서 선택한 배송지정보로 세팅
                        // deliveryIdx 값이 존재한다면 (배송지관리에서 선택하여 넘어왔다면)
                        if (deliveryIdx != 0) {
                            // 배송지관리에서 선택한 데이터를 세팅한다.
                            deliveryViewModel.getDeliveryDataByDeliveryIdx(deliveryIdx ?: 0)
                            deliveryViewModel.deliveryIdxDeliveryDataList.observe(viewLifecycleOwner) {
                                val deliveryName = it[0].deliveryName
                                val deliveryPhone = it[0].deliveryPhone
                                val deliveryAddress = it[0].deliveryAddress
                                val deliveryAddressDetail = it[0].deliveryAddressDetail
                                val basicMemo = it[0].deliveryMemo

                                // 받는 이
                                textViewOrderPersonName.text = deliveryName
                                // 연락처
                                textViewOrderPhone.text = PhoneNumberUtils.formatNumber(deliveryPhone, Locale.getDefault().country)
                                // 주소 (주소 + 상세주소)
                                textViewOrderAddress.text = "${deliveryAddress} ${deliveryAddressDetail}"

                                // 배송 메모 세팅
                                with(spinnerOrderMainDeliveryMemo) {

                                    val items = arrayOf(basicMemo, "문 앞", "경비실", "택배함", "선택 안함")
                                    val adapter =
                                        ArrayAdapter(
                                            context,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            items
                                        )
                                    val spinner: Spinner =
                                        findViewById(R.id.spinnerOrderMainDeliveryMemo)
                                    spinner.adapter = adapter

                                }
                            }
                            // deliveryIdx 값이 존재하지 않는다면 (배송지관리에서 받은 데이터가 없다면)
                        } else {
                            // 기본배송지를 세팅한다.
                            val deliveryName = it[0].deliveryName
                            val deliveryPhone = it[0].deliveryPhone
                            val deliveryAddress = it[0].deliveryAddress
                            val deliveryAddressDetail = it[0].deliveryAddressDetail
                            val basicMemo = it[0].deliveryMemo

                            // 받는 이
                            textViewOrderPersonName.text = deliveryName
                            // 연락처
                            textViewOrderPhone.text = PhoneNumberUtils.formatNumber(deliveryPhone, Locale.getDefault().country)
                            // 주소 (주소 + 상세주소)
                            textViewOrderAddress.text = "${deliveryAddress} ${deliveryAddressDetail}"

                            // 배송 메모 세팅
                            with(spinnerOrderMainDeliveryMemo) {

                                val items = arrayOf(basicMemo, "문 앞", "경비실", "택배함", "선택 안함")
                                val adapter =
                                    ArrayAdapter(
                                        context,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        items
                                    )
                                val spinner: Spinner =
                                    findViewById(R.id.spinnerOrderMainDeliveryMemo)
                                spinner.adapter = adapter
                            }
                        }
                    }
                }
            }




            // 배송지 변경 버튼 클릭
            with(buttonOrderDeliveryChange) {
                setOnClickListener {
                    // DeliveryActivity를 실행한다.
                    val deliveryIntent = Intent(requireActivity(), DeliveryActivity::class.java)
                    startActivity(deliveryIntent)
                }
            }


            // 결제하기 버튼 클릭
            with(buttonOrderPaymentSubmit) {
                setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.containerOrder, OrderSuccessFragment())
                        .commit()
                    // 추후 수정
                }
            }

            // 주문 상세 설정

            // 작품 가격 합계
            viewLifecycleOwner.lifecycleScope.launch {
                orderViewModel.pieceIdxPieceInfoDataList.observe(viewLifecycleOwner) {
                    // 작품 가격의 합계를 계산합니다.
                    val totalPrice = (it.sumOf { it.piecePrice })
                    val totalMoney = totalPrice + 3000

                    // 계산된 총 금액을 포맷하여 TextView에 설정합니다.
                    val decPrice = DecimalFormat("#,###")
                    textViewOrderMainPiecePrice.text = "${decPrice.format(totalPrice)} 원"

                    val decMoney = DecimalFormat("#,###")
                    textViewOrderMoneyMain.text = "${decMoney.format(totalMoney)} 원"

                }

            }




            // 주문하기 화면 RecyclerView
            with(recyclerViewOrderList) {


                // 어뎁터
                adapter = orderMainAdapter
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(requireActivity())

                // 마지막 리사이클러뷰 항목의 디바이더 삭제
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        requireActivity(),
                        (layoutManager as LinearLayoutManager).orientation
                    ).apply {
                        isLastItemDecorated = false
                        setDividerColorResource(requireActivity(), R.color.lightgray)
                        dividerInsetEnd = 16.dp
                        dividerInsetStart = 16.dp
                    }
                )
            }
        }
    }


    fun observeData() {
        orderViewModel.pieceIdxPieceInfoDataList.observe(viewLifecycleOwner) { pieceInfoList ->
            orderMainAdapter.updateData(pieceInfoList)
        }
    }


    // dp값으로 변환하는 확장함수 ex) 16dp로 적용시 16.dp 로 작성 //////////////////////////////////
    inline val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

    inline val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
        )
}




package kr.co.lion.unipiece.ui.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityPurchasedPieceDetailBinding
import kr.co.lion.unipiece.ui.buy.BuyFragment
import kr.co.lion.unipiece.ui.home.HomeFragment
import kr.co.lion.unipiece.ui.mygallery.MyGalleryFragment
import kr.co.lion.unipiece.ui.mypage.MyPageFragment
import kr.co.lion.unipiece.ui.rank.RankFragment
import kr.co.lion.unipiece.util.MainFragmentName
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName

class PurchasedPieceDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityPurchasedPieceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasedPieceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(PurchasedPieceDetailFragmentName.PURCHEASED_PIECE_DETAIL_FRAGMENT, false)
    }

    fun replaceFragment(name: PurchasedPieceDetailFragmentName, addToBackStack:Boolean){
        SystemClock.sleep(200)

        // Fragment 교체할 수 있는 객체 추출
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 이름으로 분기
        // Fragment의 객체를 생성하여 변수에 담기
        when(name){
            PurchasedPieceDetailFragmentName.PURCHEASED_PIECE_DETAIL_FRAGMENT
            -> fragmentTransaction.replace(R.id.containerPurchasedPieceDetail, PurchasedPieceDetailFragment())
            PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT
            -> fragmentTransaction.replace(R.id.containerPurchasedPieceDetail, PurchaseCancelFragment())
            PurchasedPieceDetailFragmentName.REFUND_FRAGMENT
            -> fragmentTransaction.replace(R.id.containerPurchasedPieceDetail, RefundFragment())
        }

        // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시키기
        if(addToBackStack == true){
            // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거
            fragmentTransaction.addToBackStack(name.str)
        }
        // Fragment 교체 확정
        fragmentTransaction.commit()
    }

    fun removeFragment(name: MainFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
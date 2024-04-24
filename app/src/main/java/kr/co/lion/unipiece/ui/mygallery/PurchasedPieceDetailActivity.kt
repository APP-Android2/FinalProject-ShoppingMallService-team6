package kr.co.lion.unipiece.ui.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivityPurchasedPieceDetailBinding
import kr.co.lion.unipiece.util.PurchasedPieceDetailFragmentName

class PurchasedPieceDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityPurchasedPieceDetailBinding

    var oldFragment: Fragment? = null
    var newFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasedPieceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pieceIdx = intent.getIntExtra("pieceIdx", -1)
        val pieceBuyIdx = intent.getIntExtra("pieceBuyIdx", -1)
        val pieceBuyState = intent.getStringExtra("pieceBuyState")

        val bundle = Bundle()
        bundle.putInt("pieceIdx", pieceIdx)
        bundle.putInt("pieceBuyIdx", pieceBuyIdx)
        bundle.putString("pieceBuyState", pieceBuyState)

        replaceFragment(PurchasedPieceDetailFragmentName.PURCHEASED_PIECE_DETAIL_FRAGMENT, false, bundle)
    }

    fun replaceFragment(name: PurchasedPieceDetailFragmentName, addToBackStack:Boolean, data:Bundle?){
        SystemClock.sleep(200)

        // Fragment 교체할 수 있는 객체 추출
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // oldFragment에 newFragment가 가지고 있는 Fragment 객체 담기
        if(newFragment != null){
            oldFragment = newFragment
        }

        // 이름으로 분기
        // Fragment의 객체를 생성하여 변수에 담기
        newFragment = when(name){
            PurchasedPieceDetailFragmentName.PURCHEASED_PIECE_DETAIL_FRAGMENT
            -> PurchasedPieceDetailFragment()
            PurchasedPieceDetailFragmentName.PURCHASE_CANCEL_FRAGEMNT
            -> PurchaseCancelFragment()
            PurchasedPieceDetailFragmentName.REFUND_FRAGMENT
            -> RefundFragment()
        }

        // 새로운 Fragment에 전달할 Bundle 객체가 있다면 arguments 프로퍼티에 넣기
        if(data != null){
            newFragment?.arguments = data
        }

        if(newFragment != null){
            // Fragment 교체
            fragmentTransaction.replace(R.id.containerPurchasedPieceDetail, newFragment!!)

            // addToBackStack 변수의 값이 true면 새롭게 보여질 Fragment를 BackStack에 포함시키기
            if(addToBackStack == true){
                // BackStack 포함 시킬때 이름을 지정해주면 원하는 Fragment를 BackStack에서 제거
                fragmentTransaction.addToBackStack(name.str)
            }

            // Fragment 교체를 확정
            fragmentTransaction.commit()
        }

    }

    fun removeFragment(name: PurchasedPieceDetailFragmentName){
        SystemClock.sleep(200)

        // 지정한 이름으로 있는 Fragment를 BackStack에서 제거
        supportFragmentManager.popBackStack(name.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
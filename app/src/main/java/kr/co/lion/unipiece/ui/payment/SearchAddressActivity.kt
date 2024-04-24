package kr.co.lion.unipiece.ui.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.databinding.ActivitySearchAddressBinding

class SearchAddressActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchAddressBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()


    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initView(){
        with(binding){

            // 툴바 셋팅
            with(toolbarAddressMain){

                // 타이틀
                title = "주소 검색"

                // 뒤로가기 버튼
                setNavigationIcon(R.drawable.back_icon)
                // 뒤로가기 버튼 클릭 시
                setNavigationOnClickListener {
                    finish()
                }

            }

            val webView: WebView = binding.locationSearchWebView

            webView.clearCache(true)
            webView.settings.javaScriptEnabled = true

            webView.addJavascriptInterface(BridgeInterface(), "Android")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    webView.loadUrl("javascript:sample2_execDaumPostcode();")
                }
            }

            webView.loadUrl("https://seulseul-35d52.web.app")
        }
    }

    inner class BridgeInterface() {
        @JavascriptInterface
        @SuppressWarnings("unused")
        fun processDATA(fullRoadAddr: String, jibunAddr: String) {
            val intent = Intent(this@SearchAddressActivity, DeliveryAddFragment()::class.java)
            intent.putExtra("EXTRA_ROAD_ADDR", fullRoadAddr)
            intent.putExtra("EXTRA_JIBUN_ADDR", jibunAddr)
            Log.d("jibun", jibunAddr)
            Log.d("jibun", fullRoadAddr)
            setResult(RESULT_OK, intent)
            finish()
        }

    }


}

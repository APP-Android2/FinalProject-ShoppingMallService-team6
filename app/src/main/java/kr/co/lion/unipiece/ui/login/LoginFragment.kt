package kr.co.lion.unipiece.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.unipiece.BuildConfig
import kr.co.lion.unipiece.R
import kr.co.lion.unipiece.UniPieceApplication
import kr.co.lion.unipiece.databinding.FragmentLoginBinding
import kr.co.lion.unipiece.ui.MainActivity
import kr.co.lion.unipiece.util.LoginFragmentName
import kr.co.lion.unipiece.util.showSoftInput


class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding

    val viewModel: LoginViewModel by viewModels()

    //아이디 중복 검사
    var checkUserId = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        naverInitialize()
        settingEvent()
        initView()
        return fragmentLoginBinding.root
    }

    //네이버 로그인 initialize
    private fun naverInitialize(){
        val naverClientId = BuildConfig.NAVER_CLIENT_ID
        val naverClientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val naverClientName = BuildConfig.NAVER_CLIENT_NAME
        NaverIdLoginSDK.initialize(requireActivity(), naverClientId, naverClientSecret, naverClientName)
    }

    //버튼 클릭
    private fun settingEvent(){
        fragmentLoginBinding.apply {
            buttonLoginGoJoin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.loginContainer, JoinFragment())
                    .addToBackStack(LoginFragmentName.JOIN_FRAGMENT.str)
                    .commit()
            }
            buttonLogin.setOnClickListener {
                val chk = checkInput()
                if (chk == true){
                    checkAccount()
                }
            }
            imageKaKao.setOnClickListener {
                kakaoLogin()
            }
            imageNaver.setOnClickListener {
                naverLogin()
            }
        }
    }

    //화면 설정
    private fun initView(){
        fragmentLoginBinding.apply {
            textLoginUserId.addTextChangedListener {
                textLoginUserIdLayout.error = null
            }
            textLoginUserPw.addTextChangedListener {
                textLoginUserPwdLayout.error = null
            }
        }
    }

    //입력 검사
    private fun checkInput() : Boolean{
        fragmentLoginBinding.apply {
            var errorText:View? = null

            val userId = textLoginUserId.text.toString()
            val userPwd = textLoginUserPw.text.toString()

            if (userId.trim().isEmpty()){
                textLoginUserIdLayout.error = "아이디를 입력해주세요"
                if (errorText == null){
                    errorText = textLoginUserId
                }else{
                    textLoginUserIdLayout.error = null
                }
            }

            if (userPwd.trim().isEmpty()){
                textLoginUserPwdLayout.error = "비밀번호를 입력해주세요"
                if (errorText == null){
                    errorText = textLoginUserPw
                }else{
                    textLoginUserPwdLayout.error = null
                }
            }

            if (errorText != null){
                requireActivity().showSoftInput(errorText)
                return false
            }else{
                return true
            }
        }
    }

    //유효한 계정인지 검사한다
    private fun checkAccount(){
        fragmentLoginBinding.apply {

            val userId = textLoginUserId.text.toString()
            val userPwd = textLoginUserPw.text.toString()

            viewLifecycleOwner.lifecycleScope.launch {
                val userInfo = viewModel.getUserDataByUserId(userId)

                //만약 아이디가 없다면
                if (userInfo == null){
                    textLoginUserIdLayout.error = "존재하지 않은 아이디입니다"
                    requireActivity().showSoftInput(textLoginUserId)
                }else{
                    textLoginUserIdLayout.error = null

                    //아이디는 유효한데 비밀번호가 틀릴경우
                    if (userPwd != userInfo.userPwd){
                        textLoginUserPwdLayout.error = "존재하지 않은 비밀번호입니다"
                        requireActivity().showSoftInput(textLoginUserPw)
                    }else{
                        val newIntent = Intent(requireActivity(), MainActivity::class.java)
                        UniPieceApplication.prefs.setUserIdx("userIdx", userInfo.userIdx)
                        startActivity(newIntent)
                        requireActivity().finish()

                    }
                }
            }

        }
    }

    //카카오 로그인 구현
    private fun kakaoLogin(){
        val TAG = "test1234"

        //kakaoSDK 초기화
        KakaoSdk.init(requireActivity(), BuildConfig.KAKAO_API_KEY)

        //카카오 계정으로 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

         // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) {
            UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
                    //Log.d("seonguk1234", Utility.getKeyHash(requireActivity()))
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    // 로그인한 사용자 정보를 가져온다.
                    // 이 때 accessToken 을 카카오 서버로 전달해야 해야하는데 알아서해준다.
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보를 가져오는데 실패하였습니다", error)
                        } else if (user != null) {

                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                checkUserId = viewModel.checkUserId(user.kakaoAccount?.email?:"")

                                if (checkUserId != false){

                                    val dialog = NicknameDialog("닉네임을 입력해주세요")
                                    dialog.setNicknameButtonClickListener(object : NicknameDialog.dialogButtonClickListener{
                                        override fun nicknameOkButton() {
                                            val nickname = dialog.binding.nickNameDialog.text.toString()
                                            val userId = user.kakaoAccount?.email?:""
                                            val name = user.kakaoAccount?.name?:""
                                            val phoneNumber = user.kakaoAccount?.phoneNumber?:""
                                            val userPwd = user.id.toString()

                                            viewModel.insertUserData(name, nickname, phoneNumber, userId, userPwd,true){success ->
                                                if (success){
                                                    viewLifecycleOwner.lifecycleScope.launch {
                                                        val userInfo = viewModel.getUserDataByUserId(userId)

                                                        val newIntent = Intent(requireActivity(), MainActivity::class.java)
                                                        UniPieceApplication.prefs.setUserIdx("userIdx", userInfo!!.userIdx)
                                                        startActivity(newIntent)
                                                    }
                                                }
                                            }
                                        }

                                        override fun nicknameNoButton() {

                                        }

                                    })
                                    dialog.show(parentFragmentManager, "NicknameDialog")

                                }else{
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        val userInfo = viewModel.getUserDataByUserId(user.kakaoAccount?.email?:"")
                                        val newIntent = Intent(requireActivity(), MainActivity::class.java)
                                        UniPieceApplication.prefs.setUserIdx("userIdx", userInfo!!.userIdx)
                                        startActivity(newIntent)
                                        requireActivity().finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
        }
    }


    private fun naverLogin(){
        var naverToken:String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse>{
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(requireActivity(), "에러코드 : ${errorCode}" + "에러 이유 : ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: NidProfileResponse) {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    checkUserId = viewModel.checkUserId(result.profile?.email?:"")

                    if (checkUserId != false){

                        val dialog = NicknameDialog("닉네임을 입력해주세요")
                        dialog.setNicknameButtonClickListener(object : NicknameDialog.dialogButtonClickListener{
                            override fun nicknameOkButton() {
                                val nickname = dialog.binding.nickNameDialog.text.toString()
                                val userId = result.profile?.email?:""
                                val name = result.profile?.name?:""
                                val phoneNumber = result.profile?.mobile?:""
                                val userPwd = result.profile?.id?:""


                                viewModel.insertUserData(name, nickname, phoneNumber, userId, userPwd,true){success ->
                                    if (success){
                                        viewLifecycleOwner.lifecycleScope.launch {
                                            val userInfo = viewModel.getUserDataByUserId(userId)

                                            val newIntent = Intent(requireActivity(), MainActivity::class.java)
                                            UniPieceApplication.prefs.setUserIdx("userIdx", userInfo!!.userIdx)
                                            startActivity(newIntent)
                                        }
                                    }
                                }
                            }

                            override fun nicknameNoButton() {

                            }

                        })
                        dialog.show(parentFragmentManager, "NicknameDialog")

                    }else{
                        viewLifecycleOwner.lifecycleScope.launch {
                            val userInfo = viewModel.getUserDataByUserId(result.profile?.email?:"")
                            val newIntent = Intent(requireActivity(), MainActivity::class.java)
                            UniPieceApplication.prefs.setUserIdx("userIdx", userInfo!!.userIdx)
                            startActivity(newIntent)
                            requireActivity().finish()
                        }
                    }
                }
            }

        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(requireActivity(), "에러코드 : ${errorCode}" + "에러 이유 : ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                naverToken = NaverIdLoginSDK.getAccessToken()
                NidOAuthLogin().callProfileApi(profileCallback)
            }

        }
        NaverIdLoginSDK.authenticate(requireActivity(), oauthLoginCallback)
    }

}

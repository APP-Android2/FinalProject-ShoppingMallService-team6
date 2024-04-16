package kr.co.lion.unipiece.ui.buy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BuyDetailViewModelFactory(private val pieceIdx: Int): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(BuyDetailViewModel::class.java)){
            return BuyDetailViewModel(pieceIdx) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }
}
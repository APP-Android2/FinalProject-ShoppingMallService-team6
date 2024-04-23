package kr.co.lion.unipiece.model

import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType

data class SearchResultData(
    var searchPieceData: PieceInfoData = PieceInfoData(),
    var searchAuthorData: AuthorInfoData = AuthorInfoData(),
    var viewType: SearchResultViewType,
    var viewTitle: Boolean = true
)

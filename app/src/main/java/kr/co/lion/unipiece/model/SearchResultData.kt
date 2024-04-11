package kr.co.lion.unipiece.model

import kr.co.lion.unipiece.ui.search.adapter.SearchResultViewType

data class SearchResultData(
    var searchAuthorData: SearchAuthorData = SearchAuthorData(),
    var searchPieceData: SearchPieceData = SearchPieceData(),
    var viewType: SearchResultViewType,
    var viewTitle: Boolean = true
)

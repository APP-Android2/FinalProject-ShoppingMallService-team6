package kr.co.lion.unipiece.ui.search.adapter

data class SearchResultData(
    var searchAuthorData: SearchAuthorData = SearchAuthorData(),
    var searchPieceData: SearchPieceData = SearchPieceData(),
    var viewType: SearchResultViewType,
    var viewTitle: Boolean = true
)

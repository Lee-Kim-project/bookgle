package bookgle.bookgle.search.domain;

import lombok.Getter;

@Getter
public enum SearchUrl {
    // base url
    BASE_URL("http://data4library.kr/api"),
    // 도서관별 소장 도서 조회
    BOOKS_BY_LIBRARY("/itemSrch"),
    // 도서 소장 도서관 조회
    ALL_LIBRARY_HAS_THE_BOOK("/libSrchByBook"),
    // 도서 정보 조회
    BOOKS_INFO("/srchBooks"),
    // 도서관별 대출 가능여부 조회
    IS_BOOK_AVAILABLE("/bookExist");


    private final String url;
    SearchUrl(String url) {
        this.url = url;
    }
}

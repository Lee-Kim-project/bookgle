package bookgle.bookgle.search.domain;

public enum SearchUrl {
    // 도서관별 소장 도서 조회
    BOOKS_BY_LIBRARY("http://data4library.kr/api/itemSrch?authKey=[발급받은키]&libCode=[도서관코드]&type=ALL"),
    // 도서 소장 도서관 조회
    ALL_LIBRARY_HAS_THE_BOOK("http://data4library.kr/api/libSrchByBook?authKey=[발급받은키]&isbn=[ISBN]&region=[지역코드]");

    private final String url;
    SearchUrl(String url) {
        this.url = url;
    }
}

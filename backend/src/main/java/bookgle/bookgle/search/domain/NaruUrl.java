package bookgle.bookgle.search.domain;

//@Getter
public enum NaruUrl {
    // scheme
    SCHEME("http"),
    // host
    HOST("data4library.kr"),
    // 도서관별 소장 도서 조회
    BOOKS_BY_LIBRARY("/api/itemSrch"),
    // 도서 소장 도서관 조회
    ALL_LIBRARY_HAS_THE_BOOK("/api/libSrchByBook"),
    // 도서 정보 조회
    BOOKS_INFO("/api/srchBooks"),
    // 도서관별 대출 가능여부 조회
    IS_BOOK_AVAILABLE("/api/bookExist"),
    // 정보공개 도서관 조회
    ALL_LIBRARIES("/api/libSrch");


    private final String url;
    NaruUrl(String url) {
        this.url = url;
    }
    public String get() {
        return url;
    }
}

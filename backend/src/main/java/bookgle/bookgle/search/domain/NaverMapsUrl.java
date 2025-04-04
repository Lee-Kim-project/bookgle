package bookgle.bookgle.search.domain;

import lombok.Getter;

@Getter
public enum NaverMapsUrl {
    SCHEME("https"),
    HOST("naveropenapi.apigw.ntruss.com"),
    GEOCODING("/map-geocode/v2/geocode");


    private final String url;
    NaverMapsUrl(String url) {
        this.url = url;
    }
    public String get() {
        return url;
    }
}

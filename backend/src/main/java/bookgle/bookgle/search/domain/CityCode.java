package bookgle.bookgle.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CityCode {
    SEOUL("서울", 11);

    private final String name;
    private final int code;
}

package bookgle.bookgle.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionCode {
    SEOUL(11);

    private final int code;
}

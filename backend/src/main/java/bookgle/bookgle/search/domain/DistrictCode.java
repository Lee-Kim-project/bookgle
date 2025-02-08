package bookgle.bookgle.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DistrictCode {

    Gangnamgu ("강남구", 11230),
    Gangdonggu ("강동구", 11250),
    Gangbukgu ("강북구", 11090),
    Gangseogu ("강서구", 11160),
    Gwanakgu ("관악구", 11210),
    Gwangjingu ("광진구", 11050),
    Gurogu ("구로구", 11170),
    Geumcheongu ("금천구", 11180),
    Nowongu ("노원구", 11110),
    Dobonggu ("도봉구", 11100),
    Dongdaemungu ("동대문구", 11060),
    Dongjakgu ("동작구", 11200),
    Mapogu ("마포구", 11140),
    Seodaemungu ("서대문구", 11130),
    Seochogu ("서초구", 11220),
    Seongdonggu ("성동구", 11040),
    Seongbukgu ("성북구", 11080),
    Songpagu ("송파구", 11240),
    Yangcheongu ("양천구", 11150),
    Yeongdeungpogu ("영등포구", 11190),
    Yongsangu ("용산구", 11030),
    Eunpyeonggu ("은평구", 11120),
    Jongnogu ("종로구", 11010),
    Junggu ("중구", 11020),
    Jungnanggu ("중랑구", 11070);

    private final String name;
    private final int code;
}

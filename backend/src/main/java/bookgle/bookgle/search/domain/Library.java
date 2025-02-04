package bookgle.bookgle.search.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // json파일의 속성들 중에서 Book 클래스에 정의된 속성들만 가져온다.
public class Library {
    private final String name;
    private final String address;
    private final String latitude;
    private final String longitude;
    private final String homepage;

    @JsonCreator
    public Library(@JsonProperty("libName") String name, @JsonProperty("address") String address,
                   @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude,
                   @JsonProperty("homepage") String homepage) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.homepage = homepage;
    }
}

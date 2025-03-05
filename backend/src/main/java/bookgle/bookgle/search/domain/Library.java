package bookgle.bookgle.search.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // json에 있는 속성들 중에서 Library 클래스에 정의된 속성들만 가져온다.
public class Library {
    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    private String address;
    private String tel;
    private String latitude;
    private String longitude;
    private String homepage;

    @Column(columnDefinition = "TEXT")
    private String operatingTime;

    @JsonCreator
    public Library(@JsonProperty("libCode") String code, @JsonProperty("libName") String name, @JsonProperty("address") String address,
                   @JsonProperty("tel") String tel, @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude,
                   @JsonProperty("homepage") String homepage, @JsonProperty("operatingTime") String operatingTime) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.homepage = homepage;
        this.operatingTime = operatingTime;
    }
}

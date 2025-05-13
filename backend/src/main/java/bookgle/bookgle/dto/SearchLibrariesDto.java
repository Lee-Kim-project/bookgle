package bookgle.bookgle.dto;

import bookgle.bookgle.search.domain.Library;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchLibrariesDto {
    private List<Library> libraries;
    private String clientId;
    private String isbn;
}

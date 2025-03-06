package bookgle.bookgle.search.repository;

import bookgle.bookgle.search.domain.Library;
import bookgle.bookgle.search.projection.LibraryCodeLatLng;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, String> {
    List<LibraryCodeLatLng> findByCodeIn(List<String> codes);

}

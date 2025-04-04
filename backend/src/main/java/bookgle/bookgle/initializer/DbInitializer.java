package bookgle.bookgle.initializer;

import bookgle.bookgle.search.domain.CityCode;
import bookgle.bookgle.search.domain.Library;
import bookgle.bookgle.search.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DbInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final SearchService searchService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDb() throws JsonProcessingException {
        try {
            // 테이블 존재 여부 확인
            jdbcTemplate.queryForObject("SELECT 1 FROM library LIMIT 1", Integer.class);
            // 테이블 데이터 개수 확인
            int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM library", Integer.class);
            if (rowCount == 0) {
                System.err.println("테이블에 데이터가 없습니다. 테이블을 초기화합니다.");
                initLibraryTable();
            } else {
                System.out.println("테이블이 이미 존재하고 데이터가 있습니다.");
            }
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.err.println("테이블이 존재하지 않습니다. 테이블을 생성하고 초기화합니다.");
            initLibraryTable();
        }
    }

    // Library table을 초기화합니다.
    @Transactional // 함수가 정상적으로 완료되면 트랜잭션을 자동으로 커밋
    public void initLibraryTable() throws JsonProcessingException {
        List<Library> libraries = searchService.searchAllLibrariesByCity(CityCode.SEOUL.getCode());
        for (Library library: libraries) {
            searchService.updateLntLngOfLibraryFromApi(library);
        }
        searchService.saveLibraries(libraries);
    }
}
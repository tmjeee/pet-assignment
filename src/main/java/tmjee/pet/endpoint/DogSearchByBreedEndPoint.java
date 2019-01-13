package tmjee.pet.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tmjee.pet.commons.DbService;
import tmjee.pet.commons.EndPointResponse;
import tmjee.pet.config.PetAppConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dog")
public class DogSearchByBreedEndPoint {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        public final Integer id;
        public final String breedName;
        public final String creationDate;
        public final String url;
        public final String s3Key;
        Response (Integer id,
                  String breedName,
                  LocalDateTime creationDate,
                  String url,
                  String s3Key) {
            this.id = id;
            this.breedName = breedName;
            this.creationDate = creationDate.format(
                    DateTimeFormatter.ofPattern(PetAppConfig.DATE_TIME_PATTERN));
            this.url =url;
            this.s3Key = s3Key;
        }
    }

    @Autowired
    private DbService dbService;

    @Transactional
    @RequestMapping(value="breed/{breedName}", method = RequestMethod.GET)
    public EndPointResponse<List<Response>> search(@PathVariable("breedName")String breedName) {
        List<DbService.DogRetrievalOutput> dogRetrievalOutputs = dbService.dogSearchByBreed(breedName);
        return new EndPointResponse<>(
                true,
                List.of("Success"),
                dogRetrievalOutputs
                        .stream()
                        .map((DbService.DogRetrievalOutput d) -> new Response(
                                d.id,
                                d.breedName,
                                d.date,
                                d.s3LocationUrl,
                                d.s3Key
                        ))
                        .collect(Collectors.toList())
                );
    }


}

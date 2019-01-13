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

@RestController
@RequestMapping("/dog")
public class RetrievalEndPoint {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static final class Response {
       public final Integer id;
       public final String s3Key;
       public final String breedName;
       public final String url;
       public final String date;
       Response(Integer id, String s3Key, String breedName, String url, LocalDateTime date) {
           this.id = id;
           this.s3Key = s3Key;
           this.breedName = breedName;
           this.url = url;
           this.date = date.format(DateTimeFormatter.ofPattern(PetAppConfig.DATE_TIME_PATTERN));
       }
    }

    @Autowired
    private DbService dbService;

    @Transactional
    @RequestMapping(value="{id}", method = RequestMethod.GET)
    public EndPointResponse<Response> get(@PathVariable("id")Integer id) {
        DbService.DogRetrievalOutput dogOutput = dbService.dogRetrieval(id);
        return new EndPointResponse<>(
                true,
                List.of("Success"),
                new Response(
                        dogOutput.id,
                        dogOutput.s3Key,
                        dogOutput.breedName,
                        dogOutput.s3LocationUrl,
                        dogOutput.date
        ));
    }


}

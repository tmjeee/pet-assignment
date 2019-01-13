package tmjee.pet.commons;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DbService {



    public static class DogRetrievalOutput {
        public final Integer id;
        public final String breedName;
        public final LocalDateTime date;
        public final String s3LocationUrl;
        public final String s3Key;
        public DogRetrievalOutput(Integer id,
                                  String breedName,
                                  LocalDateTime date,
                                  String s3LocationUrl,
                                  String s3Key) {
            this.id = id;
            this.breedName = breedName;
            this.date = date;
            this.s3LocationUrl = s3LocationUrl;
            this.s3Key = s3Key;
        }
    }

    public static class DogInsertionInput {
        public final String breedName;
        public final LocalDateTime date;
        public final String s3LocationUrl;
        public final String s3Key;
        public DogInsertionInput(String breedName,
                                 LocalDateTime date,
                                 String s3LocationUrl,
                                 String s3Key) {
            this.breedName = breedName;
            this.date = date;
            this.s3LocationUrl = s3LocationUrl;
            this.s3Key = s3Key;
        }
    }

    public static class DogInsertionOutput {
        public final Integer id;
        DogInsertionOutput(Integer id) {
            this.id = id;
        }
    }

    @Autowired
    private Jdbi jdbi;

    public DogInsertionOutput dogInsertion(DogInsertionInput dogInfo) {
        Integer id = jdbi.inTransaction((Handle h)->{
            return h.createUpdate(
                    "INSERT INTO TBL_STORAGE (BREED_NAME, DATE, S3_LOCATION, S3_KEY) " +
                            "VALUES (:breed_name, :date, :s3_location, :s3_key)")
                    .bind("breed_name", dogInfo.breedName)
                    .bind("date", dogInfo.date)
                    .bind("s3_location", dogInfo.s3LocationUrl)
                    .bind("s3_key", dogInfo.s3Key)
                    .executeAndReturnGeneratedKeys("ID")
                    .mapTo(Integer.class)
                    .findOnly();
        });
        return new DogInsertionOutput(id);
    }


    public DogRetrievalOutput dogRetrieval(Integer id) throws DogNotFoundException {
        return jdbi.inTransaction((Handle h)->
            h.createQuery(
                    "SELECT ID, BREED_NAME, DATE, S3_LOCATION, S3_KEY FROM TBL_STORAGE " +
                            "WHERE ID = :id")
                    .bind("id", id)
                    .map(new RowMapper<DogRetrievalOutput>() {
                        @Override
                        public DogRetrievalOutput map(ResultSet rs, StatementContext ctx) throws SQLException {
                            return new DogRetrievalOutput(
                                    rs.getInt("ID"),
                                    rs.getString("BREED_NAME"),
                                    rs.getTimestamp("DATE").toLocalDateTime(),
                                    rs.getString("S3_LOCATION"),
                                    rs.getString("S3_KEY")
                            );
                        }
                    })
                    .findFirst()
                    .orElseThrow(()->new DogNotFoundException(String.format("Dog with id %s is not found", id)))
        );
    }


    public void dogRemoval(Integer id) {
        jdbi.useTransaction((Handle h)-> {
                int i = h.createUpdate(
                        "DELETE FROM TBL_STORAGE WHERE ID = :id")
                        .bind("id", id)
                        .execute();
                if (i == 0) {
                    throw new DogNotFoundException(String.format("Dog with id %s not found", id));
                }
        });
    }


    public List<DogRetrievalOutput> dogSearchByBreed(String breedName) {
        return jdbi.inTransaction((Handle h) ->
            h.createQuery(
                    "SELECT ID, BREED_NAME, DATE, S3_LOCATION, S3_KEY FROM TBL_STORAGE " +
                            "WHERE LOWER(BREED_NAME) LIKE :breed_name")
                    .bind("breed_name", "%" + breedName.toLowerCase() + "%")
                    .map((r, columnNumber, ctx) ->
                            new DogRetrievalOutput(
                                    r.getInt("ID"),
                                    r.getString("BREED_NAME"),
                                    r.getTimestamp("DATE").toLocalDateTime(),
                                    r.getString("S3_LOCATION"),
                                    r.getString("S3_KEY")
                            )
                    ).list()
        );
    }

    public List<DogRetrievalOutput> allDogs() {
        return jdbi.inTransaction((Handle h) ->
                h.createQuery(
                        "SELECT ID, BREED_NAME, DATE, S3_LOCATION, S3_KEY FROM TBL_STORAGE ")
                        .map((r, columnNumber, ctx) ->
                                new DogRetrievalOutput(
                                        r.getInt("ID"),
                                        r.getString("BREED_NAME"),
                                        r.getTimestamp("DATE").toLocalDateTime(),
                                        r.getString("S3_LOCATION"),
                                        r.getString("S3_KEY")
                                )
                        ).list()
        );
    }
}

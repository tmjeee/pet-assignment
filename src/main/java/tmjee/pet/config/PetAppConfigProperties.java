package tmjee.pet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="petapp.config")
public class PetAppConfigProperties {

    private Hsqldb hsqldb = new Hsqldb();
    private Aws aws = new Aws();

    public Hsqldb getHsqldb() {
        return hsqldb;
    }

    public Aws getAws() {
        return aws;
    }

    public static class Aws {
        private String bucketName;
        private String bucketRegion;

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
        public String getBucketName() {
            return bucketName;
        }

        public void setBucketRegion(String bucketRegion) {
            this.bucketRegion = bucketRegion;
        }
        public String getBucketRegion() {
            return this.bucketRegion;
        }
    }

    public static class Hsqldb {
        private String dbName;
        private String dbPath;
        private int dbPort;

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }
        public String getDbName() {
            return dbName;
        }

        public void setDbPath(String dbPath) {
            this.dbPath = dbPath;
        }
        public String getDbPath() {
            return this.dbPath;
        }

        public void setDbPort(int dbPort) {
            this.dbPort = dbPort;
        }
        public int getDbPort() {
            return this.dbPort;
        }
    }
}

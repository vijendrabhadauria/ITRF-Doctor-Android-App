package itrf.doctor.support;

public class Const {
    //  RunConfig - Possible values: production-env/test-env/localhost
    RunConfig = "test-env",
    HTTP_TEST_ENV_URL = "http://itrfhealthcaretest.ap-south-1.elasticbeanstalk.com/webresources/",
    HTTP_LOCALHOST_URL = "http://192.168.1.5:8080/ITRFHealthcare/webresources/",
    SSL_PROD_ENV_URL = "https://itrf.in/webresources/",
    HTTP_PROD_ENV_URL = "http://itrfhealthcare.ap-south-1.elasticbeanstalk.com/webresources/",
    ServerUrl = RunConfig.equalsIgnoreCase("test-env") ? HTTP_TEST_ENV_URL :
            (RunConfig.equalsIgnoreCase("production-env") ? SSL_PROD_ENV_URL :
            (RunConfig.equalsIgnoreCase("localhost") ? HTTP_LOCALHOST_URL :
            "Invalid RunConfig value"));

    public static String doctorAppVersion = "16";
    public static String doctorAppVersion_Display = "1.6";
}
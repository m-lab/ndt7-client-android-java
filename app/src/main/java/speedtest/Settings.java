package speedtest;

public class Settings {
    private String hostnameDownload;
    private String hostnameUpload;

    private int port = 0;
    private boolean skipTlsCertificateVerification = false;

    public Settings(String hostnameDownload, String hostnameUpload, int port, boolean skipTlsCertificateVerification) {
        this.hostnameDownload = hostnameDownload;
        this.hostnameUpload = hostnameUpload;
        this.port = port;
        this.skipTlsCertificateVerification = skipTlsCertificateVerification;
    }

    public String getHostnameDownload() {
        return hostnameDownload;
    }
    public String getHostnameUpload(){
        return hostnameUpload;
    }

    public void setHostnameDownload(String hostnameDownload) {
        this.hostnameDownload = hostnameDownload;
    }

    public void setHostnameUpload(String hostnameUpload) {
        this.hostnameUpload = hostnameUpload;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSkipTlsCertificateVerification() {
        return skipTlsCertificateVerification;
    }

    public void setSkipTlsCertificateVerification(boolean skipTlsCertificateVerification) {
        this.skipTlsCertificateVerification = skipTlsCertificateVerification;
    }
}

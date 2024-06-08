package kafka.lab.dto;

public class UploadDTO {

    private String uploadId;
    private String fileStore;
    private String fileId;
    private String targetFileStore;
    private String targetFileId;
    private String fileName;

    // Constructor vacío (necesario para la deserialización)
    public UploadDTO() {
    }

    // Getters y setters
    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getFileStore() {
        return fileStore;
    }

    public void setFileStore(String fileStore) {
        this.fileStore = fileStore;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTargetFileStore() {
        return targetFileStore;
    }

    public void setTargetFileStore(String targetFileStore) {
        this.targetFileStore = targetFileStore;
    }

    public String getTargetFileId() {
        return targetFileId;
    }

    public void setTargetFileId(String targetFileId) {
        this.targetFileId = targetFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // Método toString para representación de cadena
    @Override
    public String toString() {
        return "UploadDTO{" +
                "uploadId='" + uploadId + '\'' +
                ", fileStore='" + fileStore + '\'' +
                ", fileId='" + fileId + '\'' +
                ", targetFileStore='" + targetFileStore + '\'' +
                ", targetFileId='" + targetFileId + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}

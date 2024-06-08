package kafka.lab.test;

import kafka.lab.events.v1.UploadCompleted;

import java.util.Random;
import java.util.UUID;

public class MessageGenerator {
    private static final Random RANDOM = new Random();

    public static UploadCompleted generateRandomMessage(String fileExtension) {
        UploadCompleted uploadCompleted = new UploadCompleted();
        uploadCompleted.setUploadId(UUID.randomUUID().toString());
        uploadCompleted.setFileStore("mystore");
        uploadCompleted.setFileId(UUID.randomUUID().toString());
        uploadCompleted.setTargetFileStore("s3");
        uploadCompleted.setTargetFileId(UUID.randomUUID().toString());
        uploadCompleted.setFileName("example" + fileExtension);
        return uploadCompleted;
    }
}


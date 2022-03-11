package com.example.videohosting.data.servicesImpl.service_exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
@Slf4j
public class VideoNotFoundException extends Exception {
    public VideoNotFoundException(String message) {
        log.info(String.format("%s is not found!",message));
    }
}

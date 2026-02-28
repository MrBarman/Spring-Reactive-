package org.spring.HandleTransformErrorDo.Assignment;

import reactor.core.publisher.Flux;

import java.nio.file.Path;

/**
 * Task --->
  - do the work only when it is subscribed
  - do the work based on demand
  - stop producing when subscriber cancels
  - produce only requested items
  - file should be closed once done
 */

public interface FileReaderService {
    Flux<String> read(Path path);
}

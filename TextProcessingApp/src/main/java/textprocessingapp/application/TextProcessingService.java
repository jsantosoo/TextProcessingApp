package textprocessingapp.application;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import textprocessingapp.dto.ParagraphProcessDto;
import textprocessingapp.port.ParagraphsFetcherPort;
import textprocessingapp.port.ResultSenderPort;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextProcessingService {

  private final ParagraphsFetcherPort paragraphsFetcherPort;
  private final ResultSenderPort resultSenderPort;

  public ParagraphProcessDto processParagraphs(int paragraphCount) {
    log.info("Starting to process {} paragraphs", paragraphCount);

    Instant totalStartTime = Instant.now();

    List<String> paragraphs = fetchParagraphsConcurrently(paragraphCount);

    String mostFrequentWord = findMostFrequentWord(paragraphs);
    double averageParagraphSize =
        calculateAverage(paragraphs.stream().mapToInt(String::length).toArray());
    long totalProcessingTime = calculateTotalProcessingTime(totalStartTime);

    ParagraphProcessDto result =
        buildResultDto(mostFrequentWord, averageParagraphSize, totalProcessingTime);
    resultSenderPort.sendResult(result, mostFrequentWord);

    return result;
  }

  private List<String> fetchParagraphsConcurrently(int paragraphCount) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      return IntStream.range(0, paragraphCount)
          .mapToObj(i -> executor.submit(paragraphsFetcherPort::fetchParagraph))
          .map(
              future -> {
                try {
                  return future.get();
                } catch (Exception e) {
                  log.error("Error fetching paragraph", e);
                  return "";
                }
              })
          .toList();
    }
  }

  private String findMostFrequentWord(List<String> paragraphs) {
    Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
    return paragraphs.parallelStream()
        .flatMap(
            paragraph ->
                wordPattern
                    .matcher(paragraph.toLowerCase())
                    .results()
                    .map(matchResult -> matchResult.group()))
        .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("");
  }

  private double calculateAverage(int[] values) {
    return values.length == 0 ? 0 : Arrays.stream(values).average().orElse(0);
  }

  private long calculateTotalProcessingTime(Instant startTime) {
    return Duration.between(startTime, Instant.now()).toMillis();
  }

  private ParagraphProcessDto buildResultDto(
      String mostFrequentWord, double averageParagraphSize, long totalProcessingTime) {
    log.info("Most frequent word: {}", mostFrequentWord);
    log.info("Average paragraph size: {}", averageParagraphSize);
    log.info("Total processing time: {}", totalProcessingTime);

    return new ParagraphProcessDto(mostFrequentWord, averageParagraphSize, totalProcessingTime);
  }
}

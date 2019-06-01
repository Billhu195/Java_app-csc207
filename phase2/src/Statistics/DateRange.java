package Statistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * https://stackoverflow.com/questions/38220543/java-8-localdate-how-do-i-get-all-dates-between-two-dates
 * A class to find list of all dates between two dates.
 */
class DateRange implements Iterable<LocalDate> {
  /*the start date of current date range*/
  private final LocalDate START_DATE;
  /*the end date of current date range*/
  private final LocalDate END_DATE;

  /**
   * Construct a new Date Range, and check that range is valid (null, start < end)
   * @param startDate LocalDate, the start date
   * @param endDate LocalDate, the end date
   */
  DateRange(LocalDate startDate, LocalDate endDate) {
    this.START_DATE = startDate;
    this.END_DATE = endDate;
  }

  /**
   * Build an Iterator of Stream<LocalDate>
   * @return Iterator<LocalDate>, the iterator
   */
  @Override
  public Iterator<LocalDate> iterator() {
    return stream().iterator();
  }

  /**
   * Return the Stream with all LocalDates from START_DATE to END_DATE
   * @return Stream<LocalDate>, the Stream contain all LocalDates from START_DATE to END_DATE
   */
  private Stream<LocalDate> stream() {
    return Stream.iterate(START_DATE, d -> d.plusDays(1))
        .limit(ChronoUnit.DAYS.between(START_DATE, END_DATE) + 1);
  }

  /**
   * Return an array list of Local Date by the values in DateRange
   * @return ArrayList<LocalDate>, an array list contain all LocalDate from the start date to end date
   */
  ArrayList<LocalDate> toList() { //could also be built from the stream() method
    ArrayList<LocalDate> dates = new ArrayList<> ();
    for (LocalDate d = START_DATE; !d.isAfter(END_DATE); d = d.plusDays(1)) {
      dates.add(d);
    }
    return dates;
  }
}
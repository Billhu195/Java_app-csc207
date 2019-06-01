class Time {/*24hour time format*/

  /** current hour*/
  private int hour;
  /**current minutes*/
  private int minutes;

  /**
   * set hour of Time into new hour
   * @param hour new hour
   */
  private void setHour(int hour) {
    this.hour = hour;
  }

  /**
   * set minutes of Time into new minutes
   * @param minutes new minutes
   */
  private void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  /**
   * Construct a new Time
   * @param hours current hours
   * @param minute current minutes
   */
  Time(int hours, int minute){
    setHour(hours);
    setMinutes(minute);
  }

  /**
   * find the time interval between this Time and other Time
   * @param other other Time
   * @return the minutes between these two times
   */
  int timeInterval(Time other){
    return (this.hour - other.hour) * 60 + (this.minutes - other.minutes);
  }

  /**
   * change Time object into string
   * @return the string format of Time
   */
  @Override
  public String toString() {
    return hour + ":" + minutes;
  }
}

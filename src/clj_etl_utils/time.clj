(ns clj-etl-utils.time
  (:use
   [clj-etl-utils.lang-utils :only [raise]])
  (:import
   [org.joda.time LocalDate DateTime Days DateTimeConstants]))

(defn date-seq [start-date end-date]
  (take-while
   #(or (.isBefore %1 end-date)
        (.isEqual  %1 end-date))
   (iterate (fn [d]
              (.plusDays d 1))
            start-date)))

(defn same-day? [t1 t2]
  (let [d1 (.toLocalDate t1)
        d2 (.toLocalDate t2)]
    (.isEqual d1 d2)))

(def days-of-week-long
     {DateTimeConstants/SUNDAY    "Sunday"
      DateTimeConstants/MONDAY    "Monday"
      DateTimeConstants/TUESDAY   "Tuesday"
      DateTimeConstants/WEDNESDAY "Wednesday"
      DateTimeConstants/THURSDAY  "Thursday"
      DateTimeConstants/FRIDAY    "Friday"
      DateTimeConstants/SATURDAY  "Saturday"})

(defn day-of-week-long [dt]
  (get days-of-week-long (.getDayOfWeek dt)))

(def days-of-week-abbr
     {DateTimeConstants/SUNDAY    "Sun"
      DateTimeConstants/MONDAY    "Mon"
      DateTimeConstants/TUESDAY   "Tue"
      DateTimeConstants/WEDNESDAY "Wed"
      DateTimeConstants/THURSDAY  "Thu"
      DateTimeConstants/FRIDAY    "Fri"
      DateTimeConstants/SATURDAY  "Sat"})

(defn day-of-week-abbr [dt]
  (get days-of-week-abbr (.getDayOfWeek dt)))

(def days-of-week-short
     {DateTimeConstants/SUNDAY    "S"
      DateTimeConstants/MONDAY    "M"
      DateTimeConstants/TUESDAY   "T"
      DateTimeConstants/WEDNESDAY "W"
      DateTimeConstants/THURSDAY  "Th"
      DateTimeConstants/FRIDAY    "F"
      DateTimeConstants/SATURDAY  "S"})




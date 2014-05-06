(ns clj-etl-utils.time
  (:require
   [clj-etl-utils.lang-utils :refer [raise]]
   [clj-time.core            :as time]
   [clj-time.format          :as tformat])
  (:import
   [org.joda.time LocalDate DateTime Days DateTimeConstants DateTimeZone Minutes]))

(defn date-seq
  "Sequence of timestamps, each a day ahead of the previous."
  [start-date end-date]
  (take-while
   #(or (.isBefore %1 end-date)
        (.isEqual  %1 end-date))
   (iterate (fn [d]
              (.plusDays d 1))
            start-date)))

(defn same-day?
  "Test if the two DateTime's represent the same day."
  [t1 t2]
  (let [d1 (.toLocalDate t1)
        d2 (.toLocalDate t2)]
    (.isEqual d1 d2)))

(def *days-of-week-long*
     {DateTimeConstants/SUNDAY    "Sunday"
      DateTimeConstants/MONDAY    "Monday"
      DateTimeConstants/TUESDAY   "Tuesday"
      DateTimeConstants/WEDNESDAY "Wednesday"
      DateTimeConstants/THURSDAY  "Thursday"
      DateTimeConstants/FRIDAY    "Friday"
      DateTimeConstants/SATURDAY  "Saturday"})

(defn day-of-week-long [dt]
  (get *days-of-week-long* (.getDayOfWeek dt)))

(def *days-of-week-abbr*
     {DateTimeConstants/SUNDAY    "Sun"
      DateTimeConstants/MONDAY    "Mon"
      DateTimeConstants/TUESDAY   "Tue"
      DateTimeConstants/WEDNESDAY "Wed"
      DateTimeConstants/THURSDAY  "Thu"
      DateTimeConstants/FRIDAY    "Fri"
      DateTimeConstants/SATURDAY  "Sat"})

(defn day-of-week-abbr [dt]
  (get *days-of-week-abbr* (.getDayOfWeek dt)))

(def *days-of-week-short*
     {DateTimeConstants/SUNDAY    "S"
      DateTimeConstants/MONDAY    "M"
      DateTimeConstants/TUESDAY   "T"
      DateTimeConstants/WEDNESDAY "W"
      DateTimeConstants/THURSDAY  "Th"
      DateTimeConstants/FRIDAY    "F"
      DateTimeConstants/SATURDAY  "S"})


(defonce hour-minute-time-formatter (tformat/formatter "HH:mm"))

(defn make-time-zone-for-id
  "Helper that translates coloquial time zones (eg: EDT and PDT) to the official zone.  Returns a Joda DateTimeZone."
  [^String id]
  (cond
    (nil? id)
    (DateTimeZone/forID "EST5EDT")

    (= "EDT" id)
    (DateTimeZone/forID "EST5EDT")

    (= "PDT" id)
    (DateTimeZone/forID "PST8PDT")

    (= "PST" id)
    (DateTimeZone/forID "America/Los_Angeles")

    :otherwise
    (DateTimeZone/forID id)))

(defn mins-between [^String start-hour-min ^String end-hour-min]
  (let [stime                (tformat/parse hour-minute-time-formatter start-hour-min)
        etime                (tformat/parse hour-minute-time-formatter end-hour-min)
        end-is-before-start? (.isBefore etime stime)
        etime                (if end-is-before-start?
                               (time/plus etime (time/minutes (* 24 60)))
                               etime)
        shour                (.getHourOfDay stime)
        smin                 (.getMinuteOfHour stime)
        ehour                (.getHourOfDay etime)
        emin                 (.getMinuteOfHour etime)
        diff                 (Minutes/minutesBetween stime etime)]
    (.getMinutes diff)))

(defn translate-time-of-day-to-utc-time-stamp
  "
    (translate-time-of-day-to-utc-time-of-day \"09:00\" \"EDT\")
      => ^DateTime timestamp
"
  [^String hour-of-day ^org.joda.time.DateTime tstamp ^String tz]
  (let [htime  (tformat/parse hour-minute-time-formatter hour-of-day)
        tstamp (time/to-time-zone tstamp (make-time-zone-for-id tz))
        tstamp (.withTime
                tstamp
                (.getHourOfDay    htime)
                (.getMinuteOfHour htime)
                0 0)
        tstamp (time/to-time-zone tstamp (DateTimeZone/forID "UTC"))]
    tstamp))

(comment

  (hour-span-to-time-stamps current-time start-hour end-hour tzone)

  (translate-time-of-day-to-utc-time-stamp hour-of-day tstamp tz)
  )

(defn hour-span-to-time-stamps
  " (hour-span-to-time-stamps tstamp \"09:00\" \"17:00\" \"EDT\")  "
  [^DateTime current-time ^String start-hour ^String end-hour ^String tzone]
  (let [start-tstamp (translate-time-of-day-to-utc-time-stamp start-hour current-time tzone)
        mins         (mins-between start-hour end-hour)
        end-tstamp   (time/plus start-tstamp (time/minutes mins))]
    [start-tstamp end-tstamp]))

(defn minutes-into-day [^org.joda.time.DateTime dt]
  (+
   (* 60 (.getHourOfDay dt))
   (.getMinuteOfHour dt)))


(defn during-business-hours?
  "
    (during-business-hours?
      (time/now)
      \"09:00\"
      \"17:00\"
      \"EDT\")

"
  [^org.joda.time.DateTime current-time ^String start-hour-min ^String end-hour-min ^String tz]
  (let [current-time        (time/to-time-zone current-time (DateTimeZone/forID "UTC"))
        [bus-start bus-end] (hour-span-to-time-stamps
                             current-time
                             start-hour-min
                             end-hour-min
                             tz)
        current-mins      (minutes-into-day current-time)
        bus-start-mins    (minutes-into-day bus-start)
        bus-end-mins      (minutes-into-day bus-end)]
    (if (< bus-end-mins bus-start-mins)
      (or (>= current-mins bus-start-mins)
          (<= current-mins bus-end-mins))
      (<= bus-start-mins current-mins bus-end-mins))))

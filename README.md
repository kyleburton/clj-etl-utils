# clj-etl-utils

ETL Utilities for Clojure.  This library began with functions that worked with data on disk, such as database dumps and log files, at least that was the original purpose of the library, it has since grown to include other utilities.

NB: this library was created in 2010, since then many of the functions and utilities here are availabile in other libraries (eg: camel-snake-kebab).

## Modules

### `clj-etl-utils.io`

IO and File utilities.

#### string-reader, string-input-stream

Returns a Reader or an InputStream, respectively, that will read from the given string.

#### read-fixed-length-string

Reads a fixed-length string.

#### chmod

Changes the permissions on a file by shelling out to the `chmod` command.

#### mkdir

Creates the given directory, just returning true if the given directory already exists (as opposed to throwing an exception).

#### exists?

Tests if a file exists.

#### symlink

Establishes a symlink for a file.

#### freeze, thaw

freeze invokes the java serialization and returns a byte array.  Thaw does the opposite: takes a byte array and deserializes it.

#### object->file

Uses Java serialization to write an object to the given file, truncating if it exists.

#### file->object

Deserializes a serialized object from a file.

#### ensure-directory

Ensures a directory path exists (recursively), doing nothing if it already exists.

#### string-gzip

Compress a string, returning the bytes.

#### byte-partitions-at-line-boundaries

This can be used in divide and conquer scenarios where you want to process different segments of a single file in parallel.  It takes an input file name and a desired block size.  Block boundaries will be close to the desired size - the size is used as a seek position, any line remnant present at that position is read, such that a given block will end cleanly at a line boundary.

#### random-access-file-line-seq-with-limit

Returns a lazy sequence of lines from a RandomAccessFile up to a given limit.  If a line spans the limit, the entire line will be returned, so that a valid line is always returned.

#### read-lines-from-file-segment

Returns a sequence of lines from the file across the given starting and ending positions.

### `clj-etl-utils.landmark_parser`

Semi-structured Text parsing library.  The library uses a command set to extract portions of a text based document. Commands are instructions such as: start, end forward-char, backward-char, forward-past, rewind-to and so on. The landmarks can be either literal or logical (regular expressions, 'types', etc).  This extractor can often succeed in situations where there is mixed media (javascript embedded in html), or inconsisten structure (the outputs of pdf to text). It should be able to operate on a document as long as there are identifiable sets of landmarks.

```clojure
;; this example extracts the last line from the document (string)
(lp/extract
  (lp/make-parser "This is some text.  There is some in the middle.\nThere is some towards the end, but it is not this.\nA few sentences in all.")
  [[:end nil]
   [:rewind-to "\n"]]
   [:end nil])
```

### `clj-etl-utils.lang-utils`

`lang/make-periodic-invoker` can be used to easily create 'progress' indicators or bars

#### Example

```clojure
  (let [total         1000
        period        100
        started-at-ms (.getTime (java.util.Date.))
        progress      (make-periodic-invoker
                       period
                       (fn [status counter]
                         (let [elapsed-ms    (- (.getTime (java.util.Date.)) started-at-ms)
                               elapsed-secs  (/ elapsed-ms 1000)
                               num-remaining (- total counter)
                               rate-per-sec  (/ counter elapsed-secs)
                               eta-secs      (/ num-remaining rate-per-sec)]
                           (cond
                             (= status :final)
                             (printf "All Done! %d processed in %d seconds at %3.2f/s\n" counter (long elapsed-secs) (double rate-per-sec))

                             :else
                             (printf "So far we've completed %d of %d, we are  %3.2f%% complete at %3.2f per second, we should be done in %d seconds.\n"
                                     counter
                                     total
                                     (double (* 100.0 (/ counter total 1.0)))
                                     (double rate-per-sec)
                                     (long eta-secs))))))]
    (dotimes [ii total]
      ;; do some work / processing here
      (Thread/sleep ^long (rand-nth [0 1 2 3 4 5]))
      (progress))
    (progress :final)
    :done)
```

Produces the following output:

```text
So far we've completed 100 of 1000, we are  10.00% complete at 273.22 per second, we should be done in 3 seconds.
So far we've completed 200 of 1000, we are  20.00% complete at 294.55 per second, we should be done in 2 seconds.
So far we've completed 300 of 1000, we are  30.00% complete at 300.30 per second, we should be done in 2 seconds.
So far we've completed 400 of 1000, we are  40.00% complete at 298.73 per second, we should be done in 2 seconds.
So far we've completed 500 of 1000, we are  50.00% complete at 305.06 per second, we should be done in 1 seconds.
So far we've completed 600 of 1000, we are  60.00% complete at 308.01 per second, we should be done in 1 seconds.
So far we've completed 700 of 1000, we are  70.00% complete at 306.35 per second, we should be done in 0 seconds.
So far we've completed 800 of 1000, we are  80.00% complete at 308.76 per second, we should be done in 0 seconds.
So far we've completed 900 of 1000, we are  90.00% complete at 303.85 per second, we should be done in 0 seconds.
So far we've completed 1000 of 1000, we are  100.00% complete at 303.67 per second, we should be done in 0 seconds.
All Done! 1000 processed in 3 seconds at 303.58/s
```

### `clj-etl-utils.ref_data`

Static reference data and lookup tables

* USA State Names and Abbreviations
* USA Airport Codes
* USA Phone Number Area Codes
* ISO Country Codes

### `clj-etl-utils.regex`

Ready made Regular expressions to match items like numbers, iso country codes, USA postal codes, USA state names, USA phone numbers, ipv4 adresses, etc.

### `clj-etl-utils.sequences`

Sequence utilities, eg: `make-stream-sampler`, `make-reservoir-sampler`, `reservoir-sample-seq`.

### `clj-etl-utils.text`

Text manipulation helper functions, eg: `md5->string`, `string->md5`, `sha1->string`, `string->sha1`, `substr` that supports negative indexes to read from the right hand side, `human-readable-byte-count`, `canonical-phone-number`, `encode-base64`, `decode-base64`

### `clj-etl-utils.indexer`

Module for working with line-oriented data files in-situ on disk.  These tools allow you to create (somewhat) arbitrary indexes into a file and efficiently find lines based on the indexed values.

#### Example

Given the tab delimited file `file.txt`:

```
99	line with larger key
1	is is the second line
2	this is a line
3	this is another line
99	duplicated line for key
```

We can create an index on the `id` column id:

```clojure
(index-file! "file.txt" ".file.txt.id-idx" #(first (.split % "\t" 2)))
```

 That index can then be used to read groups of records from the file with the same key values:

```clojure
(record-blocks-via-index "file.txt" ".file.txt.id-idx")
```


```clojure
( [ "1\tis is the second line" ]
[ "2\tthis is a line" ]
[ "3\tthis is another line" ]
[ "99\tline with larger key"
"99\tduplicated line for key" ] )
```

## Installation

`clj-etl-utils` is available via Clojars:

  https://clojars.org/com.github.kyleburton/clj-etl-utils

## References

UTF and BOM

  http://unicode.org/faq/utf\_bom.html

## Random Sampling

  [How to pick a random sample from a list](http://www.javamex.com/tutorials/random\_numbers/random\_sample.shtml)

## Reference Data

### US Zip5 Codes

[Fun with Zip Codes](http://www.mattcutts.com/blog/fun-with-zip-codes/)

[US Census Tigerline Data: Zip Codes](http://www.census.gov/tiger/tms/gazetteer/zips.txt)


## License

This code is covered under the same as Clojure.

# Authors

Kyle Burton <kyle.burton@gmail.com>

Paul Santa Clara <kesserich1@gmail.com>

Tim Visher <tim.visher@gmail.com>

(ns clj-etl-utils.io
  (:use [clj-etl-utils.lang-utils :only (raise log)])
  (:require [clojure.contrib.shell-out           :as sh])
  (:import
   [java.io
    InputStream FileInputStream File InputStreamReader RandomAccessFile
    Reader FileReader]
   [org.apache.commons.io.input BoundedInputStream]))

;;
;; Unicode BOM handling is Based off of nu.xom.xinclude.EncodingHeuristics
;; FAIL: use the BOM stuff from commons-io!o

;; TODO: can fail for streams that don't support marking
(defn first-n-bytes-available [#^Reader stream n-bytes]
  (let [res (atom [])]
    (try
     (if (.markSupported stream)
       (.mark stream 1024))
     (dotimes [nn n-bytes]
       (let [next-byte (.read stream)]
         (if (not (= -1 next-byte))
           (reset! res (conj @res next-byte)))))
     (finally
      ;; NB: this is no good for already created streams IOW one's
      ;; that can be recreated, b/c the goal is to advance past the
      ;; BOM and no further, so we need to consume at most 1 byte at a
      ;; time
      (if (.markSupported stream)
        (.reset stream))))
    @res))

;; ;; from: http://unicode.org/faq/utf_bom.html
;; (def *utf-16be*
;;      {:encoding  "UTF-16BE"
;;       :name      :utf-16be
;;       :marker    "\u00FE\u00FF"
;;       :marker-bytes [0xFE 0xFF]})

;; (def *utf-16le*
;;      {:encoding  "UTF-16LE"
;;       :name      :utf-16le
;;       :marker    "\u00FF\u00FE"
;;       :marker-bytes [0xFF 0xFE]})

;; (def *utf-32be*
;;      {:encoding  "UTF-32BE"
;;       :name       :utf-32be
;;       :marker    "\u0000\u0000\u00FE\u00FF"
;;       :marker-bytes [0x00 0x00 0xFE 0xFF]})

;; (def *utf-32le*
;;      {:encoding  "UTF-32LE"
;;       :name      :utf-32le
;;       :marker    "\u00FF\u00FE\u0000\u0000"
;;       :marker-bytes [0xFF 0xFE 0x00 0x00]})

;; (def *utf-8*
;;      {:encoding  "UTF-8"
;;       :name      :utf-8
;;       :marker    "\u00EF\u00BB\u00BF"
;;       :marker-bytes [0xEF 0xBB 0xBF]})

;; (def *iso-8851-1*
;;      {:encoding  "ISO-8859-1"
;;       :name      :iso-8859-1
;;       :marker    ""
;;       :marker-bytes []})

;; (def *us-ascii*
;;      {:encoding  "US-ASCII"
;;       :name      :us-ascii
;;       :marker    ""
;;       :marker-bytes []})

;; (def *bom-markers*
;;      [*utf-32be*
;;       *utf-32le*
;;       *utf-16be*
;;       *utf-16le*
;;       *utf-8*])

;; (def *default-encoding* *iso-8851-1*)


(defn byte-marker-matches? [marker-bytes file-bytes]
  (cond
    (empty? marker-bytes) false
    (empty? file-bytes)   false
    :else
    (loop [[marker & marker-bytes] marker-bytes
           [byte & file-bytes]     file-bytes]
      (cond
        (or (not marker) (not byte))      true
        (= marker byte)                   (recur marker-bytes file-bytes)
        :else                             false))))

;; TODO: what if the stream doesn't support mark?
;; TODO: may return a false positive on arbitrary binary data
;; (defn detect-stream-encoding-via-bom [stream & [default-encoding]]
;;   (let [file-bytes (first-n-bytes-available stream 4)]
;;     (loop [[encoding & encodings] *bom-markers*]
;;       (cond
;;         (not encoding)
;;         ;; TODO: return the default encoding here
;;  	(or default-encoding *default-encoding*)
;;         (byte-marker-matches? (:marker-bytes encoding) file-bytes)
;;         encoding
;;         :else
;;         (recur encodings)))))




;; (defmulti detect-file-encoding-via-bom (fn [x & [default-encoding]] (class x)))

;; (defmethod detect-file-encoding-via-bom String [#^String file & [#^String default-encoding]]
;;   (detect-file-encoding-via-bom (File. file) default-encoding))

;; (defmethod detect-file-encoding-via-bom File [#^File file & [#^String default-encoding]]
;;   (with-open [inp (FileReader. file)]
;;     (detect-stream-encoding-via-bom inp default-encoding)))

;; (defmethod detect-file-encoding-via-bom :default [file & [default-encoding]]
;;   (throw (format "Error: fell through to :default for detect-stream-encoding-via-bom file=%s" file)))


;; (defn unicode-input-stream [#^String path]
;;   (InputStreamReader.
;;    (FileInputStream. path)
;;    #^String (:encoding (detect-file-encoding-via-bom path))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn string-reader [s]
  (java.io.StringReader. s))

(defn string-input-stream [#^String s]
  (java.io.ByteArrayInputStream. (.getBytes s)))

(defn read-fixed-length-string [#^InputStream inp nchars]
  "Read a specific number of characters from the InputStream, return a string."
  (let [dest #^bytes (make-array Byte/TYPE nchars)
        nread (.read inp dest 0 nchars)]
    (String. dest 0 nread)))

(defn drain-line-reader
  "Drain a buffered reader into a sequence."
  [#^java.io.BufferedReader rdr]
  (loop [res []
         line (.readLine rdr)]
    (if line
      (recur (conj res line)
             (.readLine rdr))
      res)))

(defn exec
  "Simple wrapper around Runtime.exec - not intended to compete with clojure.contrib.shell-out"
  [#^String cmd]
  (let [proc #^Process (.exec (Runtime/getRuntime) cmd)
        rv (.waitFor proc)]
    {:error (drain-line-reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getErrorStream proc))))
     :output (drain-line-reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getInputStream proc))))
     :exit rv}))

(defn chmod
  "Change a file or directory's permissions.  Shells out to perform the chmod."
  [perms file]
  (let [cmd (format "chmod %s %s" perms file)
        res (exec cmd)]
    (log "[INFO] chmod: %s" cmd)
    (if (not (= 0 (:exit res)))
      (log "[ERROR] %s" (:error res)))))

(defmacro with-tmp-dir [[var & [prefix suffix]] & body]
  `(let [prefix# ~prefix
         suffix# ~suffix
         ~var (java.io.File/createTempFile (or prefix# "pfx") (or suffix# "sfx"))]
     (try
      (do
        (.delete ~var)
        ~@body)
      (finally
       ;; TODO: this will fail if dir is not empty!, should this recrusively remove all the files?
       (.delete ~var)))))

(defn basename
  "Strip off the last part of the file name."
  [fname]
  (if (instance? java.io.File fname)
    (.getParent #^java.io.File fname)
    (.getParent (java.io.File. #^String (str fname)))))

(defn #^java.io.File $HOME
  "Construct a path relative to the user's home directory."
  [& paths]
  (java.io.File.
   #^String (apply str
                   (cons (str (System/getProperty "user.home") "/")
                         (apply str (interpose "/" paths))))))

(defmulti expand-file-name
  "Perform bash style expansion on the given path.  Eg: ~/file.txt."
  class)

(defn #^String get-user-home
  "Get the user's home dir as a string."
  []
  (System/getProperty "user.home"))

(defmethod expand-file-name String [#^String path]
  (cond (.startsWith path "~/")
        (.replaceFirst path "^~(/|$)" (str (get-user-home) "/"))
        (.startsWith path "file://~/")
        (.replaceFirst path "^file://~/" (str "file://" (get-user-home) "/"))
        :else
        path))


(defn mkdir
  "Create the given directory path, fall back gracefuly if the path
  exists, warning if it's not a directory."
  [path]
  (let [f (java.io.File. (str path))]
    (if (not (.exists f))
      (do
        ;(log "[INFO] mkdir: creating %s" path)
        (.mkdirs f)
        true)
      (if (not (.isDirectory f))
        (do
          ;(log "[WARN] mkdir: %s exists and is not a directory!" path)
          false)
        (do
          ;(log "[DEBUG] mkdir: exists: %s" path)
          true)))))

(defmulti  exists? class)
(defmethod exists? String   [#^String s] (.exists (File. s)))
(defmethod exists? File     [#^File f]   (.exists f))
(defmethod exists? :default [x] (throw (Exception. (str "Do not know how to test <" (pr-str x) "> if it `exists?'"))))


(defn drain-line-reader
  "Drain a buffered reader into a sequence."
  [#^java.io.BufferedReader rdr]
  (loop [res []
         line (.readLine rdr)]
    (if line
      (recur (conj res line)
             (.readLine rdr))
      res)))

(defn exec
  "Simple wrapper around Runtime.exec - not intended to compete with clojure.contrib.shell-out"
  [#^String cmd]
  (let [proc (.exec (Runtime/getRuntime) cmd)
        rv (.waitFor proc)]
    {:error (drain-line-reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getErrorStream proc))))
     :output (drain-line-reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getInputStream proc))))
     :exit rv}))

(defn symlink
  "Create a symlink."
  [#^String src #^String dst]
  (let [src (java.io.File. (str src))
        dst (java.io.File. (str dst))]
    (if (not (.exists src))
      (raise "symlink: src does not exist: %s" src))
    (if (.exists dst)
      (log "[INFO] symlink: dst exists %s => %s" src dst)
      (let [cmd (format "ln -s %s %s" src dst)
            res (exec cmd)]
        (log "[INFO] symlink: %s=>%s : %s" src dst cmd)
        (if (not (= 0 (:exit res)))
          (log "[ERROR] %s" (:error res)))))))

(defn delete
  "Remove a file if it exists."
  [#^String path]
  (let [path (java.io.File. (str path))]
    (if (.exists path)
      (.delete path))))

(defn url-get
  "Very simplistic retreival of a url target."
  [url]
  (with-open [is (.openStream (java.net.URL. url))]
    (loop [sb (StringBuffer.)
           chr (.read is)]
      (if (= -1 chr)
        sb
        (do
          (.append sb (char chr))
          (recur sb
                 (.read is)))))))

(defn url-download
  "Shell's out to wget to pull the file into the target directory."
  [url #^String target-dir]
  (let [cmd (format "wget -P %s -c %s" target-dir url)
        res (exec cmd)]
    (log "[INFO] wget: %s" cmd)
    (if (not (= 0 (:exit res)))
      (log "[ERROR] %s" (:error res)))))


(defn object->file [#^Object obj #^String file]
  "Use Java Serialization to emit an object to a file (binary format)."
  (with-open [outp (java.io.ObjectOutputStream. (java.io.FileOutputStream. file))]
    (.writeObject outp obj)))


(defn file->object [#^String file]
  "Use Java Serialization to pull an object from a file (see object->file)."
  (with-open [inp (java.io.ObjectInputStream. (java.io.FileInputStream. file))]
    (.readObject inp)))

;; clojure.lang.PersistentVector$Node ins't serializable any longer...is this an oversight? ignore for now...
(defn freeze [#^Object obj]
  "Serialize an object to a byte array."
  (with-open [baos (java.io.ByteArrayOutputStream. 1024)
              oos  (java.io.ObjectOutputStream. baos)]
    (.writeObject oos obj)
    (.toByteArray baos)))

;; (freeze "foo")
;; (freeze "foo" "bar" "qux")

(defn thaw [#^bytes bytes]
  "Deserialize from a byte array to the object."
  (with-open [bais (java.io.ByteArrayInputStream. bytes)
              ois  (java.io.ObjectInputStream. bais)]
    (.readObject ois)))

;; (thaw (freeze "foo"))

;; (object->file "foo" ($HOME "/foo.bin"))
;; (file->object ($HOME "/foo.bin"))

(defmacro with-stdout-to-file [file & body]
  "Wrap code, redirecting stdout to a file."
  `(with-open [out# (ds/writer ~file)]
     (binding [*out* out#]
       ~@body)))

(defmacro with-stderr-to-file [file & body]
  "Wrap code, redirecting stderr to a file."
  `(with-open [out# (ds/writer ~file)]
     (binding [*err* out#]
       ~@body)))


(defn ensure-directory
  "Create the directory if it does not already exist."
  [#^String dir]
  (let [f (java.io.File. dir)]
    (if (not (.exists f))
      (.mkdirs f))))

;; TODO: port to pure java, rm is unix specific...
(defn deltree
  "Remove the given directory tree, all files and subdirectories."
  [#^String dir]
  (sh/sh "rm" "-rf" dir))

;; TODO this doesn't belong in io.clj, couldn't think of a better place for it
(defn string-gzip [#^String s]
  (with-open [bout (java.io.ByteArrayOutputStream.)
              gzout (java.util.zip.GZIPOutputStream. bout)]
    (.write gzout (.getBytes s))
    (.finish gzout)
    (.toByteArray bout)))

(defn file-size [f]
  (.length (java.io.File. (str f))))

(defn byte-partitions-at-line-boundaries [#^String file-name desired-block-size-bytes]
  (let [fp (RandomAccessFile. file-name "r")
        file-length (.length fp)]
    (loop [byte-positions [0]
           next-seek-point desired-block-size-bytes]
      (if (>= next-seek-point file-length)
        (conj byte-positions file-length)
        (do
          (.seek fp next-seek-point)
          (if (nil? (.readLine fp))
            byte-positions
            (recur (conj byte-positions (.getFilePointer fp))
                   (+ (.getFilePointer fp) desired-block-size-bytes))))))))

;; (count (byte-partitions-at-line-boundaries "/tmp/test-file.txt" 100))

;; see: http://webcache.googleusercontent.com/search?q=cache:WK2by6blKLQJ:forums.sun.com/thread.jspa%3FthreadID%3D466733+java+randomaccessfile+slow&cd=2&hl=en&ct=clnk&gl=us
;; seek, then create a (FileReader. (.getFD fp))
;; and wrap it in a BufferedReader
;; that should allow faster readLine, then still consult fp.getFilePointer() for
;; the current seek point to stop reading perhaps?  needs to be thought out some more...

;; org.apache.commons.io.input.BoundedInputStream
;; (let [bis (BoundedInputStream. (doto
;;                                    (FileInputStream. fname)
;;                                  ))])


(defn- random-access-file-line-seq-with-limit [#^RandomAccessFile fp end]
  (if (>= (.getFilePointer fp) end)
    (do
      (.close fp)
      nil)
    (lazy-cat
     (loop [line (.readLine fp)
            lines []
            max 1]
       (if (or (not line)
               (= max 0))
         (conj lines line)
         (recur (.readLine fp)
                (conj lines line)
                (dec max))))
     (random-access-file-line-seq-with-limit fp end))))

;; (read-lines-from-file-segment file-name start end)
(defn read-lines-from-file-segment [#^String file-name start end]
  (let [fp (RandomAccessFile. file-name "r")]
    (.seek fp start)
    (random-access-file-line-seq-with-limit fp end)))



(comment
  (byte-partitions-at-line-boundaries "/tmp/test-file.txt" 100)
  (extract-lines-from-coordinates "/tmp/test-file.txt" [0 129])
  (extract-lines-from-coordinates "/tmp/test-file.txt" [129 258])

  (extract-lines-from-coordinates "/tmp/test-file.txt" [4347 4392])

  (=
   (apply
    concat
    (map (fn [[start end]] (read-lines-from-file-segment "/tmp/test-file.txt" start end))
         (partition 2 1 (byte-partitions-at-line-boundaries "/tmp/test-file.txt" 100))))
   (clojure.contrib.duck-streams/read-lines "/tmp/test-file.txt"))


  )










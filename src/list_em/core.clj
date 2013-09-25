(ns list-em.core
  (:require [clojure.string :only (join split)]
            [clojure.java.io]
            [me.raynes.fs :as fs])
  (:gen-class))

;; Helper Functions
(defn f-children [file]
  (.listFiles file))

;; List a directory path recursively, returning a vector
(defn list-dir [dir-name]
  ;; Create in function recursive loop
  (loop [accum [] [next-child & remaining-files] [dir-name]]
    (if-not next-child
      ;; Final return
      accum
      ;; File processing
      (let [file (fs/file next-child)]
        (if-not (fs/directory? file)
          ;; We found a file, let's add and iterate
          (recur
            (conj accum (str (fs/absolute-path file)))
            remaining-files)
          ;; We found a dir, let's add children, then recurse
          (recur
            accum (into (vec (seq (f-children file)))
            remaining-files)))))))

;; Save new local vector to specified file
(defn save-dir-list [file-name file-list]
  (doseq [file file-list]
    (spit file-name (str file "\n") :append true)))

;; Runner
(defn -main [& args]
  (try
    (let [directory-path (first args) save-file (second args)]
      (let [file-list (list-dir directory-path)]
        (save-dir-list save-file file-list)))
    (catch Exception e
      (println "USAGE: java -jar list-em.jar /path/to/dir /path/to/file.txt")))
  nil)

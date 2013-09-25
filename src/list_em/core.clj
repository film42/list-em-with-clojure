(ns list-em.core
  (:require [clojure.string :only (join split)]
            [clojure.java.io]
            [me.raynes.fs :as fs])
  (:gen-class))

;; Helper Functions
(defn f-children [file]
  (.listFiles file))

;; Global vector to hold file string list
(def master-list (atom []))

;; Recursively go through each directory
(defn list-dir [dir-name]
  (let [file (fs/file dir-name)]
    (if (not (fs/directory? file))
      ;; Is a file, let's save it
      (let [file-path (fs/absolute-path file)]
        (swap! master-list conj [file-path])
        (str file-path))
      ;; Is a directory, let's recurse
      (let [file-list (f-children file)]
        (println dir-name)
        ;; Send each child through list-dir and return
        (doseq [child file-list]
          (list-dir (fs/absolute-path child)))))))

;; Save global vector to specified file
(defn save-dir-list [file-name]
  (doseq [file @master-list]
;;    (println (first file))
    (spit file-name (str (first file) "\n") :append true)))

;; Runner
(defn -main [& args]
  (try
    (let [directory-path (first args) save-file (second args)]
      (list-dir directory-path)
      (save-dir-list save-file))
    (catch Exception e
      (println (str e))
      (println "USAGE: java -jar list-em.jar /path/to/dir /path/to/file.txt")))
  ;; Ensure we don't return anything
  nil)

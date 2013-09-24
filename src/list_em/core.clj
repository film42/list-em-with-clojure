(ns list-em.core
  (:require [clojure.string :only (join split)]
            [clojure.java.io])
  (:gen-class))

(def master-list (atom []))

(defn list-dir [dir-name]
  (let [file (new java.io.File dir-name)]
    (if (not (.isDirectory file))
      ;; Is a file, let's save it
      (let [file-path (.getAbsolutePath file)]
        (swap! master-list conj [file-path])
        (str file-path))
      ;; Is a directory, let's recurse
      (let [file-list (.listFiles file)]
        ;; Send each child through list-dir and return
        (doseq [child file-list]
          (list-dir (.getAbsolutePath child)))))))

(defn save-dir-list [file-name]
  (doseq [file @master-list]
    (spit file-name (str (first file) "\n") :append true)))

(defn -main [& args]
  (list-dir (first args))
  (save-dir-list (second args))
  ;; Ensure we don't return anything
  nil)
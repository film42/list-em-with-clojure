(ns list-em.core-test
  (:require [clojure.test :refer :all]
            [list-em.core :refer :all]
            [me.raynes.fs :as fs]))

;; Test Data
(def test-id (str (rand-int 3000)))
(def test-file-list
  ["/tmp" "/tmp/test.txt" "/tmp2" "/tmp2/test.txt"])

;; Print the test file vector to a file
(deftest save-file-list-to-file
  (let [save-file (str "/tmp/" test-id ".txt")]
    ;; First save the contents
    (save-dir-list save-file test-file-list)
    ;; Second we test the results
    (let [result-file (slurp save-file)]
      (is test-file-list result-file))))

;; Just the most basic
(deftest a-test
  (testing "I simply pass."
    (is (= 1 1))))



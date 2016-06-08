(ns tpc.test.api-test
  (:require [clojure.test :refer :all]
            [tpc.api :refer :all]))

(deftest merge-headers-test
  (testing "with am empty list"
    (is (= (merge-headers []) [])))
  (testing "with header and rows"
    (is (= (merge-headers [["h1" "h2"], [1 2], [3, 4]])
           [{"h1" 1, "h2" 2} {"h1" 3, "h2" 4}]))))

(ns tpc.test.api-test
  (:require [clojure.test :refer :all]
            [tpc.api :refer :all]
            [tpc.models.types :as types])
  (:import [tpc.models.types TPCEntry]))

(deftest merge-headers-test
  (testing "with am empty list"
    (is (= (merge-headers []) [])))
  (testing "with header and rows"
    (is (= (merge-headers [["h1" "h2"], [1 2], [3, 4]])
           [{"h1" 1, "h2" 2} {"h1" 3, "h2" 4}]))))

(def row_cr {"TPC\rCOMISSION" "87,00"})
(def row_normal {"TPC COMISSION" "96,00"})

(deftest row-to-tpcentry
  (testing "With a carriage return in the name of the comission"
    (is (type (Row->TPCEntry row_cr)) TPCEntry)
    (is (:tpc-com (Row->TPCEntry row_cr)) 87)
    (is (:tpc-com (Row->TPCEntry row_normal)) 96)))

(ns tpc.test.routes.home-test
  (:require [clojure.test :refer :all]
            [tpc.routes.home :refer :all]))

(deftest display-amounts-test
  (testing "returns a paragraph without any amounts"
    (is (= (display-amounts {}) [:p "Upload a bill"])))
  (testing "returns a sequence of items for a dynamic list"
    (is (= (display-amounts {:price 10 :vat 2})
           [[:dt :price] [:dd 10] [:dt :vat] [:dd 2]]))))

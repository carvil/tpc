(ns tpc.test.models.types-test
  (:require [clojure.test :refer :all]
            [tpc.models.types :refer :all])
  (:import [tpc.models.types TPCEntry]))

(def airbnb-entry
  (->TPCEntry "10/04/2016"
              "07/04/2016"
              "4"
              "PA Orange"
              "Airbnb"
              "217,00 €"
              "0,00 €"
              "54,25 €"))

(def booking-entry
  (->TPCEntry "17/04/2016"
              "19/04/2016"
              "2"
              "PA Orange"
              "Booking"
              "140,00 €"
              "21,00 €"
              "29,75 €"))

(def expenses-entry
  (->TPCEntry "17/04/2016"
              "Fixing something"
              ""
              "PA Orange"
              ""
              ""
              "0,00 €"
              "20,00 €"))

(deftest airbnb-entry-test
  (testing "tpc-cost"
    (is (= (tpc-cost airbnb-entry) 54.25)))
  (testing "booking-cost"
    (is (= (booking-cost airbnb-entry) 0.0)))
  (testing "total-cost"
    (is (= (total-cost airbnb-entry) 54.25)))
  (testing "net-profit"
    (is (= (net-profit airbnb-entry) 162.75))))

(deftest booking-entry-test
  (testing "tpc-cost"
    (is (= (tpc-cost booking-entry) 29.75)))
  (testing "booking-cost"
    (is (= (booking-cost booking-entry) 21.0)))
  (testing "total-cost"
    (is (= (total-cost booking-entry) 50.75)))
  (testing "net-profit"
    (is (= (net-profit booking-entry) 89.25))))

(deftest expenses-entry-test
  (testing "tpc-cost"
    (is (= (tpc-cost expenses-entry) 20.0)))
  (testing "booking-cost"
    (is (= (booking-cost expenses-entry) 0.0)))
  (testing "total-cost"
    (is (= (total-cost expenses-entry) 20.0)))
  (testing "net-profit"
    (is (= (net-profit expenses-entry) -20.0))))

(deftest to-money-test
  (testing "with a string that isn't a number"
    (is (= (to-money "-") 0.0))))

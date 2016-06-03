(ns tpc.models.types)

(defn to-money [string]
  (cond
    (empty? string) 0.0
    :else (->
            string
            (.replace " €" "")
            (.replace "," ".")
            (Float.))))

(defprotocol Entry
  "A protocol defining the functions allowed on a given entry (i.e. a booking)"
  (tpc-cost [this] "The fee to be paid to TPC")
  (booking-cost [this] "The fee to be paid to booking dot com")
  (total-cost [this] "The total cost in fees")
  (net-profit [this] "The actual profit from the deal"))

(defrecord TPCEntry
  [date-in date-out nights property platform value channel-perc tpc-com]
  Entry
  (tpc-cost [this]
    (->
      (:tpc-com this)
      (to-money)))
  (booking-cost [this]
    (->
      (:channel-perc this)
      (to-money)))
  (total-cost [this]
    (+ (tpc-cost this) (booking-cost this)))
  (net-profit [this]
    (->
      (:value this)
      (to-money)
      (- (total-cost this)))))

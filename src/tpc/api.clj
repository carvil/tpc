(ns tpc.api
  (:require [tpc.models.types :as types]))

(defn find-page [file-path]
  "Given a path to a PDF file, fetch the first page of the document using Tabula"
  (->
    file-path
    (org.apache.pdfbox.pdmodel.PDDocument/load)
    (technology.tabula.ObjectExtractor.)
    (.extract)
    (.next)))

(defn tpc-comission? [cell]
  (= (.getText cell) "TPC COMISSION"))

(defn row-with-headers? [cells]
  (some tpc-comission? cells))

(defn table-with-entries? [table]
  (some row-with-headers? (.getRows table)))

(defn find-table [tables]
  (first (filter table-with-entries? tables)))

(defn merge-headers [raw-rows]
  "Takes a list of header and rows, and creates a list of maps mapping the
  headers with the rows"
  (map #(zipmap (first raw-rows) %) (rest raw-rows)))

(defn stringify-cells [cells]
  "Given a list of Cells, it extracts the text out of them."
  (map #(.getText %) cells))

(defn stringify-list [lst]
  "Converts rows of cells into strings."
  (map stringify-cells lst))

(defn valid-row? [row]
  "A row is valid if there is at least some info on it."
  (some #(not (empty? %)) row))

(defn delete-empty-rows [rows]
  "Some rows are empty, so this function filters them out."
  (filter valid-row? rows))

(defn find-rows [file-path]
  "Given a page of a PDF file, finds the last table, removes the header and
   returns the rows."
  (->
    (.extract (technology.tabula.extractors.SpreadsheetExtractionAlgorithm.)
              (find-page file-path))
    (find-table)
    (.getRows)
    (stringify-list)
    (delete-empty-rows)
    (merge-headers)))

(defn Row->TPCEntry [row]
  "Converts a single row into TPCEntry record."
  (types/->TPCEntry (get row "DATE IN")
                    (get row "DATE OUT")
                    (get row "NIGHTS")
                    (get row "PROPERTY")
                    (get row "PLATFORM")
                    (get row "VALUE")
                    (get row "CHANNEL %")
                    (get row "TPC COMISSION")))

(defn rows->entries [rows]
  "Converts a list of Rows into TPCEntry records."
  (map #(Row->TPCEntry %) rows))

(defn tpc-cost [rows]
  "Calculates the aggregate cost for TPC"
  (reduce + (map #(types/tpc-cost %) (rows->entries rows))))

(defn booking-cost [rows]
  "Calculates the aggregate cost for booking"
  (reduce + (map #(types/booking-cost %) (rows->entries rows))))

(defn total-cost [rows]
  "Calculates the aggregate cost"
  (reduce + (map #(types/total-cost %) (rows->entries rows))))

(defn net-profit [rows]
  "Calculates the net profit of this months' data"
  (reduce + (map #(types/net-profit %) (rows->entries rows))))

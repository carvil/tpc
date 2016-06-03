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

(defn find-rows [file-path]
  "Given a page of a PDF file, finds the last table, removes the header and
   returns the rows."
  (->
    (.extract (technology.tabula.extractors.SpreadsheetExtractionAlgorithm.)
              (find-page file-path))
    (.get 1)
    (.getRows)
    (reverse)
    (drop-last)))

(defn rows->entries [rows]
  "Converts a list of Rows into TPCEntry records."
  (map #(Row->TPCEntry %) rows))

(defn Row->TPCEntry [row]
  "Converts a single row into TPCEntry record."
  (apply types/->TPCEntry (map #(.getText %) row)))

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

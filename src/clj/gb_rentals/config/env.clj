(ns gb-rentals.config.env
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn read-config* []
  (aero/read-config (io/resource "config.edn")))

(def read-config (memoize read-config*))

(defn get-env [& path]
  (get-in (read-config) path))

(ns gb-rentals.server.handler
  (:require [gb-rentals.server.giantbomb :as gb]
            [ring.util.response :refer [response bad-request]]))

(defn search-games [params]
  (if-let [results (gb/search (merge {:resources "game"} params))]
    (response results)
    (bad-request {:error "Failed to search GiantBomb API."})))

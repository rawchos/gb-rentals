(ns gb-rentals.db)

;; TODO: Make sure if we reset state, we don't override the
;;       api-key if it's been set. Do give a way to set it again though. Maybe.
(def default-db
  {:search-text ""
   :search-results []
   :active-page :api-key
   :api-key ""})

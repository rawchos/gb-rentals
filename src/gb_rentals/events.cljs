(ns gb-rentals.events
  (:require [re-frame.core :as rf]
            [gb-rentals.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


(rf/reg-event-fx 
 :search
 (fn [cofx [_ search-text]]
   {:db (assoc-in (:db cofx) [:search-text] search-text)}))

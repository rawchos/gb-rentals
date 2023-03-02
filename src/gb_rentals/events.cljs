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
   (js/console.log "Setting search text: " search-text)
   {:db (assoc (:db cofx) :search-text search-text)}))

(rf/reg-event-fx 
 :add-api-key
 (fn [cofx [_ api-key]]
   (js/console.log "Setting api key: " api-key)
   {:db (assoc (:db cofx) :api-key api-key)
    :dispatch [:set-active-page :search]}))

(rf/reg-event-db
 :set-active-page
 (fn [db [_ page]]
   (assoc db :active-page page)))